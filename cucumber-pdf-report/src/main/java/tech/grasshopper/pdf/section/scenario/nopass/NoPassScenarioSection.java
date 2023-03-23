package tech.grasshopper.pdf.section.scenario.nopass;

import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.section.scenario.ScenarioSection;
import tech.grasshopper.pdf.structure.paginate.PaginationData;
import tech.grasshopper.pdf.util.TextUtil;

@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class NoPassScenarioSection extends ScenarioSection {

	@Override
	public void generateDisplay(int fromIndex, int toIndex) {
		NoPassScenarioDisplay.builder().displayData(createDisplayData(fromIndex, toIndex)).document(document)
				.reportConfig(reportConfig).destinations(destinations).paginationData(PaginationData.builder()
						.itemsPerPage(maxScenariosPerPage()).itemFromIndex(fromIndex).itemToIndex(toIndex).build())
				.featureNameRowSpans(featureNameRowSpans).build().display();
	}

	@Override
	protected int maxScenariosPerPage() {
		return reportConfig.getNoPassScenarioConfig().scenarioCount();
	}

	protected float headerRowHeight() {
		return NoPassScenarioDisplay.headerRowTextUtil.tableRowHeight();
	}

	protected TextUtil featureNameTextUtil() {
		return NoPassScenarioDisplay.featureNameTextUtil;
	}

	protected TextUtil scenarioNameTextUtil() {
		return NoPassScenarioDisplay.scenarioNameTextUtil;
	}

	protected TextLengthOptimizer featureNameTextOptimizer() {
		return NoPassScenarioDisplay.featureNameTextOptimizer;
	}

	protected TextLengthOptimizer scenarioNameTextOptimizer() {
		return NoPassScenarioDisplay.scenarioNameTextOptimizer;
	}
}
