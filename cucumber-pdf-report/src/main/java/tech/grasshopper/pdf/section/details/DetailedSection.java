package tech.grasshopper.pdf.section.details;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.data.FeatureData;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;
import tech.grasshopper.pdf.structure.Display;
import tech.grasshopper.pdf.structure.PageCreator;
import tech.grasshopper.pdf.structure.Section;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class DetailedSection extends Section {

	static final String SECTION_TITLE = "DETAILED SECTION";

	@Getter
	protected FeatureData detailedData;

	protected static final float GAP = 10f;

	protected static float ylocation = Display.CONTENT_START_Y;

	@Override
	public void createSection() {
		detailedData = (FeatureData) displayData;
		if (detailedData.getFeatures().isEmpty())
			return;

		PageCreator.builder().document(document).build()
				.createLandscapePageWithHeaderAndNumberAndAddToDocument(SECTION_TITLE);

		// float ylocation = Display.CONTENT_START_Y;

		for (Feature feature : detailedData.getFeatures()) {

			DetailedFeatureDisplay featureDisplay = DetailedFeatureDisplay.builder().feature(feature)
					.ylocation(ylocation).document(document).reportConfig(reportConfig).build();
			featureDisplay.display();

			ylocation = featureDisplay.getFinalY() - GAP;

			featureAdditionalInfoDisplay(feature);

			for (Scenario scenario : feature.getScenarios()) {

				DetailedScenarioDisplay scenarioDisplay = DetailedScenarioDisplay.builder().feature(feature)
						.scenario(scenario).ylocation(ylocation).document(document).reportConfig(reportConfig).build();
				scenarioDisplay.display();

				ylocation = scenarioDisplay.getFinalY() - GAP;

				scenarioAdditionalInfoDisplay(scenario);

				DetailedStepHookDisplay stepHookDisplay = DetailedStepHookDisplay.builder()
						.executables(scenario.getFilteredStepsAndHooks(reportConfig)).ylocation(ylocation)
						.document(document).reportConfig(reportConfig).build();
				stepHookDisplay.display();

				ylocation = stepHookDisplay.getFinalY() - GAP;
			}
		}
	}

	// Overwritten in specialized implementation eg. rest assured
	protected void featureAdditionalInfoDisplay(Feature feature) {

	}

	// Overwritten in specialized implementation eg. rest assured
	protected void scenarioAdditionalInfoDisplay(Scenario scenario) {

	}
}
