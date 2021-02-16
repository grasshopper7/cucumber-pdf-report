package tech.grasshopper.pdf.section.scenario;

import java.awt.Color;
import java.util.List;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.annotation.Annotation;
import tech.grasshopper.pdf.annotation.cell.TextLinkCell;
import tech.grasshopper.pdf.data.ScenarioData;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;
import tech.grasshopper.pdf.structure.Display;
import tech.grasshopper.pdf.structure.TableCreator;
import tech.grasshopper.pdf.structure.paginate.PaginationData;
import tech.grasshopper.pdf.util.DateUtil;
import tech.grasshopper.pdf.util.TextUtil;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ScenarioStepDetails extends Display /* implements AnnotationAware */ {

	private PaginationData paginationData;

	private TableBuilder tableBuilder;

	private static final int TABLE_X_AXIS_START = 40;
	private static final int TABLE_Y_AXIS_START = 330;

	private static final PDFont HEADER_FONT = ReportFont.BOLD_ITALIC_FONT;
	private static final int HEADER_FONT_SIZE = 12;

	private static final PDFont NAME_FONT = ReportFont.ITALIC_FONT;
	private static final int NAME_FONT_SIZE = 11;

	private static final float FEATURE_NAME_COLUMN_WIDTH = 210f;
	private static final float SCENARIO_NAME_COLUMN_WIDTH = 310f;
	private static final float HEADER_PADDING = 7f;
	private static final float DATA_PADDING = 6f;

	public static final float TABLE_SPACE = TABLE_Y_AXIS_START - Display.CONTENT_END_Y;

	public static final TextLengthOptimizer featureNameTextOptimizer = TextLengthOptimizer.builder().font(NAME_FONT)
			.fontsize(NAME_FONT_SIZE).availableSpace(FEATURE_NAME_COLUMN_WIDTH - 2 * DATA_PADDING).maxLines(2).build();

	public static final TextLengthOptimizer scenarioNameTextOptimizer = TextLengthOptimizer.builder().font(NAME_FONT)
			.fontsize(NAME_FONT_SIZE).availableSpace(SCENARIO_NAME_COLUMN_WIDTH - 2 * DATA_PADDING).maxLines(2).build();

	public static float headerRowHeight() {
		return TextUtil.builder().font(HEADER_FONT).fontSize(HEADER_FONT_SIZE).text("Scenario Name")
				.width(SCENARIO_NAME_COLUMN_WIDTH).padding(HEADER_PADDING).build().tableRowHeight();
	}

	public static TextUtil featureNameTextUtil() {
		return TextUtil.builder().font(NAME_FONT).fontSize(NAME_FONT_SIZE).text("").width(FEATURE_NAME_COLUMN_WIDTH)
				.padding(DATA_PADDING).build();
	}

	public static TextUtil scenarioNameTextUtil() {
		return TextUtil.builder().font(NAME_FONT).fontSize(NAME_FONT_SIZE).text("").width(SCENARIO_NAME_COLUMN_WIDTH)
				.padding(DATA_PADDING).build();
	}

	@Override
	public void display() {

		createTableBuilder();
		createHeaderRow();
		createDataRows();
		drawTable();
	}

	private void createTableBuilder() {

		tableBuilder = Table.builder()
				.addColumnsOfWidth(25f, FEATURE_NAME_COLUMN_WIDTH, SCENARIO_NAME_COLUMN_WIDTH, 30f, 30f, 30f, 30f, 95f)
				.borderColor(Color.LIGHT_GRAY).borderWidth(1);
	}

	private void createHeaderRow() {

		tableBuilder.addRow(Row.builder().padding(HEADER_PADDING).horizontalAlignment(HorizontalAlignment.CENTER)
				.verticalAlignment(VerticalAlignment.MIDDLE).font(HEADER_FONT).fontSize(HEADER_FONT_SIZE)
				.add(TextCell.builder().text("#").build())
				.add(TextCell.builder().text("Feature Name").horizontalAlignment(HorizontalAlignment.LEFT).build())
				.add(TextCell.builder().text("Scenario Name").horizontalAlignment(HorizontalAlignment.LEFT).build())
				.add(TextCell.builder().text("T").textColor(reportConfig.getFeatureConfig().totalColor()).build())
				.add(TextCell.builder().text("P").textColor(reportConfig.passedColor()).build())
				.add(TextCell.builder().text("F").textColor(reportConfig.failedColor()).build())
				.add(TextCell.builder().text("S").textColor(reportConfig.skippedColor()).build())
				.add(TextCell.builder().text("Duration").textColor(reportConfig.getFeatureConfig().durationColor())
						.horizontalAlignment(HorizontalAlignment.LEFT).build())
				.build());
	}

	private void createDataRows() {

		int sNo = paginationData.getItemFromIndex() + 1;
		ScenarioData scenarioData = (ScenarioData) displayData;
		List<Scenario> scenarios = scenarioData.getScenarios();

		for (int i = 0; i < scenarios.size(); i++) {
			Scenario scenario = scenarios.get(i);

			String featureName = featureNameTextOptimizer.optimizeTextLines(scenario.getFeature().getName());
			String scenarioName = scenarioNameTextOptimizer.optimizeTextLines(scenario.getName());

			Annotation featureAnnotation = Annotation.builder().title(featureName).build();
			Annotation scenarioAnnotation = Annotation.builder().title(scenarioName).build();

			tableBuilder.addRow(Row.builder().padding(DATA_PADDING).font(NAME_FONT).fontSize(NAME_FONT_SIZE)
					.horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.TOP)

					.add(TextCell.builder().text(String.valueOf(sNo)).fontSize(8).build())

					.add(createNameCell(featureName, featureAnnotation))
					.add(createNameCell(scenarioName, scenarioAnnotation))

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

			scenario.getFeature().addAnnotation(featureAnnotation);
			scenario.addAnnotation(scenarioAnnotation);

			sNo++;
		}
	}

	private void drawTable() {

		TableCreator tableDrawer = TableCreator.builder().tableBuilder(tableBuilder).document(document)
				.startX(TABLE_X_AXIS_START).startY(TABLE_Y_AXIS_START).build();
		tableDrawer.displayTable();
	}

	private AbstractCell createNameCell(String title, Annotation annotation) {

		if (reportConfig.isDisplayScenario() && reportConfig.isDisplayDetailed()) {
			return TextLinkCell.builder().annotation(annotation).text(title)
					.horizontalAlignment(HorizontalAlignment.LEFT).build();
		}
		return TextCell.builder().text(title).horizontalAlignment(HorizontalAlignment.LEFT).build();
	}
}
