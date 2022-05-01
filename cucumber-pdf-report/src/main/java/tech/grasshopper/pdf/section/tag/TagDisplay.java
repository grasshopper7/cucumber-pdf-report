package tech.grasshopper.pdf.section.tag;

import java.awt.Color;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
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
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.data.TagData;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.destination.DestinationAware;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.optimizer.TextSanitizer;
import tech.grasshopper.pdf.outline.Outline;
import tech.grasshopper.pdf.pojo.cucumber.Tag;
import tech.grasshopper.pdf.structure.Display;
import tech.grasshopper.pdf.structure.PageCreator;
import tech.grasshopper.pdf.structure.TableCreator;
import tech.grasshopper.pdf.structure.footer.CroppedMessage;
import tech.grasshopper.pdf.structure.paginate.PaginatedDisplay;
import tech.grasshopper.pdf.util.TextUtil;

@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class TagDisplay extends PaginatedDisplay implements DestinationAware {

	private TableBuilder tableBuilder;

	// private static final int TABLE_X_AXIS_START = 40;
	private static final int TABLE_Y_AXIS_START = 530;
	private static final int TABLE_Y_AXIS_END = 60;

	private static final PDFont HEADER_FONT = ReportFont.BOLD_ITALIC_FONT;
	private static final int HEADER_FONT_SIZE = 14;

	private static final PDFont NAME_FONT = ReportFont.ITALIC_FONT;
	private static final int NAME_FONT_SIZE = 12;

	private static final PDFont DATA_FONT = ReportFont.ITALIC_FONT;
	private static final int DATA_FONT_SIZE = 11;

	private static final float TAG_NAME_COLUMN_WIDTH = 400f;
	private static final float HEADER_PADDING = 9f;
	private static final float TAG_PADDING = 8f;
	private static final float DATA_PADDING = 5f;

	public static final float TABLE_SPACE = TABLE_Y_AXIS_START - TABLE_Y_AXIS_END;

	public static final TextLengthOptimizer tagNameTextOptimizer = TextLengthOptimizer.builder().font(NAME_FONT)
			.fontsize(NAME_FONT_SIZE).availableSpace(TAG_NAME_COLUMN_WIDTH - 2 * TAG_PADDING).maxLines(2).build();

	private static final String CROPPED_MESSAGE = "* The tag name has been cropped to fit in the available space.";

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private boolean nameCropped;

	public static float headerRowHeight() {
		return TextUtil.builder().font(HEADER_FONT).fontSize(HEADER_FONT_SIZE).text("Tag").width(TAG_NAME_COLUMN_WIDTH)
				.padding(HEADER_PADDING).build().tableRowHeight() * 2;
	}

	public static TextUtil tagNameTextUtil() {
		return TextUtil.builder().font(NAME_FONT).fontSize(NAME_FONT_SIZE).text("").width(TAG_NAME_COLUMN_WIDTH)
				.padding(TAG_PADDING).build();
	}

	@Override
	@SneakyThrows
	public void display() {

		page = PageCreator.builder().document(document).build()
				.createLandscapePageWithHeaderAndNumberAndAddToDocument(TagSection.SECTION_TITLE);

		content = new PDPageContentStream(document, page, AppendMode.APPEND, true);

		createTableBuilder();
		createHeaderRow();
		createDataRows();
		drawTable();
		croppedMessageDisplay();
		createDestination();

		content.close();
	}

	private void createTableBuilder() {
		// 760f
		tableBuilder = Table.builder().addColumnsOfWidth(TAG_NAME_COLUMN_WIDTH, 45f, 45f, 45f, 45f, 45f, 45f, 45f, 45f)
				.borderColor(Color.LIGHT_GRAY).borderWidth(1);
	}

	private void createHeaderRow() {
		tableBuilder.addRow(Row.builder().padding(HEADER_PADDING).horizontalAlignment(HorizontalAlignment.CENTER)
				.font(HEADER_FONT).fontSize(HEADER_FONT_SIZE).verticalAlignment(VerticalAlignment.MIDDLE)
				.add(TextCell.builder().text("Tag").build()).add(TextCell.builder().text("Scenario").colSpan(4).build())
				.add(TextCell.builder().text("Feature").colSpan(4).build()).build())

				.addRow(Row.builder().padding(HEADER_PADDING).horizontalAlignment(HorizontalAlignment.CENTER)
						.font(HEADER_FONT).fontSize(HEADER_FONT_SIZE).verticalAlignment(VerticalAlignment.MIDDLE)
						.add(TextCell.builder().text("Name").build())
						.add(TextCell.builder().text("T").textColor(reportConfig.getFeatureConfig().totalColor())
								.build())
						.add(TextCell.builder().text("P").textColor(reportConfig.passedColor()).build())
						.add(TextCell.builder().text("F").textColor(reportConfig.failedColor()).build())
						.add(TextCell.builder().text("S").textColor(reportConfig.skippedColor()).build())
						.add(TextCell.builder().text("T").textColor(reportConfig.getScenarioConfig().totalColor())
								.build())
						.add(TextCell.builder().text("P").textColor(reportConfig.passedColor()).build())
						.add(TextCell.builder().text("F").textColor(reportConfig.failedColor()).build())
						.add(TextCell.builder().text("S").textColor(reportConfig.skippedColor()).build()).build());
	}

	private void createDataRows() {
		TagData tagData = (TagData) displayData;
		List<Tag> tags = tagData.getTags();

		for (int i = 0; i < tags.size(); i++) {
			Tag tag = tags.get(i);

			tableBuilder.addRow(Row.builder().padding(DATA_PADDING).font(DATA_FONT).fontSize(DATA_FONT_SIZE)
					.horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.TOP)

					.add(createTagNameCell(tag))

					.add(TextCell.builder().text(String.valueOf(tag.getTotalScenarios()))
							.textColor(reportConfig.getScenarioConfig().totalColor()).build())
					.add(TextCell.builder().text(String.valueOf(tag.getPassedScenarios()))
							.textColor(reportConfig.passedColor()).build())
					.add(TextCell.builder().text(String.valueOf(tag.getFailedScenarios()))
							.textColor(reportConfig.failedColor()).build())
					.add(TextCell.builder().text(String.valueOf(tag.getSkippedScenarios()))
							.textColor(reportConfig.skippedColor()).build())

					.add(TextCell.builder().text(String.valueOf(tag.getTotalFeatures()))
							.textColor(reportConfig.getFeatureConfig().totalColor()).build())
					.add(TextCell.builder().text(String.valueOf(tag.getPassedFeatures()))
							.textColor(reportConfig.passedColor()).build())
					.add(TextCell.builder().text(String.valueOf(tag.getFailedFeatures()))
							.textColor(reportConfig.failedColor()).build())
					.add(TextCell.builder().text(String.valueOf(tag.getSkippedFeatures()))
							.textColor(reportConfig.skippedColor()).build())

					.build());
		}
	}

	private void drawTable() {
		TableCreator tableDrawer = TableCreator.builder().tableBuilder(tableBuilder).document(document)
				.startX(Display.CONTENT_START_X).startY(TABLE_Y_AXIS_START)
				.pageSupplier(PageCreator.builder().document(document).build().landscapePageSupplier()).build();
		tableDrawer.displayTable();
	}

	private AbstractCell createTagNameCell(Tag tag) {
		TextSanitizer sanitizer = TextSanitizer.builder().build();

		String tagName = sanitizer.sanitizeText(tagNameTextOptimizer.optimizeTextLines(tag.getName()));

		if (tagNameTextOptimizer.isTextTrimmed())
			nameCropped = true;

		return TextCell.builder().text(tagName).padding(TAG_PADDING).font(NAME_FONT).fontSize(NAME_FONT_SIZE)
				.horizontalAlignment(HorizontalAlignment.LEFT).textColor(statusColor(tag.getStatus())).build();
	}

	private void croppedMessageDisplay() {
		if (nameCropped)
			CroppedMessage.builder().content(content).message(CROPPED_MESSAGE).build().displayMessage();
	}

	@Override
	public void createDestination() {
		Destination destination = Destination.builder()
				.name(Outline.TAGS_SECTION_TEXT + " - " + (paginationData.getItemFromIndex() + 1) + " to "
						+ paginationData.getItemToIndex())
				.yCoord((int) page.getMediaBox().getHeight()).page(page).build();
		destinations.addTagDestination(destination);
	}
}
