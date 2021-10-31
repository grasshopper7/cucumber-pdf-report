package tech.grasshopper.pdf.section.details.executable;

import static tech.grasshopper.pdf.section.details.executable.DummyCellDisplay.dummyCellLeftBorder;
import static tech.grasshopper.pdf.section.details.executable.DummyCellDisplay.dummyCellRightBorder;

import java.awt.Color;

import org.vandeseer.easytable.structure.Row;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.pojo.cucumber.Step;
import tech.grasshopper.pdf.util.DateUtil;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class StepDisplay extends ExecutableDisplay {

	private final Step step = (Step) executable;

	@Override
	public int processSNo(int serialNum) {
		return serialNum + 1;
	}

	@Override
	protected String getDuration() {
		return DateUtil.durationValue(((Step) executable).calculatedDuration());
	}

	@Override
	public String executableName() {
		return step.getName();
	}

	@Override
	protected String getSerialNumber() {
		return String.valueOf(sNo);
	}

	@Override
	protected int getSubTypeRowSpanCount() {
		int span = 0;
		if (step.getDocString() != null && !step.getDocString().isEmpty())
			span++;
		if (step.getRows() != null && !step.getRows().isEmpty())
			span++;
		return span;
	}

	@Override
	protected void displaySubTypeDetails() {

		if (step.getDocString() != null && !step.getDocString().isEmpty()) {

			tableBuilder.addRow(Row.builder().add(dummyCellLeftBorder())
					.add(DocStringDisplay.builder().step(step)
							.textColor(reportConfig.getDetailedStepHookConfig().stepTextColor())
							.backgroundColor(reportConfig.getDetailedStepHookConfig().stepBackgroundColor()).build()
							.display())
					.add(dummyCellRightBorder()).add(dummyCellRightBorder()).build());
		}

		if (step.getRows() != null && !step.getRows().isEmpty()) {

			tableBuilder.addRow(Row.builder().add(dummyCellLeftBorder())
					.add(DataTableDisplay.builder().step(step)
							.textColor(reportConfig.getDetailedStepHookConfig().stepTextColor())
							.backgroundColor(reportConfig.getDetailedStepHookConfig().stepBackgroundColor()).build()
							.display())
					.add(dummyCellRightBorder()).add(dummyCellRightBorder()).build());
		}
	}

	@Override
	protected Color executableNameColor() {
		return reportConfig.getDetailedStepHookConfig().stepTextColor();
	}

	@Override
	protected Color executableBackgroundColor() {
		return reportConfig.getDetailedStepHookConfig().stepBackgroundColor();
	}
}
