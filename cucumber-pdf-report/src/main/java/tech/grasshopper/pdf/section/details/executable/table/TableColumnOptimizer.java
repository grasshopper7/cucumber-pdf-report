package tech.grasshopper.pdf.section.details.executable.table;

import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_COLUMN_WIDTH;
import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_PADDING;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.vandeseer.easytable.util.PdfUtil;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.pojo.cucumber.Row;

@Builder
public class TableColumnOptimizer {

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

	private static final float MAX_COLUMN_WIDTH = 100f;
	private static final float PADDING = 3f;
	private static final float INDICATOR_COLUMN_WIDTH = 4f;
	private static final float AVAILABLE_COLUMN_WIDTH = STEP_HOOK_TEXT_COLUMN_WIDTH - (2 * STEP_HOOK_TEXT_PADDING);

	public List<Float> organizeColumnStructure() {

		maximumColumnTextWidths = maximumColumnWidths();

		List<Float> resizedColumnTextWidth = new ArrayList<>(maximumColumnTextWidths);
		resizeColumnWidth(resizedColumnTextWidth, false);

		return removeExtraColumns(resizedColumnTextWidth);
	}

	private List<Float> maximumColumnWidths() {

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
}
