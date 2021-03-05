package tech.grasshopper.pdf.section.details;

import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_COLUMN_WIDTH;
import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_PADDING;

import java.awt.Color;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Row.RowBuilder;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.ImageCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.pojo.cucumber.Executable;
import tech.grasshopper.pdf.tablecell.TableWithinTableCell;

@Data
@Builder
public class MediaDisplay {

	private Executable executable;

	private PDDocument document;

	private static final int MAX_MEDIA_COUNT = 5;
	private static final float AVAILABLE_COLUMN_WIDTH = STEP_HOOK_TEXT_COLUMN_WIDTH - (2 * STEP_HOOK_TEXT_PADDING);

	@SneakyThrows
	public AbstractCell display() {

		List<String> medias = executable.getMedia();
		float mediaWidth = 100f, mediaHeigth = 100f;
		float padding = 2f;

		TableBuilder mediaTableBuilder = Table.builder();
		RowBuilder rowBuilder = Row.builder();
		int mediaCount = medias.size() > MAX_MEDIA_COUNT ? MAX_MEDIA_COUNT : medias.size();

		for (int i = 0; i < mediaCount; i++) {
			mediaTableBuilder.addColumnOfWidth(mediaWidth);
			PDImageXObject image = PDImageXObject.createFromFile(medias.get(i), document);
			rowBuilder.add(
					ImageCell.builder().image(image).width(mediaWidth).padding(padding).maxHeight(mediaHeigth).build());
		}

		if (medias.size() > MAX_MEDIA_COUNT) {
			mediaTableBuilder.addColumnOfWidth(AVAILABLE_COLUMN_WIDTH - (MAX_MEDIA_COUNT * mediaWidth));
			rowBuilder.add(TextCell.builder().font(ReportFont.REGULAR_FONT).fontSize(9).textColor(Color.RED)
					.text("Only first " + MAX_MEDIA_COUNT + " medias are displayed.").build());
		}

		mediaTableBuilder.addRow(rowBuilder.build());

		return TableWithinTableCell.builder().table(mediaTableBuilder.build()).build();
	}
}
