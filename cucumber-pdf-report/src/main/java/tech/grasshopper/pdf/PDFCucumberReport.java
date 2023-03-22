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

import tech.grasshopper.pdf.annotation.AnnotationProcessor;
import tech.grasshopper.pdf.annotation.FileAnnotationProcessor;
import tech.grasshopper.pdf.config.ParameterConfig;
import tech.grasshopper.pdf.config.ReportConfig;
import tech.grasshopper.pdf.data.ReportData;
import tech.grasshopper.pdf.destination.Destination.DestinationStore;
import tech.grasshopper.pdf.exception.PdfReportException;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.outline.Outline;
import tech.grasshopper.pdf.section.attributes.AuthorSection;
import tech.grasshopper.pdf.section.attributes.DeviceSection;
import tech.grasshopper.pdf.section.attributes.TagSection;
import tech.grasshopper.pdf.section.dashboard.Dashboard;
import tech.grasshopper.pdf.section.details.DetailedSection;
import tech.grasshopper.pdf.section.details.executable.MediaCleanup;
import tech.grasshopper.pdf.section.details.executable.MediaCleanup.CleanupType;
import tech.grasshopper.pdf.section.details.executable.MediaCleanup.MediaCleanupOption;
import tech.grasshopper.pdf.section.expanded.ExpandedSection;
import tech.grasshopper.pdf.section.feature.FeatureSection;
import tech.grasshopper.pdf.section.scenario.ScenarioSection;
import tech.grasshopper.pdf.section.summary.SummarySection;

public class PDFCucumberReport {

	private static final Logger logger = Logger.getLogger(PDFCucumberReport.class.getName());

	protected ReportData reportData;
	protected File reportFile;
	protected PDDocument document;
	private DestinationStore destinations;
	protected ReportConfig reportConfig;
	private MediaCleanupOption mediaCleanupOption;

	public PDFCucumberReport(ReportData reportData, String reportDirectory) {
		this(reportData, reportDirectory, MediaCleanupOption.builder().cleanUpType(CleanupType.NONE).build());
	}

	public PDFCucumberReport(ReportData reportData, File reportFile) {
		this(reportData, reportFile, MediaCleanupOption.builder().cleanUpType(CleanupType.NONE).build());
	}

	public PDFCucumberReport(ReportData reportData, String reportDirectory, MediaCleanupOption mediaCleanupOption) {
		this(reportData, new File(reportDirectory + "/report.pdf"), mediaCleanupOption);
	}

	public PDFCucumberReport(ReportData reportData, File reportFile, MediaCleanupOption mediaCleanupOption) {
		this.reportData = reportData;
		this.reportFile = reportFile;
		this.document = new PDDocument();
		this.destinations = new DestinationStore();
		this.mediaCleanupOption = mediaCleanupOption;

		ReportFont.loadReportFontFamily(document);
		createReportDirectory(this.reportFile.getParent());
		collectReportConfiguration();

		reportData.setReportConfig(reportConfig);
		reportData.populateSectionData();
		reportData.checkData();
	}

	public void setParameterConfig(ParameterConfig parameterConfig) {
		reportConfig.mergeParameterConfig(parameterConfig);
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

		verifyReportConfig();

		createDashboardSection();

		createSummarySection();

		createTagSection();

		createDeviceSection();

		createAuthorSection();

		createFeatureSection();

		createScenarioSection();

		createDetailedSection();

		createExpandedSection();

		processDestinationAnnotations();

		processFileAnnotations();

		Outline.createDocumentOutline(document, reportConfig, destinations, reportData);

		MediaCleanup.builder().executableData(reportData.getExecutableData()).mediaCleanupOption(mediaCleanupOption)
				.build().deleteMedia();

		try {
			document.save(reportFile);
			document.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occurred", e);
			throw new PdfReportException(e);
		}
	}

	protected void verifyReportConfig() {
		reportConfig.verifyAndUpdate();
	}

	protected void createDashboardSection() {
		Dashboard.builder().displayData(reportData.getSummaryData()).reportConfig(reportConfig).document(document)
				.destinations(destinations).build().createSection();
	}

	protected void createSummarySection() {
		SummarySection.builder().displayData(reportData.getFeatureData()).reportConfig(reportConfig).document(document)
				.destinations(destinations).build().createSection();
	}

	protected void createTagSection() {
		if (reportConfig.isDisplayTag() && !reportData.getTagData().getTags().isEmpty())
			TagSection.builder().displayData(reportData.getTagData()).reportConfig(reportConfig).document(document)
					.destinations(destinations).build().createSection();
	}

	protected void createDeviceSection() {
		if (reportConfig.isDisplayDevice() && !reportData.getDeviceData().getDevices().isEmpty())
			DeviceSection.builder().displayData(reportData.getDeviceData()).reportConfig(reportConfig)
					.document(document).destinations(destinations).build().createSection();
	}

	protected void createAuthorSection() {
		if (reportConfig.isDisplayAuthor() && !reportData.getAuthorData().getAuthors().isEmpty())
			AuthorSection.builder().displayData(reportData.getAuthorData()).reportConfig(reportConfig)
					.document(document).destinations(destinations).build().createSection();
	}

	protected void createFeatureSection() {
		if (reportConfig.isDisplayFeature())
			FeatureSection.builder().displayData(reportData.getFeatureData()).reportConfig(reportConfig)
					.document(document).destinations(destinations).build().createSection();
	}

	protected void createScenarioSection() {
		if (reportConfig.isDisplayScenario())
			ScenarioSection.builder().displayData(reportData.getScenarioData()).reportConfig(reportConfig)
					.document(document).destinations(destinations).build().createSection();
	}

	protected void createDetailedSection() {
		if (reportConfig.isDisplayDetailed())
			DetailedSection.builder().displayData(reportData.getFeatureData()).reportConfig(reportConfig)
					.document(document).build().createSection();
	}

	protected void createExpandedSection() {
		if (reportConfig.isDisplayExpanded())
			ExpandedSection.builder().displayData(reportData.getExecutableData()).reportConfig(reportConfig)
					.document(document).build().createSection();
	}

	protected void processDestinationAnnotations() {
		AnnotationProcessor.builder().reportData(reportData).reportConfig(reportConfig).build().updateDestination();
	}

	protected void processFileAnnotations() {
		FileAnnotationProcessor.builder().reportData(reportData).document(document).build().updateAttachments();
	}
}
