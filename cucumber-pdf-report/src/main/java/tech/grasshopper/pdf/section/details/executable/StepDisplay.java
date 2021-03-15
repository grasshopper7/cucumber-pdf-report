package tech.grasshopper.pdf.section.details.executable;

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
	protected String executableName() {
		return step.getKeyword() + " - " + step.getName();
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

		if (step.getDocString() != null && !step.getDocString().isEmpty())
			tableBuilder.addRow(Row.builder().add(DocStringDisplay.builder().step(step)
					.textColor(reportConfig.getDetailedStepHookConfig().stepTextColor())
					.backgroundColor(reportConfig.getDetailedStepHookConfig().stepBackgroundColor()).build().display())
					.build());

		if (step.getRows() != null && !step.getRows().isEmpty())
			tableBuilder.addRow(
					Row.builder().backgroundColor(reportConfig.getDetailedStepHookConfig().stepBackgroundColor())
							.add(DataTableDisplay.builder().step(step)
									.textColor(reportConfig.getDetailedStepHookConfig().stepTextColor())
									.backgroundColor(reportConfig.getDetailedStepHookConfig().stepBackgroundColor())
									.build().display())
							.build());
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
