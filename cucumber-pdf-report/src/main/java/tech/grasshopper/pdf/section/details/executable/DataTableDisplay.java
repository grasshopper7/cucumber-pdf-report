package tech.grasshopper.pdf.section.details.executable;

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

	public AbstractCell display() {

		List<Float> columnTextWidths = TableColumnOptimizer.builder().rows(step.getRows()).fontsize(fontsize).build()
				.organizeColumnStructure();

		return TableCellWithMessage.builder().rows(step.getRows()).columnTextWidths(columnTextWidths)
				.backgroundColor(backgroundColor).fontsize(fontsize).textColor(textColor).build().createTableCell();
	}
}
