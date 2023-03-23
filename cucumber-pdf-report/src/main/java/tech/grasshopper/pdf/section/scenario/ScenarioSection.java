package tech.grasshopper.pdf.section.scenario;

import static tech.grasshopper.pdf.section.scenario.ScenarioStepDetails.TABLE_SPACE;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.data.DisplayData;
import tech.grasshopper.pdf.data.ScenarioData;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;
import tech.grasshopper.pdf.structure.paginate.PaginatedSection;
import tech.grasshopper.pdf.structure.paginate.PaginationData;
import tech.grasshopper.pdf.structure.paginate.ScenarioPaginator;
import tech.grasshopper.pdf.util.TextUtil;

@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ScenarioSection extends PaginatedSection {

	protected ScenarioData scenarioData;

	@Setter
	protected List<Integer> featureNameRowSpans;

	@Override
	public void createSection() {
		scenarioData = (ScenarioData) displayData;
		if (scenarioData.getScenarios().isEmpty())
			return;

		ScenarioPaginator paginator = ScenarioPaginator.builder().data(scenarioData)
				.maxScenariosPerPage(maxScenariosPerPage()).tableSpace(tableSpace()).headerRowHeight(headerRowHeight())
				.textUtilFeature(featureNameTextUtil()).textUtilScenario(scenarioNameTextUtil())
				.featureNameTextOptimizer(featureNameTextOptimizer())
				.scenarioNameTextOptimizer(scenarioNameTextOptimizer()).section(this).build();
		paginator.paginate();
	}

	@Override
	public void generateDisplay(int fromIndex, int toIndex) {
		ScenarioDisplay.builder().displayData(createDisplayData(fromIndex, toIndex)).document(document)
				.reportConfig(reportConfig).destinations(destinations).paginationData(PaginationData.builder()
						.itemsPerPage(maxScenariosPerPage()).itemFromIndex(fromIndex).itemToIndex(toIndex).build())
				.featureNameRowSpans(featureNameRowSpans).build().display();
	}

	@Override
	public DisplayData createDisplayData(int fromIndex, int toIndex) {
		List<Scenario> pageScenarios = scenarioData.getScenarios().subList(fromIndex, toIndex);
		return ScenarioData.builder().scenarios(pageScenarios).build();
	}

	protected int maxScenariosPerPage() {
		return reportConfig.getScenarioConfig().scenarioCount();
	}

	protected float tableSpace() {
		return TABLE_SPACE;
	}

	protected float headerRowHeight() {
		return ScenarioStepDetails.headerRowTextUtil.tableRowHeight();
	}

	protected TextUtil featureNameTextUtil() {
		return ScenarioStepDetails.featureNameTextUtil;
	}

	protected TextUtil scenarioNameTextUtil() {
		return ScenarioStepDetails.scenarioNameTextUtil;
	}

	protected TextLengthOptimizer featureNameTextOptimizer() {
		return ScenarioStepDetails.featureNameTextOptimizer;
	}

	protected TextLengthOptimizer scenarioNameTextOptimizer() {
		return ScenarioStepDetails.scenarioNameTextOptimizer;
	}
}
