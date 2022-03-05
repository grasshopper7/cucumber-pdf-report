package tech.grasshopper.pdf.section.details.executable.table;

import java.awt.Color;
import java.util.List;

import org.vandeseer.easytable.structure.Row.RowBuilder;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.optimizer.TextSanitizer;
import tech.grasshopper.pdf.pojo.cucumber.Row;
import tech.grasshopper.pdf.structure.cell.TableWithinTableCell;

@Builder
public class TableCellWithMessage {

	@Setter
	private Color textColor;

	@Setter
	@Default
	private Color backgroundColor = Color.WHITE;

	@Setter
	private int fontsize;

	@Getter(value = AccessLevel.NONE)
	@Setter(value = AccessLevel.NONE)
	private boolean columnsCropped;

	@Getter(value = AccessLevel.NONE)
	@Setter(value = AccessLevel.NONE)
	private boolean cellTextCropped;

	@Getter(value = AccessLevel.NONE)
	@Setter(value = AccessLevel.NONE)
	private List<Float> maximumColumnTextWidths;

	@Setter
	private List<Row> rows;

	@Setter
	private List<Float> columnTextWidths;

	private static final float PADDING = 3f;
	private static final float INDICATOR_COLUMN_WIDTH = 4f;

	public AbstractCell createTableCell() {

		TableBuilder tableBuilder = Table.builder().backgroundColor(backgroundColor).font(ReportFont.REGULAR_FONT)
				.fontSize(fontsize).borderColor(Color.GRAY).borderWidth(1f).padding(PADDING);

		TextSanitizer sanitizer = TextSanitizer.builder().build();

		TextLengthOptimizer optimizer = TextLengthOptimizer.builder().font(ReportFont.REGULAR_FONT).fontsize(fontsize)
				.build();

		addColumnWidthsToTable(tableBuilder, columnTextWidths);
		boolean firstRow = true;

		for (Row row : rows) {

			RowBuilder rowBuilder = org.vandeseer.easytable.structure.Row.builder();
			for (int i = 0; i < columnTextWidths.size(); i++) {
				optimizer.setAvailableSpace(columnTextWidths.get(i));
				rowBuilder.add(TextCell.builder().textColor(textColor)
						.text(sanitizer.sanitizeText(optimizer.optimizeText(row.getCells().get(i)))).build());

				if (optimizer.isTextTrimmed())
					cellTextCropped = optimizer.isTextTrimmed();
			}

			croppedColumnIndicatorCells(rowBuilder, firstRow);
			if (firstRow)
				firstRow = false;

			tableBuilder.addRow(rowBuilder.build());
		}

		croppedMessage(tableBuilder, columnTextWidths, sanitizer.getStripMessage());
		return TableWithinTableCell.builder().table(tableBuilder.build()).borderColor(Color.GRAY).borderWidth(1)
				.build();
	}

	private void addColumnWidthsToTable(TableBuilder tableBuilder, List<Float> columnTextWidths) {

		columnTextWidths.forEach(w -> tableBuilder.addColumnOfWidth(w + (2 * PADDING)));

		if (columnsCropped)
			tableBuilder.addColumnOfWidth(INDICATOR_COLUMN_WIDTH + (2 * PADDING));
	}

	private void croppedColumnIndicatorCells(RowBuilder rowBuilder, boolean firstRow) {

		if (columnsCropped) {
			if (firstRow)
				rowBuilder.add(TextCell.builder().text("*").build());
			else
				rowBuilder.add(TextCell.builder().text("").build());
		}
	}

	private void croppedMessage(TableBuilder tableBuilder, List<Float> columnTextWidths, String sanitizerMessage) {

		if (sanitizerMessage.isEmpty() && !cellTextCropped && !columnsCropped)
			return;

		String croppedMsgPrefix = sanitizerMessage.isEmpty() ? "* The" : " The";
		String croppedMsgSuffix = "have been cropped to fit in the available space.";

		String message = "";
		int colSpan = columnTextWidths.size();

		if (cellTextCropped) {
			message = " data cell text(s) ";
		}

		if (columnsCropped) {
			if (cellTextCropped)
				message = message + "and";
			message = message + " extra column(s) ";
			colSpan++;
		}
		tableBuilder.addRow(org.vandeseer.easytable.structure.Row.builder()
				.add(TextCell.builder().text(sanitizerMessage + croppedMsgPrefix + message + croppedMsgSuffix)
						.colSpan(colSpan).textColor(textColor).build())
				.build());
	}
}
