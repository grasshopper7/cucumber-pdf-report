package tech.grasshopper.pdf.section.expanded;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.ImageCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextSanitizer;
import tech.grasshopper.pdf.pojo.cucumber.ExecutableEntity;
import tech.grasshopper.pdf.structure.Display;
import tech.grasshopper.pdf.structure.PageCreator;
import tech.grasshopper.pdf.structure.TableCreator;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ExpandedMediaDisplay extends Display /* implements DestinationAware */ {

	private ExecutableEntity executable;

	@Getter
	private float finalY;

	private int destinationY;

	@Override
	@SneakyThrows
	public void display() {

		TextSanitizer sanitizer = TextSanitizer.builder().build();
		PDPage initialPage = document.getPage(document.getNumberOfPages() - 1);
		destinationY = (int) ylocation;

		for (String media : executable.getMedia()) {

			PDImageXObject image = PDImageXObject.createFromFile(media, document);

			TableBuilder tableBuilder = Table.builder().addColumnsOfWidth(200f, 560f).borderWidth(1f)
					.borderColor(Color.GRAY).padding(5f).horizontalAlignment(HorizontalAlignment.LEFT)
					.verticalAlignment(VerticalAlignment.TOP).font(ReportFont.REGULAR_FONT)

					.addRow(Row.builder().font(ReportFont.BOLD_FONT).fontSize(10).add(TextCell.builder()
							.text(executable.getFeature().getName() + " / " + executable.getScenario().getName() + " / "
									+ executable.getDisplay().executableName() + " / " + "Media - ")
							.build()).add(ImageCell.builder().image(image).padding(5f).maxHeight(240f).build())
							.build());

			TableCreator tableCreator = TableCreator.builder().tableBuilder(tableBuilder).document(document)
					.startX(CONTENT_START_X).startY(ylocation).endY(DETAILED_CONTENT_END_Y).repeatRows(1)
					.pageSupplier(PageCreator.builder().document(document).build()
							.landscapePageWithHeaderAndNumberSupplier(ExpandedSection.SECTION_TITLE))
					.build();
			tableCreator.displayTable();

			finalY = tableCreator.getFinalY();

			ylocation = finalY - 10f;

			page = tableCreator.getTableStartPage();

			if (!initialPage.equals(page))
				destinationY = (int) Display.CONTENT_START_Y;
		}

		// createDestination();
	}

	/*
	 * @Override public void createDestination() {
	 * 
	 * Destination destination =
	 * Destination.builder().yCoord(destinationY).page(page).build();
	 * feature.setDestination(destination); }
	 */
}
