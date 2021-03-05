package tech.grasshopper.pdf.section.details;

import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_COLUMN_WIDTH;
import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_PADDING;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.vandeseer.easytable.structure.Row.RowBuilder;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;
import org.vandeseer.easytable.util.PdfUtil;

import lombok.Builder;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.optimizer.TextSanitizer;
import tech.grasshopper.pdf.pojo.cucumber.Row;
import tech.grasshopper.pdf.pojo.cucumber.Step;
import tech.grasshopper.pdf.tablecell.TableWithinTableCell;

@Builder
public class DataTableDisplay {

	private Step step;

	private Color textColor;

	private Color backgroundColor;

	private final int fontsize = 9;

	private boolean rowsCropped;

	private boolean columnsCropped;

	private boolean cellTextCropped;

	private List<Float> maximumColumnTextWidths;

	private static final int MAX_ROW_COUNT = 5;
	private static final float MAX_COLUMN_WIDTH = 100f;
	private static final float PADDING = 3f;
	private static final float INDICATOR_COLUMN_WIDTH = 4f;
	private static final float AVAILABLE_COLUMN_WIDTH = STEP_HOOK_TEXT_COLUMN_WIDTH - (2 * STEP_HOOK_TEXT_PADDING);

	public AbstractCell display() {

		List<Row> rows = calculateRowCount();
		maximumColumnTextWidths = maximumColumnWidths(rows);

		List<Float> resizedColumnTextWidth = new ArrayList<>(maximumColumnTextWidths);
		resizeColumnWidth(resizedColumnTextWidth, false);

		List<Float> columnTextWidths = removeExtraColumns(resizedColumnTextWidth);

		return createDataTable(rows, columnTextWidths);

	}

	private List<Row> calculateRowCount() {

		List<Row> rows = step.getRows();
		int displayRows = rows.size();

		if (rows.size() > MAX_ROW_COUNT) {
			displayRows = MAX_ROW_COUNT;
			rowsCropped = true;
		}
		return rows.subList(0, displayRows);
	}

	private List<Float> maximumColumnWidths(List<Row> rows) {

		Float[][] textWidths = new Float[rows.get(0).getCells().size()][rows.size()];

		for (int i = 0; i < rows.size(); i++) {
			Row row = rows.get(i);
			for (int j = 0; j < row.getCells().size(); j++)
				textWidths[j][i] = PdfUtil.getStringWidth(row.getCells().get(j), ReportFont.REGULAR_FONT, fontsize);
		}

		List<Float> maxColWidths = new ArrayList<>();
		for (Float[] width : textWidths)
			maxColWidths.add(Collections.max(Arrays.asList(width)));

		return maxColWidths;
	}

	private void resizeColumnWidth(List<Float> columnMaxWidths, boolean removedColumn) {

		float availableWidth = AVAILABLE_COLUMN_WIDTH - (columnMaxWidths.size() * (2 * PADDING));

		if (removedColumn)
			availableWidth = availableWidth - (INDICATOR_COLUMN_WIDTH + ((2 * PADDING)));

		if (columnMaxWidths.stream().reduce(Float::sum).get() > availableWidth
				&& columnMaxWidths.stream().filter(w -> w > MAX_COLUMN_WIDTH).count() > 0) {

			float widthToFitIn = availableWidth
					- columnMaxWidths.stream().filter(w -> w <= MAX_COLUMN_WIDTH).reduce(Float::sum).orElse(0f);
			float widthToAdjust = columnMaxWidths.stream().filter(w -> w > MAX_COLUMN_WIDTH).reduce(Float::sum).get();

			float reductionFactor = widthToFitIn / widthToAdjust;

			for (int i = 0; i < columnMaxWidths.size(); i++) {

				if (columnMaxWidths.get(i) > MAX_COLUMN_WIDTH) {
					float resizedWidth = columnMaxWidths.get(i) * reductionFactor;
					resizedWidth = resizedWidth > MAX_COLUMN_WIDTH ? resizedWidth : MAX_COLUMN_WIDTH;
					columnMaxWidths.set(i, resizedWidth);
				}
			}
		}

		if (columnMaxWidths.stream().reduce(Float::sum).get() > availableWidth
				&& columnMaxWidths.stream().filter(w -> w > MAX_COLUMN_WIDTH).count() > 0)
			resizeColumnWidth(columnMaxWidths, false);
	}

	private List<Float> removeExtraColumns(List<Float> resizedColumnWidths) {

		float colSize = resizedColumnWidths.stream().reduce(Float::sum).get();
		List<Float> columnWidths = new ArrayList<>(resizedColumnWidths);
		float width = 0f;

		if (colSize > (AVAILABLE_COLUMN_WIDTH - (resizedColumnWidths.size() * (2 * PADDING)))) {

			for (int i = 0; i < resizedColumnWidths.size(); i++) {
				width = width + resizedColumnWidths.get(i);

				if (width > (AVAILABLE_COLUMN_WIDTH - ((i + 1) * (2 * PADDING)))) {
					columnWidths = new ArrayList<>(maximumColumnTextWidths.subList(0, i));
					resizeColumnWidth(columnWidths, true);
					columnsCropped = true;
					break;
				}
			}
		}
		return columnWidths;
	}

	private AbstractCell createDataTable(List<Row> rows, List<Float> columnTextWidths) {

		TableBuilder tableBuilder = Table.builder().backgroundColor(backgroundColor).font(ReportFont.REGULAR_FONT)
				.fontSize(fontsize).borderColor(Color.GRAY).borderWidth(1f).padding(PADDING);

		final TextSanitizer sanitizer = TextSanitizer.builder().build();

		final TextLengthOptimizer optimizer = TextLengthOptimizer.builder().font(ReportFont.REGULAR_FONT)
				.fontsize(fontsize).build();

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

		croppedMessage(tableBuilder, columnTextWidths);
		return TableWithinTableCell.builder().table(tableBuilder.build()).build();
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

	private void croppedMessage(TableBuilder tableBuilder, List<Float> columnTextWidths) {

		if (!rowsCropped && !cellTextCropped && !columnsCropped)
			return;

		final String croppedMsgPrefix = "* The";
		final String croppedMsgSuffix = "have been cropped to fit in the available space.";

		String message = "";
		int colSpan = columnTextWidths.size();

		if (rowsCropped)
			message = " data row(s), maximum " + MAX_ROW_COUNT + ", ";

		if (cellTextCropped) {
			if (rowsCropped)
				message = message + "and";
			message = message + " data cell text(s) ";
		}

		if (columnsCropped) {
			if (rowsCropped || cellTextCropped)
				message = message + "and";
			message = message + " extra column(s) ";
			colSpan++;
		}
		tableBuilder.addRow(org.vandeseer.easytable.structure.Row.builder().add(TextCell.builder()
				.text(croppedMsgPrefix + message + croppedMsgSuffix).colSpan(colSpan).textColor(textColor).build())
				.build());
	}
}
