package tech.grasshopper.pdf.section.feature;

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

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.annotation.Annotation;
import tech.grasshopper.pdf.data.FeatureData;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.optimizer.TextSanitizer;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.structure.Display;
import tech.grasshopper.pdf.structure.PageCreator;
import tech.grasshopper.pdf.structure.TableCreator;
import tech.grasshopper.pdf.structure.cell.TextLinkCell;
import tech.grasshopper.pdf.structure.footer.CroppedMessage;
import tech.grasshopper.pdf.structure.paginate.PaginationData;
import tech.grasshopper.pdf.util.DateUtil;
import tech.grasshopper.pdf.util.TextUtil;

@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class FeatureScenarioDetails extends Display {

	private PaginationData paginationData;

	private TableBuilder tableBuilder;

	private static final int TABLE_X_AXIS_START = 40;
	private static final int TABLE_Y_AXIS_START = 330;

	private static final PDFont HEADER_FONT = ReportFont.BOLD_ITALIC_FONT;
	private static final int HEADER_FONT_SIZE = 12;

	private static final PDFont NAME_FONT = ReportFont.ITALIC_FONT;
	private static final int NAME_FONT_SIZE = 11;

	private static final float FEATURE_NAME_COLUMN_WIDTH = 490f;
	private static final float HEADER_PADDING = 7f;
	private static final float DATA_PADDING = 6f;

	public static final float TABLE_SPACE = TABLE_Y_AXIS_START - Display.CONTENT_END_Y;

	public static final TextLengthOptimizer featureNameTextOptimizer = TextLengthOptimizer.builder().font(NAME_FONT)
			.fontsize(NAME_FONT_SIZE).availableSpace(FEATURE_NAME_COLUMN_WIDTH - 2 * DATA_PADDING).maxLines(2).build();

	private static final String CROPPED_MESSAGE = "* The feature name has been cropped to fit in the available space.";

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private boolean nameCropped;

	public static float headerRowHeight() {
		return TextUtil.builder().font(HEADER_FONT).fontSize(HEADER_FONT_SIZE).text("Feature Name")
				.width(FEATURE_NAME_COLUMN_WIDTH).padding(HEADER_PADDING).build().tableRowHeight();
	}

	public static TextUtil featureNameTextUtil() {
		return TextUtil.builder().font(NAME_FONT).fontSize(NAME_FONT_SIZE).text("").width(FEATURE_NAME_COLUMN_WIDTH)
				.padding(DATA_PADDING).build();
	}

	@Override
	public void display() {

		createTableBuilder();
		createHeaderRow();
		createDataRows();
		drawTable();
		croppedMessageDisplay();
	}

	private void createTableBuilder() {

		tableBuilder = Table.builder().addColumnsOfWidth(30f, FEATURE_NAME_COLUMN_WIDTH, 35f, 35f, 35f, 35f, 100f)
				.borderColor(Color.LIGHT_GRAY).borderWidth(1);
	}

	private void createHeaderRow() {

		tableBuilder.addRow(Row.builder().padding(HEADER_PADDING).horizontalAlignment(HorizontalAlignment.CENTER)
				.font(HEADER_FONT).fontSize(HEADER_FONT_SIZE).verticalAlignment(VerticalAlignment.MIDDLE)
				.add(TextCell.builder().text("#").build())
				.add(TextCell.builder().text("Feature Name").horizontalAlignment(HorizontalAlignment.LEFT).build())
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
		FeatureData featureData = (FeatureData) displayData;
		List<Feature> features = featureData.getFeatures();

		for (int i = 0; i < features.size(); i++) {
			Feature feature = features.get(i);

			tableBuilder.addRow(Row.builder().padding(DATA_PADDING).font(NAME_FONT).fontSize(NAME_FONT_SIZE)
					.horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.TOP)

					.add(TextCell.builder().text(String.valueOf(sNo)).fontSize(8).build())

					.add(createFeatureNameCell(feature))

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
	}

	private void drawTable() {

		TableCreator tableDrawer = TableCreator.builder().tableBuilder(tableBuilder).document(document)
				.startX(TABLE_X_AXIS_START).startY(TABLE_Y_AXIS_START)
				.pageSupplier(PageCreator.builder().document(document).build().landscapePageSupplier()).build();
		tableDrawer.displayTable();
	}

	private AbstractCell createFeatureNameCell(Feature feature) {

		TextSanitizer sanitizer = TextSanitizer.builder().build();
		String featureName = sanitizer.sanitizeText(featureNameTextOptimizer.optimizeTextLines(feature.getName()));

		if (featureNameTextOptimizer.isTextTrimmed())
			nameCropped = true;

		if (reportConfig.isDisplayFeature() && reportConfig.isDisplayDetailed()) {
			Annotation annotation = Annotation.builder().title(sanitizer.sanitizeText(feature.getName())).build();
			feature.addAnnotation(annotation);

			return TextLinkCell.builder().annotation(annotation).text(featureName)
					.textColor(statusColor(feature.getStatus())).horizontalAlignment(HorizontalAlignment.LEFT).build();
		}
		return TextCell.builder().text(featureName).textColor(statusColor(feature.getStatus()))
				.horizontalAlignment(HorizontalAlignment.LEFT).build();
	}

	private void croppedMessageDisplay() {

		if (nameCropped)
			CroppedMessage.builder().content(content).message(CROPPED_MESSAGE).build().displayMessage();
	}
}
