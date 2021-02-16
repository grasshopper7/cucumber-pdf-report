package tech.grasshopper.pdf.section.details;

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
import org.vandeseer.easytable.structure.cell.TableWithinTableCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.pojo.cucumber.Executable;

@Data
@Builder
public class MediaDisplay {

	private Executable executable;

	private PDDocument document;

	private static final int MAX_MEDIA_COUNT = 6;

	@SneakyThrows
	public AbstractCell display() {

		List<String> medias = executable.getMedia();
		float mediaWidth = 80f, mediaHeigth = 80f;
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
			mediaTableBuilder.addColumnOfWidth(100f);
			rowBuilder.add(TextCell.builder().font(ReportFont.REGULAR_FONT).fontSize(10).textColor(Color.RED)
					.text("Only first 6 medias are displayed.").build());
		}

		mediaTableBuilder.addRow(rowBuilder.build());

		return TableWithinTableCell.builder().table(mediaTableBuilder.build()).build();
	}
}
