package tech.grasshopper.pdf.annotation;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;

import lombok.Builder;
import lombok.Data;
import tech.grasshopper.pdf.data.ReportData;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;

@Data
@Builder
public class Annotation {

	private String title;
	private int xBottom;
	private int yBottom;
	private int width;
	private int height;

	public PDAnnotationLink createPDAnnotationLink() {

		PDRectangle position = new PDRectangle(xBottom, yBottom, width, height);
		PDAnnotationLink link = new PDAnnotationLink();

		PDBorderStyleDictionary borderULine = new PDBorderStyleDictionary();
		borderULine.setStyle(PDBorderStyleDictionary.STYLE_UNDERLINE);
		borderULine.setWidth(1f);
		link.setBorderStyle(borderULine);

		link.setRectangle(position);
		return link;
	}

	public static void updateDestination(ReportData reportData) {

		for (Feature feature : reportData.getFeatures()) {
			feature.getAnnotations().forEach(a -> {
				PDActionGoTo action = new PDActionGoTo();
				action.setDestination(feature.getDestination());
				a.setAction(action);
			});

			for (Scenario scenario : feature.getScenarios()) {
				scenario.getAnnotations().forEach(a -> {
					PDActionGoTo action = new PDActionGoTo();
					action.setDestination(scenario.getDestination());
					a.setAction(action);
				});
			}
		}
	}
}
