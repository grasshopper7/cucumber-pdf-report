package tech.grasshopper.pdf.pojo.cucumber;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.exception.PdfReportException;
import tech.grasshopper.pdf.section.details.ExecutableDisplay;
import tech.grasshopper.pdf.section.details.HookDisplay;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Hook extends TimeDetails implements Executable {

	@Default
	private List<String> output = new ArrayList<>();
	@Default
	private List<String> media = new ArrayList<>();

	private Status status;
	@Default
	private String errorMessage = "";
	@Default
	private String location = "";

	private HookType hookType;

	@Override
	public ExecutableDisplay getDisplay() {
		return HookDisplay.builder().executable(this).build();
	}

	public static enum HookType {
		BEFORE, AFTER, BEFORE_STEP, AFTER_STEP;
	}

	@Override
	String getName() {
		return location;
	}

	public void checkData() {

		if (location == null || location.isEmpty())
			throw new PdfReportException("Location is null or empty for hook - " + getName());

		if (status == null)
			throw new PdfReportException("No status present for hook - " + getName());

		checkTimeData();
	}
}
