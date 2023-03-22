package tech.grasshopper.pdf.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tech.grasshopper.pdf.config.ReportConfig;
import tech.grasshopper.pdf.data.AttributeData.AuthorData;
import tech.grasshopper.pdf.data.AttributeData.DeviceData;
import tech.grasshopper.pdf.data.AttributeData.TagData;
import tech.grasshopper.pdf.exception.PdfReportException;
import tech.grasshopper.pdf.pojo.cucumber.Attribute;
import tech.grasshopper.pdf.pojo.cucumber.Author;
import tech.grasshopper.pdf.pojo.cucumber.Device;
import tech.grasshopper.pdf.pojo.cucumber.Executable;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;
import tech.grasshopper.pdf.pojo.cucumber.Status;
import tech.grasshopper.pdf.pojo.cucumber.Step;
import tech.grasshopper.pdf.pojo.cucumber.Tag;

@Getter
@Builder
public class ReportData {

	private List<Feature> features;

	private DashboardData summaryData;

	private FeatureData featureData;

	private ScenarioData scenarioData;

	private ExecutableData executableData;

	private TagData tagData;

	private DeviceData deviceData;

	private AuthorData authorData;

	@Setter
	private ReportConfig reportConfig;

	public void checkData() {

		if (features == null || features.size() == 0)
			throw new PdfReportException("No features present in test execution.");

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
		populateFeaturesScenariosStepsData();
		populateTagData();
		populateDeviceData();
		populateAuthorData();
	}

	private void populateCounts() {

		if (features == null || features.size() == 0)
			throw new PdfReportException("No features present in test execution.");

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

		summaryData = DashboardData.builder()
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

	private void populateFeaturesScenariosStepsData() {
		featureData = FeatureData.builder().features(features).build();
		scenarioData = ScenarioData.builder().build();
		executableData = ExecutableData.builder().build();

		for (Feature feature : features) {

			for (Scenario scenario : feature.getScenarios()) {
				scenario.setFeature(feature);
				scenarioData.getScenarios().add(scenario);

				for (Executable executable : scenario.getFilteredStepsAndHooks(reportConfig)) {
					executable.setScenario(scenario);
					executable.setFeature(feature);
					executableData.getExecutables().add(executable);
				}
			}
		}
	}

	private void populateTagData() {
		tagData = TagData.builder().build();
		List<Tag> tags = tagData.getTags();

		populateAttributeData(Tag::new, tags);
	}

	private void populateDeviceData() {
		deviceData = DeviceData.builder().build();
		List<Device> devices = deviceData.getDevices();

		populateAttributeData(Device::new, devices);
	}

	private void populateAuthorData() {
		authorData = AuthorData.builder().build();
		List<Author> authors = authorData.getAuthors();

		populateAttributeData(Author::new, authors);
	}

	private <T extends Attribute> void populateAttributeData(Function<String, T> newAttribute, List<T> attributes) {

		Class<? extends Attribute> clzAttr = newAttribute.apply("").getClass();

		for (Feature feature : features) {
			List<String> featureAttr = new ArrayList<>();

			for (Scenario scenario : feature.getScenarios()) {

				for (String attrName : scenario.getAttributes(clzAttr)) {

					T attr = newAttribute.apply(attrName);
					int attrIndex = attributes.indexOf(attr);

					if (attrIndex > -1)
						attr = attributes.get(attrIndex);
					else
						attributes.add(attr);

					if (scenario.getStatus() == Status.PASSED)
						attr.setPassedScenarios(attr.getPassedScenarios() + 1);
					else if (scenario.getStatus() == Status.FAILED)
						attr.setFailedScenarios(attr.getFailedScenarios() + 1);
					else
						attr.setSkippedScenarios(attr.getSkippedScenarios() + 1);

					attr.setTotalScenarios(attr.getTotalScenarios() + 1);

					if (!featureAttr.contains(attrName)) {
						if (feature.getStatus() == Status.PASSED)
							attr.setPassedFeatures(attr.getPassedFeatures() + 1);
						else if (feature.getStatus() == Status.FAILED)
							attr.setFailedFeatures(attr.getFailedFeatures() + 1);
						else
							attr.setSkippedFeatures(attr.getSkippedFeatures() + 1);

						attr.setTotalFeatures(attr.getTotalFeatures() + 1);
						featureAttr.add(attrName);
					}
				}
			}
		}

		for (T attr : attributes) {

			if (attr.getFailedScenarios() > 0 || attr.getFailedFeatures() > 0)
				attr.setStatus(Status.FAILED);
			else if ((attr.getPassedScenarios() == attr.getTotalScenarios() && attr.getFailedScenarios() == 0)
					&& attr.getPassedFeatures() == attr.getTotalFeatures() && attr.getFailedFeatures() == 0)
				attr.setStatus(Status.PASSED);
			else
				attr.setStatus(Status.SKIPPED);
		}
	}
}
