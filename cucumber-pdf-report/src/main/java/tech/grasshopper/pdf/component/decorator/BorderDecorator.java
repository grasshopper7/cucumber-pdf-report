package tech.grasshopper.pdf.component.decorator;

import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.component.Component;
import tech.grasshopper.pdf.component.ComponentDecorator;
import tech.grasshopper.pdf.exception.PdfReportException;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class BorderDecorator extends ComponentDecorator {

	private static final Logger logger = Logger.getLogger(BorderDecorator.class.getName());

	private Component component;

	private PDPageContentStream content;
	@Default
	private Color borderColor = Color.DARK_GRAY;
	private float xContainerBottomLeft;
	private float yContainerBottomLeft;
	private float containerWidth;
	private float containerHeight;
	@Default
	private int borderwidth = 1;

	@Override
	public void display() {
		try {
			content.setStrokingColor(borderColor);
			content.addRect(xContainerBottomLeft - borderwidth, yContainerBottomLeft - borderwidth, containerWidth + (2 * borderwidth),
					containerHeight + (2 * borderwidth));
			content.stroke();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occurred", e);
			throw new PdfReportException(e);
		}

		component.display();
	}
}
