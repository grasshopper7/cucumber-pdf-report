package tech.grasshopper.pdf.config;

import java.util.ArrayList;
import java.util.List;

import tech.grasshopper.pdf.config.DetailedConfig.DetailedStepHookConfig;
import tech.grasshopper.pdf.pojo.cucumber.Executable;
import tech.grasshopper.pdf.pojo.cucumber.Hook;
import tech.grasshopper.pdf.pojo.cucumber.Status;
import tech.grasshopper.pdf.pojo.cucumber.Step;

public class StepHookFilter {

	public static List<Executable> allExecutables(List<Step> steps, List<Hook> before, List<Hook> after) {
		List<Executable> executables = new ArrayList<>();

		before.forEach(h -> executables.add(h));
		steps.forEach(s -> {
			s.getBefore().forEach(h -> executables.add(h));
			executables.add(s);
			s.getAfter().forEach(h -> executables.add(h));
		});
		after.forEach(h -> executables.add(h));

		return executables;
	}

	public static List<Executable> filterExecutables(ReportConfig reportConfig, List<Step> steps, List<Hook> before,
			List<Hook> after) {
		DetailedStepHookConfig detailedStepHookConfig = reportConfig.getDetailedStepHookConfig();

		List<Executable> executables = new ArrayList<>();

		before.forEach(h -> {
			if (!detailedStepHookConfig.isSkipScenarioBeforeHooks() || h.getStatus() != Status.PASSED)
				executables.add(h);
		});

		steps.forEach(s -> {
			s.getBefore().forEach(h -> {
				if (!detailedStepHookConfig.isSkipStepBeforeHooks() || h.getStatus() != Status.PASSED)
					executables.add(h);
			});

			executables.add(s);

			s.getAfter().forEach(h -> {
				if (!detailedStepHookConfig.isSkipStepAfterHooks() || h.getStatus() != Status.PASSED)
					executables.add(h);
			});
		});

		after.forEach(h -> {
			if (!detailedStepHookConfig.isSkipScenarioAfterHooks() || h.getStatus() != Status.PASSED)
				executables.add(h);
		});

		return executables;
	}
}
