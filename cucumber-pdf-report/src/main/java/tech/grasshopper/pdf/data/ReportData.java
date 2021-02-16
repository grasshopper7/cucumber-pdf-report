package tech.grasshopper.pdf.data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Data;
import tech.grasshopper.pdf.pojo.cucumber.Executable;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;
import tech.grasshopper.pdf.pojo.cucumber.Status;
import tech.grasshopper.pdf.pojo.cucumber.Step;

@Data
@Builder
public class ReportData {

	private List<Feature> features;

	private SummaryData summaryData;

	private FeatureData featureData;

	private ScenarioData scenarioData;

	public void checkData() {

		for (Feature feature : features) {
			feature.checkData();

			for (Scenario scenario : feature.getScenarios()) {
				scenario.checkData();

				for (Executable executable : scenario.getStepsAndHooks()) {
					executable.checkData();
				}
			}
		}
	}

	public void populateSectionData() {

		populateCounts();
		populateDashboardData();
		populateFeaturesData();
		populateScenariosData();
	}

	private void populateCounts() {

		for (Feature feature : features) {

			for (Scenario scenario : feature.getScenarios()) {

				for (Step step : scenario.getSteps()) {

					if (step.getStatus() == Status.PASSED) {
						scenario.setPassedSteps(scenario.getPassedSteps() + 1);
						feature.setPassedSteps(feature.getPassedSteps() + 1);
					} else if (step.getStatus() == Status.FAILED) {
						scenario.setFailedSteps(scenario.getFailedSteps() + 1);
						feature.setFailedSteps(feature.getFailedSteps() + 1);
					} else {
						scenario.setSkippedSteps(scenario.getSkippedSteps() + 1);
						feature.setSkippedSteps(feature.getSkippedSteps() + 1);
					}

					scenario.setTotalSteps(scenario.getTotalSteps() + 1);
					feature.setTotalSteps(feature.getTotalSteps() + 1);
				}

				if (scenario.getStatus() == Status.PASSED)
					feature.setPassedScenarios(feature.getPassedScenarios() + 1);
				else if (scenario.getStatus() == Status.FAILED)
					feature.setFailedScenarios(feature.getFailedScenarios() + 1);
				else
					feature.setSkippedScenarios(feature.getSkippedScenarios() + 1);

				feature.setTotalScenarios(feature.getTotalScenarios() + 1);
			}
		}
	}

	private void populateDashboardData() {

		summaryData = SummaryData.builder()
				.testRunStartTime(
						Collections.min(features.stream().map(Feature::getStartTime).collect(Collectors.toList())))
				.testRunEndTime(
						Collections.max(features.stream().map(Feature::getEndTime).collect(Collectors.toList())))
				.build();

		for (Feature feature : features) {
			if (feature.getStatus() == Status.PASSED)
				summaryData.setPassedFeatures(summaryData.getPassedFeatures() + 1);
			else if (feature.getStatus() == Status.FAILED)
				summaryData.setFailedFeatures(summaryData.getFailedFeatures() + 1);
			else
				summaryData.setSkippedFeatures(summaryData.getSkippedFeatures() + 1);

			summaryData.setTotalFeatures(summaryData.getTotalFeatures() + 1);

			summaryData.setPassedScenarios(summaryData.getPassedScenarios() + feature.getPassedScenarios());
			summaryData.setFailedScenarios(summaryData.getFailedScenarios() + feature.getFailedScenarios());
			summaryData.setSkippedScenarios(summaryData.getSkippedScenarios() + feature.getSkippedScenarios());
			summaryData.setTotalScenarios(summaryData.getTotalScenarios() + feature.getTotalScenarios());

			summaryData.setPassedSteps(summaryData.getPassedSteps() + feature.getPassedSteps());
			summaryData.setFailedSteps(summaryData.getFailedSteps() + feature.getFailedSteps());
			summaryData.setSkippedSteps(summaryData.getSkippedSteps() + feature.getSkippedSteps());
			summaryData.setTotalSteps(summaryData.getTotalSteps() + feature.getTotalSteps());
		}
	}

	private void populateFeaturesData() {
		featureData = FeatureData.builder().features(features).build();
	}

	private void populateScenariosData() {

		scenarioData = ScenarioData.builder().build();
		for (Feature feature : features) {
			for (Scenario scenario : feature.getScenarios()) {
				scenario.setFeature(feature);
				scenarioData.getScenarios().add(scenario);
			}
		}
	}
}
