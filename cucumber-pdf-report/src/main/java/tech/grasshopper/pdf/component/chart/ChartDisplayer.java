package tech.grasshopper.pdf.component.chart;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.internal.chartpart.Chart;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.exception.PdfReportException;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ChartDisplayer {
	
	private static final Logger logger = Logger.getLogger(ChartDisplayer.class.getName());

	private PDPageContentStream content;
	private PDDocument document;
	private Chart<?,?> chart;
	private float xBottomLeft;
	private float yBottomLeft;

	public void display() {
		try {
			BufferedImage chartImage = BitmapEncoder.getBufferedImage(chart);
			PDImageXObject pdfChartImage = JPEGFactory.createFromImage(document, chartImage, 1);
			content.drawImage(pdfChartImage, xBottomLeft, yBottomLeft);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occurred", e);
			throw new PdfReportException(e);
		}
	}
}
