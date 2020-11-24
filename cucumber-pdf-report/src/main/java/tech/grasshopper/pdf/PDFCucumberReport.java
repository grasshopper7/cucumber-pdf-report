package tech.grasshopper.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PageMode;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import tech.grasshopper.pdf.annotation.Annotation;
import tech.grasshopper.pdf.bookmark.Bookmark;
import tech.grasshopper.pdf.chapter.Chapter;
import tech.grasshopper.pdf.chapter.detailed.DetailedChapter;
import tech.grasshopper.pdf.chapter.feature.FeatureChapter;
import tech.grasshopper.pdf.chapter.scenario.ScenarioChapter;
import tech.grasshopper.pdf.chapter.summary.SummaryChapter;
import tech.grasshopper.pdf.config.ReportConfig;
import tech.grasshopper.pdf.data.ReportData;
import tech.grasshopper.pdf.destination.Destination.ChapterDestinationStore;
import tech.grasshopper.pdf.exception.PdfReportException;
import tech.grasshopper.pdf.font.ReportFont;

public class PDFCucumberReport {

	private static final Logger logger = Logger.getLogger(PDFCucumberReport.class.getName());

	private ReportData reportData;
	private File reportFile;
	private PDDocument document;
	private ChapterDestinationStore destinations;
	private ReportConfig reportConfig;

	private List<Chapter> chapters = new ArrayList<>();
	
	public PDFCucumberReport(ReportData reportData, String reportDirectory) {
		this(reportData, new File(reportDirectory + "/report.pdf"));
	}
	
	public PDFCucumberReport(ReportData reportData, File reportFile) {
		this.reportData = reportData;
		this.reportFile = reportFile;
		this.document = new PDDocument();
		this.destinations = new ChapterDestinationStore();
		
		ReportFont.loadReportFontFamily(document);
		createReportDirectory(this.reportFile.getParent());
		collectReportConfiguration();
		reportData.populateChapterData();
		collectChapters();
	}

	private void createReportDirectory(String reportDirectory) {
		File dir = new File(reportDirectory);
		if(!dir.exists())
			dir.mkdirs();			
	}
	
	private void collectReportConfiguration() {

		Yaml yaml = new Yaml(new Constructor(ReportConfig.class));
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream("src/test/resources/pdf-config.yaml");
			reportConfig = yaml.load(inputStream);
		} catch (FileNotFoundException e) {
			logger.log(Level.INFO, "PDF report configuration not found. Using default settings.");
			reportConfig = new ReportConfig();
		}
	}

	private void collectChapters() {

		chapters.add(SummaryChapter.builder().displayData(reportData.getSummaryData()).reportConfig(reportConfig)
				.document(document).destinations(destinations).build());

		if (reportConfig.isDisplayFeature())
			chapters.add(FeatureChapter.builder().displayData(reportData.getFeatureData()).reportConfig(reportConfig)
					.document(document).destinations(destinations).build());

		if (reportConfig.isDisplayScenario())
			chapters.add(ScenarioChapter.builder().displayData(reportData.getScenarioData()).reportConfig(reportConfig)
					.document(document).destinations(destinations).build());

		if (reportConfig.isDisplayDetailed())
			chapters.add(DetailedChapter.builder().displayData(reportData.getFeatureData()).reportConfig(reportConfig)
					.document(document).destinations(destinations).build());
	}

	public void createReport() {

		chapters.forEach(c -> c.createChapter());

		Bookmark bookmark = Bookmark.builder().reportConfig(reportConfig).build();
		PDDocumentOutline outline = bookmark.createDocumentOutline(destinations, reportData);

		Annotation.updateDestination(reportData);

		document.getDocumentCatalog().setDocumentOutline(outline);
		document.getDocumentCatalog().setPageMode(PageMode.USE_OUTLINES);

		try {
			document.save(reportFile);
			document.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occurred", e);
			throw new PdfReportException(e);
		}
	}
}
