package tech.grasshopper.pdf.chapter.scenario;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.chapter.PaginatedChapter;
import tech.grasshopper.pdf.chapter.page.PaginationData;
import tech.grasshopper.pdf.chapter.page.Paginator;
import tech.grasshopper.pdf.data.DisplayData;
import tech.grasshopper.pdf.data.ScenarioData;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ScenarioChapter extends PaginatedChapter {

	private final int scenarioPerPage = reportConfig.getScenarioConfig().getItemcount();

	private ScenarioData scenarioData;

	@Override
	public void createChapter() {
		scenarioData = (ScenarioData) displayData;
		Paginator paginator = Paginator.builder().itemsCount(scenarioData.getScenarios().size())
				.itemsPerPage(scenarioPerPage).chapter(this).build();
		paginator.paginate();
	}

	@Override
	public void generatePage(int fromIndex, int toIndex, int pageNum) {
		ScenarioPage.builder().displayData(createPageData(fromIndex, toIndex)).document(document)
				.reportConfig(reportConfig).destinations(destinations).paginationData(PaginationData.builder()
						.itemsPerPage(scenarioPerPage).itemFromIndex(fromIndex).itemToIndex(toIndex).build())
				.build().createPage();
	}

	@Override
	public DisplayData createPageData(int fromIndex, int toIndex) {
		List<Scenario> pageScenarios = scenarioData.getScenarios().subList(fromIndex, toIndex);
		return ScenarioData.builder().scenarios(pageScenarios).build();
	}
}
