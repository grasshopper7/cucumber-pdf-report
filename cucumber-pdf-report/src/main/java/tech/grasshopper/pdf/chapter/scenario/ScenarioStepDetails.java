package tech.grasshopper.pdf.chapter.scenario;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.annotation.Annotation;
import tech.grasshopper.pdf.annotation.AnnotationAware;
import tech.grasshopper.pdf.chapter.page.PaginationData;
import tech.grasshopper.pdf.component.Component;
import tech.grasshopper.pdf.data.ScenarioData;
import tech.grasshopper.pdf.exception.PdfReportException;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;
import tech.grasshopper.pdf.util.DateUtil;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ScenarioStepDetails extends Component implements AnnotationAware {

	private PaginationData paginationData;

	private static final int TABLE_X_AXIS_START = 50;
	private static final int TABLE_Y_AXIS_START = 440;
	private static final float TABLE_HEADER_HEIGHT = 25f;
	private static final float TABLE_ROW_HEIGHT = 22f;

	private static final PDFont NAME_FONT = ReportFont.ITALIC_FONT;
	private static final int NAME_FONT_SIZE = 10;

	private static final int FEATURE_NAME_COLUMN_WIDTH = 110;
	private static final int SCENARIO_NAME_COLUMN_WIDTH = 160;
	private static final int PADDING = 5;

	private final TextLengthOptimizer featureNameTextOptimizer = TextLengthOptimizer.builder().font(NAME_FONT)
			.fontsize(NAME_FONT_SIZE).spaceWidth(FEATURE_NAME_COLUMN_WIDTH - 2 * PADDING).build();

	private final TextLengthOptimizer scenarioNameTextOptimizer = TextLengthOptimizer.builder().font(NAME_FONT)
			.fontsize(NAME_FONT_SIZE).spaceWidth(SCENARIO_NAME_COLUMN_WIDTH - 2 * PADDING).build();

	@Override
	public void display() {
		TableBuilder myTableBuilder = Table.builder()
				.addColumnsOfWidth(25, FEATURE_NAME_COLUMN_WIDTH, SCENARIO_NAME_COLUMN_WIDTH, 30, 30, 30, 30, 85)
				.padding(PADDING).borderColor(Color.LIGHT_GRAY).borderWidth(1).font(PDType1Font.HELVETICA_BOLD_OBLIQUE)
				.fontSize(11)

				.addRow(Row.builder().horizontalAlignment(HorizontalAlignment.CENTER)
						.verticalAlignment(VerticalAlignment.MIDDLE).height(TABLE_HEADER_HEIGHT)
						.add(TextCell.builder().text("#").build())
						.add(TextCell.builder().text("Feature Name").horizontalAlignment(HorizontalAlignment.LEFT)
								.build())
						.add(TextCell.builder().text("Scenario Name").horizontalAlignment(HorizontalAlignment.LEFT)
								.build())
						.add(TextCell.builder().text("T").textColor(reportConfig.getFeatureConfig().totalColor())
								.build())
						.add(TextCell.builder().text("P").textColor(reportConfig.passedColor()).build())
						.add(TextCell.builder().text("F").textColor(reportConfig.failedColor()).build())
						.add(TextCell.builder().text("S").textColor(reportConfig.skippedColor()).build())
						.add(TextCell.builder().text("Duration")
								.textColor(reportConfig.getFeatureConfig().durationColor())
								.horizontalAlignment(HorizontalAlignment.LEFT).build())
						.build());

		int sNo = paginationData.getItemFromIndex() + 1;
		ScenarioData scenarioData = (ScenarioData) displayData;
		List<Scenario> scenarios = scenarioData.getScenarios();
		for (int i = 0; i < scenarios.size(); i++) {
			Scenario scenario = scenarios.get(i);

			String featureName = featureNameTextOptimizer.optimizeText(scenario.getFeature().getName());
			String scenarioName = scenarioNameTextOptimizer.optimizeText(scenario.getName());

			myTableBuilder.addRow(Row.builder().font(NAME_FONT).fontSize(NAME_FONT_SIZE).height(TABLE_ROW_HEIGHT)
					.horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.MIDDLE)
					.add(TextCell.builder().text(String.valueOf(sNo)).fontSize(8).build())
					.add(TextCell.builder().text(featureName).horizontalAlignment(HorizontalAlignment.LEFT).build())
					.add(TextCell.builder().text(scenarioName).horizontalAlignment(HorizontalAlignment.LEFT).build())
					.add(TextCell.builder().text(String.valueOf(scenario.getTotalSteps()))
							.textColor(reportConfig.getFeatureConfig().totalColor()).build())
					.add(TextCell.builder().text(String.valueOf(scenario.getPassedSteps()))
							.textColor(reportConfig.passedColor()).build())
					.add(TextCell.builder().text(String.valueOf(scenario.getFailedSteps()))
							.textColor(reportConfig.failedColor()).build())
					.add(TextCell.builder().text(String.valueOf(scenario.getSkippedSteps()))
							.textColor(reportConfig.skippedColor()).build())
					.add(TextCell.builder().text(DateUtil.durationValue(scenario.calculatedDuration()))
							.textColor(reportConfig.getFeatureConfig().durationColor())
							.horizontalAlignment(HorizontalAlignment.LEFT).build())
					.build());
			sNo++;
		}

		Table myTable = myTableBuilder.build();
		TableDrawer tableDrawer = TableDrawer.builder().contentStream(content).startX(TABLE_X_AXIS_START)
				.startY(TABLE_Y_AXIS_START).table(myTable).build();
		tableDrawer.draw();
	}

	@Override
	public void createAnnotationLinks() {

		int yCoord = TABLE_Y_AXIS_START + 5 - (int) (TABLE_HEADER_HEIGHT + TABLE_ROW_HEIGHT);

		ScenarioData scenarioData = (ScenarioData) displayData;
		List<Scenario> scenarios = scenarioData.getScenarios();
		for (int i = 0; i < scenarios.size(); i++) {
			Feature feature = scenarios.get(i).getFeature();

			Annotation annotation = Annotation.builder().title(feature.getName()).xBottom(80).yBottom(yCoord)
					.width(featureNameTextOptimizer.optimizedTextWidth(feature.getName())).height(18).build();
			PDAnnotationLink annotationLink = annotation.createPDAnnotationLink();
			feature.getAnnotations().add(annotationLink);
			try {
				page.getAnnotations().add(annotationLink);
			} catch (IOException e) {
				throw new PdfReportException(e);
			}

			annotation = Annotation.builder().title(scenarios.get(i).getName()).xBottom(190).yBottom(yCoord)
					.width(scenarioNameTextOptimizer.optimizedTextWidth(scenarios.get(i).getName())).height(15).build();
			annotationLink = annotation.createPDAnnotationLink();
			scenarios.get(i).getAnnotations().add(annotationLink);
			try {
				page.getAnnotations().add(annotationLink);
			} catch (IOException e) {
				throw new PdfReportException(e);
			}

			yCoord = yCoord - (int) (TABLE_ROW_HEIGHT);
		}
	}
}
