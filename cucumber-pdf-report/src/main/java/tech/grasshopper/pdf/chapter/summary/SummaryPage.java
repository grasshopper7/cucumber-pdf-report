package tech.grasshopper.pdf.chapter.summary;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.chapter.page.Page;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.destination.DestinationAware;
import tech.grasshopper.pdf.exception.PdfReportException;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class SummaryPage extends Page implements DestinationAware {

	private static final Logger logger = Logger.getLogger(SummaryPage.class.getName());
	private PDPage page;

	@Override
	public void createPage() {
		try {
			page = new PDPage(PDRectangle.A4);
			document.addPage(page);
			content = new PDPageContentStream(document, page);

			createHeader();
			createStatistics();
			createChartTitle();
			createDonutCharts();
			createChartData();
			createDialCharts();
			createDestination();

			content.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occurred", e);
			throw new PdfReportException(e);
		}
	}

	private void createHeader() {
		SummaryHeader.builder().content(content).displayData(displayData).reportConfig(reportConfig).build().display();
	}

	private void createStatistics() {
		SummaryStatistics.builder().content(content).displayData(displayData).reportConfig(reportConfig).build()
				.display();
	}

	private void createChartTitle() {
		SummaryChartTitles.builder().content(content).reportConfig(reportConfig).build().display();
	}

	private void createDonutCharts() {
		SummaryDonutCharts.builder().document(document).content(content).displayData(displayData)
				.reportConfig(reportConfig).build().display();
	}

	private void createChartData() {
		SummaryChartData.builder().content(content).displayData(displayData).reportConfig(reportConfig).build()
				.display();
	}

	private void createDialCharts() {
		SummaryDialCharts.builder().document(document).content(content).displayData(displayData)
				.reportConfig(reportConfig).build().display();
	}

	@Override
	public Destination createDestination() {
		Destination destination = Destination.builder().name("SUMMARY").yCoord(800).page(page).build();
		destinations.setSummaryChapterDestination(destination);
		return destination;
	}
}
