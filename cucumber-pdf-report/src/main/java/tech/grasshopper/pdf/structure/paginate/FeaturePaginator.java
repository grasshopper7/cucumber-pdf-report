package tech.grasshopper.pdf.structure.paginate;

import static tech.grasshopper.pdf.section.feature.FeatureScenarioDetails.TABLE_SPACE;
import static tech.grasshopper.pdf.section.feature.FeatureScenarioDetails.featureNameTextOptimizer;
import static tech.grasshopper.pdf.section.feature.FeatureScenarioDetails.featureNameTextUtil;
import static tech.grasshopper.pdf.section.feature.FeatureScenarioDetails.headerRowHeight;

import lombok.Builder;
import tech.grasshopper.pdf.data.FeatureData;
import tech.grasshopper.pdf.util.TextUtil;

@Builder
public class FeaturePaginator {

	private FeatureData data;
	private PaginatedSection section;
	private int maxFeaturesPerPage;

	public void paginate() {

		float currentHeight = headerRowHeight();

		int fromIndex = 0;
		int toIndex = 0;

		TextUtil textUtil = featureNameTextUtil();

		for (int i = 0; i < data.getFeatures().size(); i++) {

			textUtil.setText(featureNameTextOptimizer.optimizeTextLines(data.getFeatures().get(i).getName()));

			currentHeight = currentHeight + textUtil.tableRowHeight();

			if (currentHeight > TABLE_SPACE || (toIndex - fromIndex) + 1 > maxFeaturesPerPage) {
				section.generateDisplay(fromIndex, toIndex);
				fromIndex = toIndex;
				currentHeight = headerRowHeight();
				i--;
			} else {
				toIndex++;
			}
		}

		// Remaining data
		section.generateDisplay(fromIndex, toIndex);
	}
}
