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
import org.vandeseer.easytable.structure.cell.TableWithinTableCell;
import org.vandeseer.easytable.structure.cell.TextCell;
import org.vandeseer.easytable.util.PdfUtil;

import lombok.Builder;
import lombok.Data;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.optimizer.TextSanitizer;
import tech.grasshopper.pdf.pojo.cucumber.Row;
import tech.grasshopper.pdf.pojo.cucumber.Step;

@Data
@Builder
public class DataTableDisplay {

	private Step step;
	private Color textColor;
	private Color backgroundColor;
	private final int fontsize = 9;

	private List<Float> maximumColumnTextWidths;

	private static final int MAX_ROW_COUNT = 5;
	private static final float MAX_COLUMN_WIDTH = 100f;
	private static final float PADDING = 3f;
	private static final float AVAILABLE_COLUMN_WIDTH = STEP_HOOK_TEXT_COLUMN_WIDTH - (2 * STEP_HOOK_TEXT_PADDING);

	public AbstractCell display() {

		List<Row> rows = displayRows();
		maximumColumnTextWidths = maximumColumnWidths(rows);
		List<Float> resizedColumnTextWidth = new ArrayList<>(maximumColumnTextWidths);
		resizeColumnWidth(resizedColumnTextWidth);
		List<Float> columnTextWidths = removeExtraColumns(resizedColumnTextWidth);

		TableBuilder tableBuilder = Table.builder().backgroundColor(backgroundColor).font(ReportFont.REGULAR_FONT)
				.fontSize(fontsize).borderColor(Color.GRAY).borderWidth(1f).padding(PADDING);

		final TextSanitizer sanitizer = TextSanitizer.builder().font(ReportFont.REGULAR_FONT).build();

		final TextLengthOptimizer optimizer = TextLengthOptimizer.builder().font(ReportFont.REGULAR_FONT)
				.fontsize(fontsize).build();

		columnTextWidths.forEach(w -> tableBuilder.addColumnOfWidth(w + (2 * PADDING)));

		for (Row row : rows) {

			RowBuilder rowBuilder = org.vandeseer.easytable.structure.Row.builder();
			for (int i = 0; i < columnTextWidths.size(); i++) {
				optimizer.setAvailableSpace(columnTextWidths.get(i));
				rowBuilder.add(TextCell.builder()
						.text(sanitizer.sanitizeText(optimizer.optimizeText(row.getCells().get(i)))).build());
			}
			tableBuilder.addRow(rowBuilder.build());
		}

		return TableWithinTableCell.builder().table(tableBuilder.build()).build();
	}

	private List<Row> displayRows() {

		List<Row> rows = step.getRows();
		int displayRows = rows.size() > MAX_ROW_COUNT ? MAX_ROW_COUNT : rows.size();
		return rows.subList(0, displayRows);
	}

	private List<Float> maximumColumnWidths(List<Row> rows) {

		Float[][] textWidths = new Float[rows.get(0).getCells().size()][rows.size()];

		for (int i = 0; i < rows.size(); i++) {
			Row row = rows.get(i);
			for (int j = 0; j < row.getCells().size(); j++)
				textWidths[j][i] = PdfUtil.getStringWidth(row.getCells().get(j), ReportFont.REGULAR_FONT, fontsize);
		}

		System.out.println(Arrays.deepToString(textWidths));

		List<Float> maxColWidths = new ArrayList<>();
		for (Float[] width : textWidths)
			maxColWidths.add(Collections.max(Arrays.asList(width)));

		System.out.println("Initial max - " + maxColWidths);
		return maxColWidths;
	}

	private void resizeColumnWidth(List<Float> columnMaxWidths) {

		if (columnMaxWidths.stream().reduce(Float::sum)
				.get() > (AVAILABLE_COLUMN_WIDTH - (columnMaxWidths.size() * (2 * PADDING)))
				&& columnMaxWidths.stream().filter(w -> w > MAX_COLUMN_WIDTH).count() > 0) {

			float widthToFitIn = AVAILABLE_COLUMN_WIDTH - (columnMaxWidths.size() * (2 * PADDING))
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

		System.out.println("resized - " + columnMaxWidths);

		if (columnMaxWidths.stream().reduce(Float::sum)
				.get() > (AVAILABLE_COLUMN_WIDTH - (columnMaxWidths.size() * (2 * PADDING)))
				&& columnMaxWidths.stream().filter(w -> w > MAX_COLUMN_WIDTH).count() > 0)
			resizeColumnWidth(columnMaxWidths);
	}

	private List<Float> removeExtraColumns(List<Float> resizedColumnWidths) {

		float colSize = resizedColumnWidths.stream().reduce(Float::sum).get();
		List<Float> columnWidths = new ArrayList<>(resizedColumnWidths);

		float width = 0f;
		if (colSize > (AVAILABLE_COLUMN_WIDTH - (resizedColumnWidths.size() * (2 * PADDING)))) {

			for (int i = 0; i < resizedColumnWidths.size(); i++) {
				width = width + resizedColumnWidths.get(i);

				if (width > (AVAILABLE_COLUMN_WIDTH - ((i + 1) * PADDING))) {
					columnWidths = new ArrayList<>(maximumColumnTextWidths.subList(0, i));
					resizeColumnWidth(columnWidths);
					break;
				}
			}
		}
		System.out.println("removed - " + columnWidths);
		return columnWidths;
	}
}
