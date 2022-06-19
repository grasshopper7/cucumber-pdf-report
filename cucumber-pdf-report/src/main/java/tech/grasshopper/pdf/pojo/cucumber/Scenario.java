package tech.grasshopper.pdf.pojo.cucumber;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.config.ReportConfig;
import tech.grasshopper.pdf.config.StepHookFilter;
import tech.grasshopper.pdf.exception.PdfReportException;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Scenario extends NonExecutable {

	private Feature feature;

	@Default
	private List<Hook> before = new ArrayList<>();
	@Default
	private List<Step> steps = new ArrayList<>();
	@Default
	private List<Hook> after = new ArrayList<>();
	@Default
	private List<String> tags = new ArrayList<>();

	@Default
	private int passedSteps = 0;
	@Default
	private int failedSteps = 0;
	@Default
	private int skippedSteps = 0;
	@Default
	private int totalSteps = 0;

	public List<Hook> getBeforeAfterHooks() {
		List<Hook> hooks = new ArrayList<>();
		hooks.addAll(before);
		hooks.addAll(after);
		return hooks;
	}

	public List<Executable> getStepsAndHooks() {
		return StepHookFilter.allExecutables(steps, before, after);
	}

	public List<Executable> getFilteredStepsAndHooks(ReportConfig reportConfig) {
		return StepHookFilter.filterExecutables(reportConfig, steps, before, after);
	}

	@Override
	public void checkData() {

		if (name == null || name.isEmpty())
			throw new PdfReportException("Scenario name is null or empty.");

		if (feature == null)
			throw new PdfReportException("No feature present for scenario - " + getName());

		if (steps == null || steps.isEmpty())
			throw new PdfReportException("No steps present for scenario - " + getName());

		if (status == null)
			throw new PdfReportException("No status present for scenario - " + getName());

		super.checkData();
	}
}
