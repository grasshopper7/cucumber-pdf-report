package tech.grasshopper.pdf.chapter.page;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.config.ReportConfig;
import tech.grasshopper.pdf.data.DisplayData;
import tech.grasshopper.pdf.destination.Destination.ChapterDestinationStore;

@Data
@SuperBuilder
public abstract class Page {
	
	protected PDDocument document;
	
	protected PDPageContentStream content;
		
	protected ChapterDestinationStore destinations;
	
	protected DisplayData displayData;
	
	protected ReportConfig reportConfig;
	
	public abstract void createPage();
}
