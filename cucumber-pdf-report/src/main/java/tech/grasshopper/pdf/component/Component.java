package tech.grasshopper.pdf.component;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.config.ReportConfig;
import tech.grasshopper.pdf.data.DisplayData;

@Data
@SuperBuilder
public abstract class Component {

	protected PDPageContentStream content;
	
	protected DisplayData displayData;
	
	protected ReportConfig reportConfig;
		
	protected PDPage page;

	public abstract void display();
}
