package tech.grasshopper.pdf.section.attributes;

import java.awt.Color;
import java.util.List;
import java.util.function.Consumer;

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
import tech.grasshopper.pdf.data.AttributeData;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.destination.DestinationAware;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.optimizer.TextSanitizer;
import tech.grasshopper.pdf.pojo.cucumber.Attribute;
import tech.grasshopper.pdf.structure.Display;
import tech.grasshopper.pdf.structure.PageCreator;
import tech.grasshopper.pdf.structure.TableCreator;
import tech.grasshopper.pdf.structure.footer.CroppedMessage;
import tech.grasshopper.pdf.structure.paginate.PaginatedDisplay;
import tech.grasshopper.pdf.util.TextUtil;

@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class AttributeDisplay extends PaginatedDisplay implements DestinationAware {

	private TableBuilder tableBuilder;

	private String attributeType;

	private Consumer<Destination> attributeDestinationConsumer;

	// private static final int TABLE_X_AXIS_START = 40;
	private static final int TABLE_Y_AXIS_START = 530;
	private static final int TABLE_Y_AXIS_END = 60;

	private static final PDFont HEADER_FONT = ReportFont.BOLD_ITALIC_FONT;
	private static final int HEADER_FONT_SIZE = 14;

	private static final PDFont NAME_FONT = ReportFont.ITALIC_FONT;
	private static final int NAME_FONT_SIZE = 12;

	private static final PDFont DATA_FONT = ReportFont.ITALIC_FONT;
	private static final int DATA_FONT_SIZE = 11;

	private static final float ATTRIBUTE_NAME_COLUMN_WIDTH = 400f;
	private static final float HEADER_PADDING = 9f;
	private static final float ATTRIBUTE_PADDING = 8f;
	private static final float DATA_PADDING = 5f;

	public static final float TABLE_SPACE = TABLE_Y_AXIS_START - TABLE_Y_AXIS_END;

	public static final TextLengthOptimizer attributeNameTextOptimizer = TextLengthOptimizer.builder().font(NAME_FONT)
			.fontsize(NAME_FONT_SIZE).availableSpace(ATTRIBUTE_NAME_COLUMN_WIDTH - 2 * ATTRIBUTE_PADDING).maxLines(2)
			.build();

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private boolean nameCropped;

	public static float headerRowHeight() {
		// Text does not matter
		return TextUtil.builder().font(HEADER_FONT).fontSize(HEADER_FONT_SIZE).text("Attribute")
				.width(ATTRIBUTE_NAME_COLUMN_WIDTH).padding(HEADER_PADDING).build().tableRowHeight() * 2;
	}

	public static TextUtil attributeNameTextUtil() {
		return TextUtil.builder().font(NAME_FONT).fontSize(NAME_FONT_SIZE).text("").width(ATTRIBUTE_NAME_COLUMN_WIDTH)
				.padding(ATTRIBUTE_PADDING).build();
	}

	@Override
	@SneakyThrows
	public void display() {

		page = PageCreator.builder().document(document).build()
				.createLandscapePageWithHeaderAndNumberAndAddToDocument(attributeType + " SUMMARY");

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
		tableBuilder = Table.builder()
				.addColumnsOfWidth(ATTRIBUTE_NAME_COLUMN_WIDTH, 45f, 45f, 45f, 45f, 45f, 45f, 45f, 45f)
				.borderColor(Color.LIGHT_GRAY).borderWidth(1);
	}

	private void createHeaderRow() {
		tableBuilder
				.addRow(Row.builder().padding(HEADER_PADDING).horizontalAlignment(HorizontalAlignment.CENTER)
						.font(HEADER_FONT).fontSize(HEADER_FONT_SIZE).verticalAlignment(VerticalAlignment.MIDDLE)
						.add(TextCell.builder().text(attributeType).build())
						.add(TextCell.builder().text("Scenario").colSpan(4).build())
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
		AttributeData attributeData = (AttributeData) displayData;
		List<? extends Attribute> attributes = attributeData.getAttributes();

		for (int i = 0; i < attributes.size(); i++) {
			Attribute attribute = attributes.get(i);

			tableBuilder.addRow(Row.builder().padding(DATA_PADDING).font(DATA_FONT).fontSize(DATA_FONT_SIZE)
					.horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.TOP)

					.add(createAttributeNameCell(attribute))

					.add(TextCell.builder().text(String.valueOf(attribute.getTotalScenarios()))
							.textColor(reportConfig.getScenarioConfig().totalColor()).build())
					.add(TextCell.builder().text(String.valueOf(attribute.getPassedScenarios()))
							.textColor(reportConfig.passedColor()).build())
					.add(TextCell.builder().text(String.valueOf(attribute.getFailedScenarios()))
							.textColor(reportConfig.failedColor()).build())
					.add(TextCell.builder().text(String.valueOf(attribute.getSkippedScenarios()))
							.textColor(reportConfig.skippedColor()).build())

					.add(TextCell.builder().text(String.valueOf(attribute.getTotalFeatures()))
							.textColor(reportConfig.getFeatureConfig().totalColor()).build())
					.add(TextCell.builder().text(String.valueOf(attribute.getPassedFeatures()))
							.textColor(reportConfig.passedColor()).build())
					.add(TextCell.builder().text(String.valueOf(attribute.getFailedFeatures()))
							.textColor(reportConfig.failedColor()).build())
					.add(TextCell.builder().text(String.valueOf(attribute.getSkippedFeatures()))
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

	private AbstractCell createAttributeNameCell(Attribute attr) {
		TextSanitizer sanitizer = TextSanitizer.builder().build();

		String attrName = sanitizer.sanitizeText(attributeNameTextOptimizer.optimizeTextLines(attr.getName()));

		if (attributeNameTextOptimizer.isTextTrimmed())
			nameCropped = true;

		return TextCell.builder().text(attrName).padding(ATTRIBUTE_PADDING).font(NAME_FONT).fontSize(NAME_FONT_SIZE)
				.horizontalAlignment(HorizontalAlignment.LEFT).textColor(statusColor(attr.getStatus())).build();
	}

	private void croppedMessageDisplay() {
		if (nameCropped)
			CroppedMessage.builder().content(content)
					.message("* The " + attributeType + " name has been cropped to fit in the available space.").build()
					.displayMessage();
	}

	@Override
	public void createDestination() {
		Destination destination = Destination.builder()
				.name(attributeType + "S - " + (paginationData.getItemFromIndex() + 1) + " to "
						+ paginationData.getItemToIndex())
				.yCoord((int) page.getMediaBox().getHeight()).page(page).build();
		// destinations.addTagDestination(destination);

		attributeDestinationConsumer.accept(destination);
	}
}
