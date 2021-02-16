package tech.grasshopper.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import tech.grasshopper.pdf.annotation.Annotation;
import tech.grasshopper.pdf.config.ReportConfig;
import tech.grasshopper.pdf.data.ReportData;
import tech.grasshopper.pdf.destination.Destination.DestinationStore;
import tech.grasshopper.pdf.exception.PdfReportException;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.outline.Outline;
import tech.grasshopper.pdf.section.dashboard.Dashboard;
import tech.grasshopper.pdf.section.details.DetailedSection;
import tech.grasshopper.pdf.section.feature.FeatureSection;
import tech.grasshopper.pdf.section.scenario.ScenarioSection;

public class PDFCucumberReport {

	private static final Logger logger = Logger.getLogger(PDFCucumberReport.class.getName());

	private ReportData reportData;
	private File reportFile;
	private PDDocument document;
	private DestinationStore destinations;
	private ReportConfig reportConfig;

	public PDFCucumberReport(ReportData reportData, String reportDirectory) {
		this(reportData, new File(reportDirectory + "/report.pdf"));
	}

	public PDFCucumberReport(ReportData reportData, File reportFile) {
		this.reportData = reportData;
		this.reportFile = reportFile;
		this.document = new PDDocument();
		this.destinations = new DestinationStore();

		ReportFont.loadReportFontFamily(document);
		createReportDirectory(this.reportFile.getParent());
		collectReportConfiguration();
		reportData.populateSectionData();
	}

	private void createReportDirectory(String reportDirectory) {
		File dir = new File(reportDirectory);
		if (!dir.exists())
			dir.mkdirs();
	}

	private void collectReportConfiguration() {

		Yaml yaml = new Yaml(new Constructor(ReportConfig.class));
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream("src/test/resources/pdf-config.yaml");
			reportConfig = yaml.load(inputStream);
		} catch (FileNotFoundException e) {
			logger.log(Level.INFO, "PDF report configuration YAML file not found. Using default settings.");
			reportConfig = new ReportConfig();
		}
	}

	public void createReport() {

		Dashboard.builder().displayData(reportData.getSummaryData()).reportConfig(reportConfig).document(document)
				.destinations(destinations).build().createSection();

		if (reportConfig.isDisplayFeature())
			FeatureSection.builder().displayData(reportData.getFeatureData()).reportConfig(reportConfig)
					.document(document).destinations(destinations).build().createSection();

		if (reportConfig.isDisplayScenario())
			ScenarioSection.builder().displayData(reportData.getScenarioData()).reportConfig(reportConfig)
					.document(document).destinations(destinations).build().createSection();

		if (reportConfig.isDisplayDetailed())
			DetailedSection.builder().displayData(reportData.getFeatureData()).reportConfig(reportConfig)
					.document(document).build().createSection();

		Annotation.updateDestination(reportData);

		Outline.createDocumentOutline(document, reportConfig, destinations, reportData);

		try {
			document.save(reportFile);
			document.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occurred", e);
			throw new PdfReportException(e);
		}
	}
}
