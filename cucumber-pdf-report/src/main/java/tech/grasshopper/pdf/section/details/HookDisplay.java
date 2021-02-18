package tech.grasshopper.pdf.section.details;

import java.awt.Color;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.pojo.cucumber.Hook;
import tech.grasshopper.pdf.util.DateUtil;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class HookDisplay extends ExecutableDisplay {

	private final Hook hook = (Hook) executable;

	@Override
	protected int processSNo(int serialNum) {
		return serialNum;
	}

	@Override
	protected String getDuration() {
		return DateUtil.durationValue(((Hook) executable).calculatedDuration());
	}

	@Override
	protected String executableName() {

		return hook.getHookType().toString() + " - " + hook.getLocation();
	}

	@Override
	protected String getSerialNumber() {
		return "";
	}

	@Override
	protected Color executableNameColor() {
		return reportConfig.getDetailedStepHookConfig().hookTextColor();
	}

	@Override
	protected Color executableBackgroundColor() {
		return reportConfig.getDetailedStepHookConfig().hookBackgroundColor();
	}
}
