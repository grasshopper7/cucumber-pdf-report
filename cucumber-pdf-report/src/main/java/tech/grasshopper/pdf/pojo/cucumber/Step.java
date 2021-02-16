package tech.grasshopper.pdf.pojo.cucumber;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.exception.PdfReportException;
import tech.grasshopper.pdf.section.details.ExecutableDisplay;
import tech.grasshopper.pdf.section.details.StepDisplay;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Step extends TimeDetails implements Executable {

	private String name;
	private String keyword;

	@Default
	private List<Row> rows = new ArrayList<>();
	@Default
	private String docString = "";

	@Default
	private List<Hook> before = new ArrayList<>();
	@Default
	private List<Hook> after = new ArrayList<>();

	@Default
	private List<String> output = new ArrayList<>();
	@Default
	private List<String> media = new ArrayList<>();

	private Status status;
	@Default
	private String errorMessage = "";
	@Default
	private String location = "";

	public List<Hook> getBeforeAfterHooks() {
		List<Hook> hooks = new ArrayList<>();
		hooks.addAll(before);
		hooks.addAll(after);
		return hooks;
	}

	public void addBeforeStepHook(Hook hook) {
		before.add(hook);
	}

	public void addAfterStepHook(Hook hook) {
		after.add(hook);
	}

	@Override
	public ExecutableDisplay getDisplay() {
		return StepDisplay.builder().executable(this).build();
	}

	public void checkData() {

		if (name == null || name.isEmpty())
			throw new PdfReportException("Step text is null or empty.");

		if (keyword == null || keyword.isEmpty())
			throw new PdfReportException("Keyword is null or empty for step - " + getName());

		if (location == null || location.isEmpty())
			throw new PdfReportException("Location is null or empty for step - " + getName());

		if (status == null)
			throw new PdfReportException("No status present for step - " + getName());

		checkTimeData();
	}
}
