package tech.grasshopper.pdf.annotation;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import tech.grasshopper.pdf.data.ReportData;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.pojo.cucumber.Executable;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;

@Setter
@Builder
public class Annotation {

	@SuppressWarnings("unused")
	private String title;
	private float xBottom;
	private float yBottom;
	private float width;
	private float height;
	@Getter
	private PDPage page;

	public PDAnnotationLink createPDAnnotationLink() {

		PDRectangle position = new PDRectangle(xBottom, yBottom, width, height);
		PDAnnotationLink link = new PDAnnotationLink();

		PDBorderStyleDictionary borderULine = new PDBorderStyleDictionary();
		link.setBorderStyle(borderULine);

		link.setRectangle(position);
		return link;
	}

	@SneakyThrows
	public static void updateDestination(ReportData reportData) {

		for (Feature feature : reportData.getFeatures()) {
			feature.getAnnotations().forEach(a -> {
				updateDestination(a, feature.getDestination());
			});

			for (Scenario scenario : feature.getScenarios()) {
				scenario.getAnnotations().forEach(a -> {
					updateDestination(a, scenario.getDestination());
				});

				for (Executable executable : scenario.getStepsAndHooks()) {
					for (int i = 0; i < executable.getMediaAnnotations().size(); i++) {
						updateDestination(executable.getMediaAnnotations().get(i),
								executable.getMediaDestinations().get(i));
					}
				}
			}
		}
	}

	@SneakyThrows
	private static void updateDestination(Annotation annotation, Destination destination) {

		PDActionGoTo action = new PDActionGoTo();
		action.setDestination(destination.createPDPageDestination());
		PDAnnotationLink link = annotation.createPDAnnotationLink();
		link.setAction(action);

		annotation.getPage().getAnnotations().add(link);
	}
}
