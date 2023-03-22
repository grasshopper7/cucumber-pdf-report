package tech.grasshopper.pdf.structure.paginate;

import static tech.grasshopper.pdf.section.scenario.ScenarioStepDetails.TABLE_SPACE;
import static tech.grasshopper.pdf.section.scenario.ScenarioStepDetails.featureNameTextOptimizer;
import static tech.grasshopper.pdf.section.scenario.ScenarioStepDetails.featureNameTextUtil;
import static tech.grasshopper.pdf.section.scenario.ScenarioStepDetails.headerRowHeight;
import static tech.grasshopper.pdf.section.scenario.ScenarioStepDetails.scenarioNameTextOptimizer;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import tech.grasshopper.pdf.data.ScenarioData;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;
import tech.grasshopper.pdf.section.scenario.ScenarioSection;
import tech.grasshopper.pdf.util.TextUtil;

@Builder
public class ScenarioPaginator {

	private ScenarioData data;
	private ScenarioSection section;
	private int maxScenariosPerPage;

	public void paginate() {

		float currentHeight = headerRowHeight();

		int fromIndex = 0;
		int toIndex = 0;

		TextUtil textUtilFeature = featureNameTextUtil();
		TextUtil textUtilScenario = featureNameTextUtil();

		List<Integer> featureRowSpans = new ArrayList<>();
		String firstUniqueFeatureName = "";
		int firstRowSpanIndex = 0;

		for (int i = 0; i < data.getScenarios().size(); i++) {

			Scenario scenario = data.getScenarios().get(i);

			String featureName = scenario.getFeature().getName();
			if (featureName.equals(firstUniqueFeatureName)) {
				//Update row span count of first feature name occurence
				featureRowSpans.set(firstRowSpanIndex, featureRowSpans.get(firstRowSpanIndex) + 1);
				//Set row span for 'empty' feature name
				featureRowSpans.add(0);
				featureName = "";
			} else {
				//Set index of first feature name occurence
				firstRowSpanIndex = featureRowSpans.size();
				firstUniqueFeatureName = featureName;
				featureRowSpans.add(1);
			}

			textUtilFeature.setText(featureNameTextOptimizer.optimizeTextLines(featureName));
			float featureHeight = textUtilFeature.tableRowHeight();

			textUtilScenario.setText(scenarioNameTextOptimizer.optimizeTextLines(scenario.getName()));
			float scenarioHeight = textUtilScenario.tableRowHeight();

			currentHeight = currentHeight + (scenarioHeight > featureHeight ? scenarioHeight : featureHeight);

			if (currentHeight > TABLE_SPACE || (toIndex - fromIndex) + 1 > maxScenariosPerPage) {
				//Subtract 1 from row span if last row in page is repeat feature name
				if (featureRowSpans.get(featureRowSpans.size() - 1) == 0) {
					featureRowSpans.set(firstRowSpanIndex, featureRowSpans.get(firstRowSpanIndex) - 1);
				}
				//Remove last row span element
				featureRowSpans.remove(featureRowSpans.size() - 1);

				section.setFeatureNameRowSpans(featureRowSpans);
				section.generateDisplay(fromIndex, toIndex);
				fromIndex = toIndex;
				featureRowSpans = new ArrayList<>();
				firstUniqueFeatureName = "";
				firstRowSpanIndex = 0;
				currentHeight = headerRowHeight();
				i--;
			} else {
				toIndex++;
			}
		}
		// Remaining data
		section.setFeatureNameRowSpans(featureRowSpans);
		section.generateDisplay(fromIndex, toIndex);
	}
}
