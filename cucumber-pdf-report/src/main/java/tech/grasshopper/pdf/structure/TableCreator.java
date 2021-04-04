package tech.grasshopper.pdf.structure;

import java.util.function.Supplier;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.vandeseer.easytable.RepeatedHeaderTableDrawer;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Table.TableBuilder;

import lombok.AccessLevel;
import lombok.Builder.Default;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class TableCreator {

	protected TableBuilder tableBuilder;

	protected PDDocument document;

	protected PDPage tableStartPage;

	@Default
	protected int repeatRows = 1;

	@Default
	protected boolean splitRow = false;

	protected float startX;

	protected float startY;

	protected float finalY;

	@Default
	protected float endY = Display.CONTENT_END_Y;

	@Default
	protected float offsetNewPageY = Display.CONTENT_MARGIN_TOP_Y;

	protected Supplier<PDPage> pageSupplier;

	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	protected TableDrawer tableDrawer;

	@SneakyThrows
	public void displayTable() {

		tableDrawer = RepeatedHeaderTableDrawer.builder().table(tableBuilder.build()).startX(startX).startY(startY)
				.splitRow(splitRow).endY(endY).numberOfRowsToRepeat(repeatRows).build();
		drawTable();
	}

	@SneakyThrows
	protected void drawTable() {

		tableDrawer.draw(() -> document, pageSupplier, offsetNewPageY);
		finalY = tableDrawer.getFinalY();
		tableStartPage = tableDrawer.getTableStartPage();
	}
}
