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
public class BackgroundDecorator extends ComponentDecorator {

	private static final Logger logger = Logger.getLogger(BackgroundDecorator.class.getName());
	
	private Component component;
	
	private PDPageContentStream content;
	@Default
	private Color containerColor = Color.LIGHT_GRAY;
	private float xContainerBottomLeft;
	private float yContainerBottomLeft;
	private float containerWidth;
	private float containerHeight;
	
	@Override
	public void display() {		
		try {
			content.setNonStrokingColor(containerColor);
			content.addRect(xContainerBottomLeft, yContainerBottomLeft, containerWidth, containerHeight);
			content.fill();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occurred", e);
			throw new PdfReportException(e);
		}
		
		component.display();
	}
}
