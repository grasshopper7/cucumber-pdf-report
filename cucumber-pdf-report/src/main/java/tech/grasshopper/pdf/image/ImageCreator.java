package tech.grasshopper.pdf.image;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.internal.chartpart.Chart;

import lombok.Builder;
import lombok.SneakyThrows;

@Builder
public class ImageCreator {

	private Chart<?, ?> chart;

	private PDDocument document;

	private PDPageContentStream content;

	@SneakyThrows
	public PDImageXObject generateChartImageXObject() {
		byte[] bytes = BitmapEncoder.getBitmapBytes(chart, BitmapFormat.PNG);
		return PDImageXObject.createFromByteArray(document, bytes, "");
	}

	@SneakyThrows
	public void generateAndDisplayChartImage(float xLocation, float yLocation) {
		content.drawImage(generateChartImageXObject(), xLocation, yLocation);
	}
}
