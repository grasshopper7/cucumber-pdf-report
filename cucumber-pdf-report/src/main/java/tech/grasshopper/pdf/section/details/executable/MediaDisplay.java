package tech.grasshopper.pdf.section.details.executable;

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
import lombok.Setter;
import lombok.SneakyThrows;
import tech.grasshopper.pdf.annotation.Annotation;
import tech.grasshopper.pdf.annotation.cell.TextLinkCell;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.pojo.cucumber.Executable;
import tech.grasshopper.pdf.tablecell.TableWithinTableCell;

@Data
@Builder
public class MediaDisplay {

	@Setter
	private Executable executable;

	@Setter
	private PDDocument document;

	@Setter
	private boolean expandView;

	private static final int MAX_MEDIA_COUNT_PER_ROW = 4;

	@SneakyThrows
	public AbstractCell display() {

		List<String> medias = executable.getMedia();
		float mediaWidth = 125f, mediaHeigth = 125f, plusWidth = 15f;
		float padding = 2f;

		TableBuilder mediaTableBuilder = Table.builder();
		RowBuilder rowBuilder = null;

		// Adding column widths
		for (int i = 0; i < MAX_MEDIA_COUNT_PER_ROW; i++)
			mediaTableBuilder.addColumnsOfWidth(mediaWidth, plusWidth);

		// Adding images and plus annotation
		for (int i = 0; i < medias.size(); i++) {

			if (i % MAX_MEDIA_COUNT_PER_ROW == 0) {
				if (rowBuilder != null)
					mediaTableBuilder.addRow(rowBuilder.build());
				rowBuilder = Row.builder();
			}

			PDImageXObject image = PDImageXObject.createFromFile(medias.get(i), document);
			rowBuilder.add(
					ImageCell.builder().image(image).width(mediaWidth).padding(padding).maxHeight(mediaHeigth).build());

			if (expandView) {
				Annotation annotation = Annotation.builder().title(String.valueOf(i)).build();
				rowBuilder.add(TextLinkCell.builder().text("+").annotation(annotation).font(ReportFont.REGULAR_FONT)
						.fontSize(15).textColor(Color.RED).padding(0f).build());
			} else {
				rowBuilder.add(TextCell.builder().text("").fontSize(0).padding(0f).build());
			}
		}

		// Adding blank cells for partial filled rows
		if ((medias.size() % MAX_MEDIA_COUNT_PER_ROW) > 0) {

			for (int i = 0; i < (MAX_MEDIA_COUNT_PER_ROW - (medias.size() % MAX_MEDIA_COUNT_PER_ROW)); i++) {

				rowBuilder.add(TextCell.builder().text("").fontSize(0).build());
				rowBuilder.add(TextCell.builder().text("").fontSize(0).build());
			}
		}

		mediaTableBuilder.addRow(rowBuilder.build());

		return TableWithinTableCell.builder().table(mediaTableBuilder.build()).borderColor(Color.GRAY).borderWidth(1)
				.build();
	}
}
