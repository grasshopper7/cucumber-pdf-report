package tech.grasshopper.pdf.structure;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.config.ReportConfig;
import tech.grasshopper.pdf.data.DisplayData;
import tech.grasshopper.pdf.destination.Destination.DestinationStore;

@Data
@SuperBuilder
public abstract class Section {

	@NonNull
	protected PDDocument document;

	@NonNull
	protected DisplayData displayData;

	protected ReportConfig reportConfig;

	protected DestinationStore destinations;

	// protected AnnotationStore annotations;

	// protected ExtentPDFReporterConfig config;

	// @NonNull
	// protected Report report;

	// protected PageHeader pageHeader;

	public abstract void createSection();

	protected PDPage createPage() {
		return PageCreator.createLandscapePageAndAddToDocument(document);
	}

	/*
	 * public void pageHeaderDetails() {
	 * pageHeader.addSectionPageData(getSectionTitle(),
	 * document.getNumberOfPages()); }
	 */

}
