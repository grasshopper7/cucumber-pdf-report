package tech.grasshopper.pdf.section.scenario;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.data.DisplayData;
import tech.grasshopper.pdf.data.ScenarioData;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;
import tech.grasshopper.pdf.structure.paginate.PaginatedSection;
import tech.grasshopper.pdf.structure.paginate.PaginationData;
import tech.grasshopper.pdf.structure.paginate.ScenarioPaginator;

@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ScenarioSection extends PaginatedSection {

	static final String SECTION_TITLE = "SCENARIOS SUMMARY";

	private final int maxScenariosPerPage = reportConfig.getScenarioConfig().scenarioCount();

	private ScenarioData scenarioData;

	@Override
	public void createSection() {
		scenarioData = (ScenarioData) displayData;
		if (scenarioData.getScenarios().isEmpty())
			return;

		ScenarioPaginator paginator = ScenarioPaginator.builder().data(scenarioData)
				.maxScenariosPerPage(maxScenariosPerPage).section(this).build();
		paginator.paginate();
	}

	@Override
	public void generateDisplay(int fromIndex, int toIndex) {
		ScenarioDisplay.builder().displayData(createDisplayData(fromIndex, toIndex)).document(document)
				.reportConfig(reportConfig).destinations(destinations).paginationData(PaginationData.builder()
						.itemsPerPage(maxScenariosPerPage).itemFromIndex(fromIndex).itemToIndex(toIndex).build())
				.build().display();
	}

	@Override
	public DisplayData createDisplayData(int fromIndex, int toIndex) {
		List<Scenario> pageScenarios = scenarioData.getScenarios().subList(fromIndex, toIndex);
		return ScenarioData.builder().scenarios(pageScenarios).build();
	}
}
