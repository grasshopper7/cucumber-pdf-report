package tech.grasshopper.pdf.annotation;

import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;

import lombok.Builder;
import lombok.SneakyThrows;
import tech.grasshopper.pdf.config.ReportConfig;
import tech.grasshopper.pdf.data.ReportData;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.pojo.cucumber.Executable;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;

@Builder
public class AnnotationProcessor {

	private ReportData reportData;

	private ReportConfig reportConfig;

	@SneakyThrows
	public void updateDestination() {

		for (Feature feature : reportData.getFeatures()) {
			feature.getAnnotations().forEach(a -> {
				updateDestination(a, feature.getDestination());
			});

			for (Scenario scenario : feature.getScenarios()) {
				scenario.getAnnotations().forEach(a -> {
					updateDestination(a, scenario.getDestination());
				});

				for (Executable executable : scenario.getFilteredStepsAndHooks(reportConfig)) {
					for (int i = 0; i < executable.getAnnotations().size(); i++) {
						updateDestination(executable.getAnnotations().get(i), executable.getDestinations().get(i));
					}
				}
			}
		}
	}

	@SneakyThrows
	private void updateDestination(Annotation annotation, Destination destination) {

		if (annotation == null || destination == null)
			return;

		PDActionGoTo action = new PDActionGoTo();
		action.setDestination(destination.createPDPageDestination());
		PDAnnotationLink link = annotation.createPDAnnotationLink();
		link.setAction(action);

		annotation.getPage().getAnnotations().add(link);
	}
}
