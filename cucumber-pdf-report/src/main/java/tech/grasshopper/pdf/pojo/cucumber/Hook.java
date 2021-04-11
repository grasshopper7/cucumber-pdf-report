package tech.grasshopper.pdf.pojo.cucumber;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.exception.PdfReportException;
import tech.grasshopper.pdf.section.details.executable.ExecutableDisplay;
import tech.grasshopper.pdf.section.details.executable.HookDisplay;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Hook extends Executable {

	private HookType hookType;

	@Override
	public ExecutableDisplay getDisplay() {
		return HookDisplay.builder().executable(this).build();
	}

	public static enum HookType {
		BEFORE, AFTER, BEFORE_STEP, AFTER_STEP;
	}

	@Override
	public String getName() {
		return location;
	}

	public void checkData() {

		if (location == null || location.isEmpty())
			throw new PdfReportException("Location is null or empty for hook - " + getName());

		if (status == null)
			throw new PdfReportException("No status present for hook - " + getName());

		super.checkData();
	}
}
