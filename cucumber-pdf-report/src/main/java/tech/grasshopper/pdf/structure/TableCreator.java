package tech.grasshopper.pdf.structure;

import java.util.function.Supplier;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.vandeseer.easytable.RepeatedHeaderTableDrawer;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Table.TableBuilder;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;

@Data
@Builder
public class TableCreator {

	private TableBuilder tableBuilder;

	private PDDocument document;

	@Getter
	private PDPage tableStartPage;

	@Default
	private int repeatRows = 1;

	private float startX;

	private float startY;

	@Getter
	private float finalY;

	@Default
	private float endY = Display.CONTENT_END_Y;

	@Default
	private float offsetNewPageY = Display.CONTENT_MARGIN_TOP_Y;

	private Supplier<PDPage> pageSupplier;

	@SneakyThrows
	public void displayTable() {

		TableDrawer tableDrawer = RepeatedHeaderTableDrawer.builder().table(tableBuilder.build()).startX(startX)
				.startY(startY).endY(endY).numberOfRowsToRepeat(repeatRows).build();

		tableDrawer.draw(() -> document, pageSupplier, offsetNewPageY);
		finalY = tableDrawer.getFinalY();
		tableStartPage = tableDrawer.getTableStartPage();
	}
}
