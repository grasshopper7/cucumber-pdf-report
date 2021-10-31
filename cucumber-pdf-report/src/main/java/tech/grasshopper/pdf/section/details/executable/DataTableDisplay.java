package tech.grasshopper.pdf.section.details.executable;

import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_COLUMN_WIDTH;
import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_PADDING;

import java.awt.Color;
import java.util.List;

import org.vandeseer.easytable.structure.cell.AbstractCell;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tech.grasshopper.pdf.pojo.cucumber.Step;
import tech.grasshopper.pdf.section.details.executable.table.TableCellWithMessage;
import tech.grasshopper.pdf.section.details.executable.table.TableColumnOptimizer;

@Builder
public class DataTableDisplay {

	@Setter
	private Step step;

	@Setter
	private Color textColor;

	@Setter
	private Color backgroundColor;

	private final int fontsize = 9;

	@Getter(value = AccessLevel.NONE)
	@Setter(value = AccessLevel.NONE)
	private boolean columnsCropped;

	@Getter(value = AccessLevel.NONE)
	@Setter(value = AccessLevel.NONE)
	private boolean cellTextCropped;

	@Getter(value = AccessLevel.NONE)
	@Setter(value = AccessLevel.NONE)
	private List<Float> maximumColumnTextWidths;

	private static final float MAX_COLUMN_WIDTH = 100f;
	private static final float PADDING = 3f;
	private static final float INDICATOR_COLUMN_WIDTH = 4f;
	private static final float AVAILABLE_COLUMN_WIDTH = STEP_HOOK_TEXT_COLUMN_WIDTH - (2 * STEP_HOOK_TEXT_PADDING);

	public AbstractCell display() {

		List<Float> columnTextWidths = TableColumnOptimizer.builder().rows(step.getRows()).fontsize(fontsize).build()
				.organizeColumnStructure();

		return TableCellWithMessage.builder().rows(step.getRows()).columnTextWidths(columnTextWidths)
				.backgroundColor(backgroundColor).fontsize(fontsize).textColor(textColor).build().createTableCell();
	}
}
