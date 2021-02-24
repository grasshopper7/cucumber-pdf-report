package tech.grasshopper.pdf.section.feature;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.destination.DestinationAware;
import tech.grasshopper.pdf.outline.Outline;
import tech.grasshopper.pdf.structure.PageCreator;
import tech.grasshopper.pdf.structure.paginate.PaginatedDisplay;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class FeatureDisplay extends PaginatedDisplay implements DestinationAware {

	@Override
	@SneakyThrows
	public void display() {

		page = PageCreator.builder().document(document).build()
				.createLandscapePageWithHeaderAndNumberAndAddToDocument(FeatureSection.SECTION_TITLE);

		content = new PDPageContentStream(document, page, AppendMode.APPEND, true);

		createStackedBarChart();
		createTable();
		createDestination();

		content.close();
	}

	private void createStackedBarChart() {

		FeatureStackedBarChart.builder().document(document).content(content).displayData(displayData)
				.reportConfig(reportConfig).maxFeatures(reportConfig.getFeatureConfig().getItemcount())
				.fromXData(paginationData.getItemFromIndex()).toXData(paginationData.getItemToIndex()).build()
				.display();
	}

	private void createTable() {

		FeatureScenarioDetails.builder().displayData(displayData).content(content).reportConfig(reportConfig)
				.document(document).paginationData(paginationData).build().display();
	}

	@Override
	public void createDestination() {
		Destination destination = Destination.builder()
				.name(Outline.FEATURES_SECTION_TEXT + " - " + (paginationData.getItemFromIndex() + 1) + " to "
						+ paginationData.getItemToIndex())
				.yCoord((int) page.getMediaBox().getHeight()).page(page).build();
		destinations.addFeatureDestination(destination);
	}
}
