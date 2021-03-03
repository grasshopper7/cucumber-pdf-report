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
	private FeatureData detailedData;

	private static final float GAP = 10f;

	@Override
	public void createSection() {

		detailedData = (FeatureData) displayData;

		PageCreator.builder().document(document).build()
				.createLandscapePageWithHeaderAndNumberAndAddToDocument(SECTION_TITLE);

		float ylocation = Display.CONTENT_START_Y;

		for (Feature feature : detailedData.getFeatures()) {

			DetailedFeatureDisplay featureDisplay = DetailedFeatureDisplay.builder().feature(feature)
					.ylocation(ylocation).document(document).reportConfig(reportConfig).build();
			featureDisplay.display();

			ylocation = featureDisplay.getFinalY() - GAP;

			for (Scenario scenario : feature.getScenarios()) {

				DetailedScenarioDisplay scenarioDisplay = DetailedScenarioDisplay.builder().feature(feature)
						.scenario(scenario).ylocation(ylocation).document(document).reportConfig(reportConfig).build();
				scenarioDisplay.display();

				ylocation = scenarioDisplay.getFinalY() - GAP;

				DetailedStepHookDisplay stepHookDisplay = DetailedStepHookDisplay.builder()
						.executables(scenario.getStepsAndHooks()).ylocation(ylocation).document(document)
						.reportConfig(reportConfig).build();
				stepHookDisplay.display();

				ylocation = stepHookDisplay.getFinalY() - GAP;
			}
		}
	}
}
