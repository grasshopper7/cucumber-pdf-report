package tech.grasshopper.pdf.section.feature;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.data.DisplayData;
import tech.grasshopper.pdf.data.FeatureData;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.structure.paginate.FeaturePaginator;
import tech.grasshopper.pdf.structure.paginate.PaginatedSection;
import tech.grasshopper.pdf.structure.paginate.PaginationData;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class FeatureSection extends PaginatedSection {

	private final int maxFeaturesPerPage = reportConfig.getFeatureConfig().getItemcount();

	private FeatureData featureData;

	@Override
	public void createSection() {

		featureData = (FeatureData) displayData;
		FeaturePaginator paginator = FeaturePaginator.builder().data(featureData).maxFeaturesPerPage(maxFeaturesPerPage)
				.section(this).build();
		paginator.paginate();
	}

	@Override
	public void generateDisplay(int fromIndex, int toIndex) {

		FeatureDisplay.builder().displayData(createDisplayData(fromIndex, toIndex)).document(document)
				.reportConfig(reportConfig).destinations(destinations).paginationData(PaginationData.builder()
						.itemsPerPage(maxFeaturesPerPage).itemFromIndex(fromIndex).itemToIndex(toIndex).build())
				.build().display();
	}

	@Override
	public DisplayData createDisplayData(int fromIndex, int toIndex) {

		List<Feature> pageFeatures = featureData.getFeatures().subList(fromIndex, toIndex);
		return FeatureData.builder().features(pageFeatures).build();
	}
}
