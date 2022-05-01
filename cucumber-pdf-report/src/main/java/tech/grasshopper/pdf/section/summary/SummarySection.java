package tech.grasshopper.pdf.section.summary;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.data.DisplayData;
import tech.grasshopper.pdf.data.FeatureData;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.structure.paginate.PaginatedSection;
import tech.grasshopper.pdf.structure.paginate.PaginationData;
import tech.grasshopper.pdf.structure.paginate.SummaryPaginator;

@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class SummarySection extends PaginatedSection {

	static final String SECTION_TITLE = "SUMMARY";

	private final int maxDataCountPerPage = reportConfig.getSummaryConfig().dataCount();

	private FeatureData featureData;

	@Override
	public void generateDisplay(int fromIndex, int toIndex) {
		SummaryDisplay.builder().displayData(createDisplayData(fromIndex, toIndex)).document(document)
				.reportConfig(reportConfig).destinations(destinations).paginationData(PaginationData.builder()
						.itemsPerPage(maxDataCountPerPage).itemFromIndex(fromIndex).itemToIndex(toIndex).build())
				.build().display();
	}

	@Override
	public DisplayData createDisplayData(int fromIndex, int toIndex) {
		List<Feature> pageFeatures = featureData.getFeatures().subList(fromIndex, toIndex);
		return FeatureData.builder().features(pageFeatures).build();
	}

	@Override
	public void createSection() {
		featureData = (FeatureData) displayData;
		SummaryPaginator paginator = SummaryPaginator.builder().data(featureData)
				.maxDataCountPerPage(maxDataCountPerPage).section(this).build();
		paginator.paginate();
	}
}
