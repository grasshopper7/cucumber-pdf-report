package tech.grasshopper.pdf.chapter.feature;

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
import tech.grasshopper.pdf.data.FeatureData;
import tech.grasshopper.pdf.exception.PdfReportException;
import tech.grasshopper.pdf.optimizer.TextContentSanitizer;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.util.DateUtil;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class FeatureScenarioDetails extends Component implements AnnotationAware {

	private PaginationData paginationData;

	private static final int TABLE_X_AXIS_START = 50;
	private static final int TABLE_Y_AXIS_START = 430;
	private static final float TABLE_HEADER_HEIGHT = 30f;
	private static final float TABLE_ROW_HEIGHT = 25f;

	private static final PDFont NAME_FONT = PDType1Font.HELVETICA_OBLIQUE;
	private static final int NAME_FONT_SIZE = 10;

	private static final int FEATURE_NAME_COLUMN_WIDTH = 225;
	private static final int PADDING = 5;

	private final TextLengthOptimizer featureNameTextOptimizer = TextLengthOptimizer.builder().font(NAME_FONT)
			.fontsize(NAME_FONT_SIZE).spaceWidth(FEATURE_NAME_COLUMN_WIDTH - 2 * PADDING).build();

	private final TextContentSanitizer textSanitizer = TextContentSanitizer.builder().font(NAME_FONT).build();

	@Override
	public void display() {
		TableBuilder myTableBuilder = Table.builder()
				.addColumnsOfWidth(25, FEATURE_NAME_COLUMN_WIDTH, 35, 35, 35, 35, 110).padding(PADDING)
				.borderColor(Color.LIGHT_GRAY).borderWidth(1).font(PDType1Font.HELVETICA_BOLD_OBLIQUE).fontSize(12)

				.addRow(Row.builder().horizontalAlignment(HorizontalAlignment.CENTER)
						.verticalAlignment(VerticalAlignment.MIDDLE).height(TABLE_HEADER_HEIGHT)
						.add(TextCell.builder().text("#").build())
						.add(TextCell.builder().text("Feature Name").horizontalAlignment(HorizontalAlignment.LEFT)
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
		FeatureData featureData = (FeatureData) displayData;
		List<Feature> features = featureData.getFeatures();
		for (int i = 0; i < features.size(); i++) {
			Feature feature = features.get(i);

			String featureName = featureNameTextOptimizer.optimizeText(textSanitizer.sanitizeText(feature.getName()));

			myTableBuilder.addRow(Row.builder().font(NAME_FONT).fontSize(NAME_FONT_SIZE).height(TABLE_ROW_HEIGHT)
					.horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.MIDDLE)
					.add(TextCell.builder().text(String.valueOf(sNo)).fontSize(8).build())
					.add(TextCell.builder().text(featureName).horizontalAlignment(HorizontalAlignment.LEFT).build())
					.add(TextCell.builder().text(String.valueOf(feature.getTotalScenarios()))
							.textColor(reportConfig.getFeatureConfig().totalColor()).build())
					.add(TextCell.builder().text(String.valueOf(feature.getPassedScenarios()))
							.textColor(reportConfig.passedColor()).build())
					.add(TextCell.builder().text(String.valueOf(feature.getFailedScenarios()))
							.textColor(reportConfig.failedColor()).build())
					.add(TextCell.builder().text(String.valueOf(feature.getSkippedScenarios()))
							.textColor(reportConfig.skippedColor()).build())
					.add(TextCell.builder().text(DateUtil.durationValue(feature.calculatedDuration()))
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

		FeatureData featureData = (FeatureData) displayData;
		List<Feature> features = featureData.getFeatures();
		for (int i = 0; i < features.size(); i++) {

			Annotation annotation = Annotation.builder().title(features.get(i).getName()).xBottom(80).yBottom(yCoord)
					.width(featureNameTextOptimizer.optimizedTextWidth(features.get(i).getName())).height(20).build();
			PDAnnotationLink annotationLink = annotation.createPDAnnotationLink();
			features.get(i).getAnnotations().add(annotationLink);
			try {
				page.getAnnotations().add(annotationLink);
			} catch (IOException e) {
				throw new PdfReportException(e);
			}
			yCoord = yCoord - (int) (TABLE_ROW_HEIGHT);
		}
	}
}
