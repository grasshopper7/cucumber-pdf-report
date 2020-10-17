package tech.grasshopper.pdf.chapter.scenario;

import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.chapter.page.PaginatedPage;
import tech.grasshopper.pdf.component.text.TextComponent;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.destination.DestinationAware;
import tech.grasshopper.pdf.exception.PdfReportException;
import tech.grasshopper.pdf.pojo.report.Text;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ScenarioPage extends PaginatedPage implements DestinationAware {

	private static final Logger logger = Logger.getLogger(ScenarioPage.class.getName());
	private PDPage page;

	@Override
	public void createPage() {
		try {
			page = new PDPage(PDRectangle.A4);
			document.addPage(page);
			content = new PDPageContentStream(document, page);

			createTitle();
			createStackedBarChart();
			createTable();
			collectPageAnnotations();
			createDestination();

			content.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occurred", e);
			throw new PdfReportException(e);
		}
	}

	private void createTitle() {
		Text text = Text.builder().textColor(Color.LIGHT_GRAY).font(PDType1Font.HELVETICA_OBLIQUE).fontSize(12)
				.xoffset(40).yoffset(775).text("SCENARIOS SUMMARY " + (paginationData.getItemFromIndex() + 1) + " - "
						+ paginationData.getItemToIndex())
				.build();
		TextComponent.builder().content(content).text(text).build().display();
	}

	private void createStackedBarChart() {
		ScenarioStackedBarChart.builder().document(document).content(content).displayData(displayData)
				.reportConfig(reportConfig).itemCount(paginationData.getItemsPerPage())
				.fromXData(paginationData.getItemFromIndex()).toXData(paginationData.getItemToIndex()).build()
				.display();
	}

	private void createTable() {
		ScenarioStepDetails.builder().displayData(displayData).content(content).reportConfig(reportConfig)
				.paginationData(paginationData).build().display();
	}

	public void collectPageAnnotations() {
		if (reportConfig.isDisplayScenario() && reportConfig.isDisplayDetailed())
			ScenarioStepDetails.builder().displayData(displayData).content(content).page(page).build()
					.createAnnotationLinks();
	}

	@Override
	public Destination createDestination() {
		Destination destination = Destination.builder().name(
				"Scenarios - " + (paginationData.getItemFromIndex() + 1) + " to " + paginationData.getItemToIndex())
				.yCoord(800).page(page).build();
		destinations.addScenarioChapterDestinations(destination);
		return destination;
	}
}
