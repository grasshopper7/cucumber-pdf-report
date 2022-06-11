package tech.grasshopper.pdf.section.expanded;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.ImageCell;
import org.vandeseer.easytable.structure.cell.TextCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.annotation.Annotation;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.destination.DestinationAware;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.optimizer.TextSanitizer;
import tech.grasshopper.pdf.pojo.cucumber.Executable;
import tech.grasshopper.pdf.structure.Display;
import tech.grasshopper.pdf.structure.PageCreator;
import tech.grasshopper.pdf.structure.TableCreator;
import tech.grasshopper.pdf.structure.cell.TextLinkCell;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ExpandedMediaDisplay extends Display implements DestinationAware {

	private Executable executable;

	@Getter
	private float finalY;

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private int destinationY;

	private static final float GAP = 10f;
	private static final float DATA_COLUMN_WIDTH = 200f;
	private static final float IMAGE_COLUMN_WIDTH = 560f;
	private static final float PADDING = 5f;

	private static final PDFont DATA_FONT = ReportFont.REGULAR_FONT;
	private static final int DATA_FONT_SIZE = 10;

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private final TextSanitizer sanitizer = TextSanitizer.builder().build();

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private final TextLengthOptimizer optimizer = TextLengthOptimizer.builder().font(DATA_FONT).fontsize(DATA_FONT_SIZE)
			.availableSpace(DATA_COLUMN_WIDTH - (2 * PADDING)).maxLines(4).build();

	@Override
	@SneakyThrows
	public void display() {

		PDPage initialPage = document.getPage(document.getNumberOfPages() - 1);

		for (int i = 0; i < executable.getMedia().size(); i++) {

			destinationY = (int) ylocation;

			String media = executable.getMedia().get(i);
			PDImageXObject image = PDImageXObject.createFromFile(media, document);

			TableBuilder tableBuilder = Table.builder().addColumnsOfWidth(DATA_COLUMN_WIDTH, IMAGE_COLUMN_WIDTH)
					.borderWidth(1f).borderColor(Color.GRAY).padding(PADDING)
					.horizontalAlignment(HorizontalAlignment.LEFT).verticalAlignment(VerticalAlignment.TOP)
					.font(DATA_FONT).fontSize(DATA_FONT_SIZE).textColor(Color.DARK_GRAY)

					.addRow(Row.builder().add(stepDetails(executable, i))
							.add(ImageCell.builder().image(image).maxHeight(240f).rowSpan(3).borderWidth(0f).padding(0f)
									.build())
							.build())
					.addRow(Row.builder().add(scenarioDetails(executable)).build())
					.addRow(Row.builder().add(featureDetails(executable)).build());

			TableCreator tableCreator = TableCreator.builder().tableBuilder(tableBuilder).document(document)
					.startX(CONTENT_START_X).startY(ylocation).endY(DETAILED_CONTENT_END_Y).repeatRows(1)
					.pageSupplier(PageCreator.builder().document(document).build()
							.landscapePageWithHeaderAndNumberSupplier(ExpandedSection.SECTION_TITLE))
					.build();
			tableCreator.displayTable();

			page = tableCreator.getTableStartPage();
			ylocation = tableCreator.getFinalY() - GAP;

			if (!initialPage.equals(page)) {
				destinationY = (int) Display.CONTENT_START_Y;
				initialPage = page;
			}

			createDestination();
		}
		finalY = ylocation;
	}

	private ParagraphCell stepDetails(Executable executable, int index) {
		ParagraphBuilder detailsBuilder = Paragraph.builder();

		if (executable.getMedia().size() > 1) {
			detailsBuilder.append(StyledText.builder().text("Media No : " + (index + 1))
					.color(reportConfig.getDetailedStepHookConfig().stepTextColor()).build());
			detailsBuilder.appendNewLine();
		}

		detailsBuilder.append(StyledText.builder()
				.text(optimizer.optimizeTextLines(
						"(Step) " + sanitizer.sanitizeText(executable.getDisplay().executableName())))
				.color(reportConfig.getDetailedStepHookConfig().stepTextColor()).build());
		return ParagraphCell.builder().paragraph(detailsBuilder.build()).lineSpacing(1.3f).build();
	}

	private AbstractCell featureDetails(Executable executable) {

		if (reportConfig.isDisplayDetailed() && reportConfig.isDisplayExpanded()) {
			Annotation featureAnnotation = Annotation.builder()
					.title(sanitizer.sanitizeText(executable.getFeature().getName())).build();
			executable.getFeature().addAnnotation(featureAnnotation);

			return TextLinkCell.builder()
					.text(optimizer
							.optimizeTextLines("(F) " + sanitizer.sanitizeText(executable.getFeature().getName())))
					.textColor(reportConfig.getDetailedFeatureConfig().featureNameColor()).annotation(featureAnnotation)
					.build();
		}

		return TextCell.builder()
				.text(optimizer.optimizeTextLines("(F) " + sanitizer.sanitizeText(executable.getFeature().getName())))
				.textColor(reportConfig.getDetailedFeatureConfig().featureNameColor()).build();
	}

	private AbstractCell scenarioDetails(Executable executable) {

		if (reportConfig.isDisplayDetailed() && reportConfig.isDisplayExpanded()) {
			Annotation scenarioAnnotation = Annotation.builder()
					.title(sanitizer.sanitizeText(executable.getScenario().getName())).build();
			executable.getScenario().addAnnotation(scenarioAnnotation);

			return TextLinkCell.builder()
					.text(optimizer
							.optimizeTextLines("(S) " + sanitizer.sanitizeText(executable.getScenario().getName())))
					.textColor(reportConfig.getDetailedScenarioConfig().scenarioNameColor())
					.annotation(scenarioAnnotation).build();
		}

		return TextCell.builder()
				.text(optimizer.optimizeTextLines("(S) " + sanitizer.sanitizeText(executable.getScenario().getName())))
				.textColor(reportConfig.getDetailedScenarioConfig().scenarioNameColor()).build();
	}

	@Override
	public void createDestination() {
		Destination destination = Destination.builder().yCoord(destinationY).page(page).build();
		executable.getDestinations().add(destination);
	}
}