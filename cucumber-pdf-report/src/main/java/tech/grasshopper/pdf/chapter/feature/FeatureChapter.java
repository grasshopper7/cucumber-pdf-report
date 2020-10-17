package tech.grasshopper.pdf.chapter.feature;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.chapter.PaginatedChapter;
import tech.grasshopper.pdf.chapter.page.PaginationData;
import tech.grasshopper.pdf.chapter.page.Paginator;
import tech.grasshopper.pdf.data.DisplayData;
import tech.grasshopper.pdf.data.FeatureData;
import tech.grasshopper.pdf.pojo.cucumber.Feature;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class FeatureChapter extends PaginatedChapter {

	private final int featurePerPage = reportConfig.getFeatureConfig().getItemcount();

	private FeatureData featureData;

	@Override
	public void createChapter() {
		featureData = (FeatureData) displayData;
		Paginator paginator = Paginator.builder().itemsCount(featureData.getFeatures().size())
				.itemsPerPage(featurePerPage).chapter(this).build();
		paginator.paginate();
	}

	@Override
	public void generatePage(int fromIndex, int toIndex, int pageNum) {
		FeaturePage.builder().displayData(createPageData(fromIndex, toIndex)).document(document)
				.reportConfig(reportConfig).destinations(destinations).paginationData(PaginationData.builder()
						.itemsPerPage(featurePerPage).itemFromIndex(fromIndex).itemToIndex(toIndex).build())
				.build().createPage();
	}

	@Override
	public DisplayData createPageData(int fromIndex, int toIndex) {
		List<Feature> pageFeatures = featureData.getFeatures().subList(fromIndex, toIndex);
		return FeatureData.builder().features(pageFeatures).build();
	}
}
