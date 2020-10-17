package tech.grasshopper.pdf.chapter.detailed;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.chapter.page.Page;
import tech.grasshopper.pdf.component.Component;
import tech.grasshopper.pdf.destination.DestinationAware;
import tech.grasshopper.pdf.exception.PdfReportException;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class DetailedPage extends Page {

	private static final Logger logger = Logger.getLogger(DetailedPage.class.getName());

	public static final int CONTENT_HEIGHT = 750;
	public static final int CONTENT_Y_START = 800;

	private List<Component> components;

	@Override
	public void createPage() {
		try {
			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);
			content = new PDPageContentStream(document, page);

			components.forEach(c -> {
				c.setContent(content);
				c.setPage(page);
				c.display();

				if (c instanceof DestinationAware)
					((DestinationAware) c).createDestination();
			});

			content.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occurred", e);
			throw new PdfReportException(e);
		}
	}
}
