package tech.grasshopper.pdf.outline;

import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PageMode;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;

import tech.grasshopper.pdf.config.ReportConfig;
import tech.grasshopper.pdf.data.ReportData;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.destination.Destination.DestinationStore;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.pojo.cucumber.Executable;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;

public class Outline {

	public static final String DASHBOARD_SECTION_TEXT = "DASHBOARD";
	public static final String FEATURES_SECTION_TEXT = "FEATURES";
	public static final String SCENARIOS_SECTION_TEXT = "SCENARIOS";
	public static final String DETAILED_SECTION_TEXT = "DETAILS";
	public static final String EXPANDED_SECTION_TEXT = "EXPANDED";

	public static void createDocumentOutline(PDDocument document, ReportConfig reportConfig,
			DestinationStore destinations, ReportData reportData) {

		PDDocumentOutline outline = new PDDocumentOutline();
		outline.addLast(createOutlineItem(destinations.getDashboardDestination()));

		if (reportConfig.isDisplayFeature()) {
			PDOutlineItem featureOutlineitem = createChapterOutlineItems(destinations.getFeaturesDestinations(),
					Outline.FEATURES_SECTION_TEXT);
			outline.addLast(featureOutlineitem);
		}

		if (reportConfig.isDisplayScenario()) {
			PDOutlineItem scenarioOutlineItem = createChapterOutlineItems(destinations.getScenariosDestinations(),
					Outline.SCENARIOS_SECTION_TEXT);
			outline.addLast(scenarioOutlineItem);
		}

		if (reportConfig.isDisplayDetailed()) {
			PDOutlineItem detailedOutlineItem = new PDOutlineItem();
			detailedOutlineItem.setTitle(Outline.DETAILED_SECTION_TEXT);

			for (Feature feature : reportData.getFeatures()) {
				PDOutlineItem featureOutline = createOutlineItem(feature.getDestination(),
						TextLengthOptimizer.optimizeOutlineText(feature.getName()));
				detailedOutlineItem.addLast(featureOutline);

				for (Scenario scenario : feature.getScenarios()) {
					PDOutlineItem scenarioOutline = createOutlineItem(scenario.getDestination(),
							TextLengthOptimizer.optimizeOutlineText(scenario.getName()));
					featureOutline.addLast(scenarioOutline);
				}
			}
			outline.addLast(detailedOutlineItem);
		}

		if (reportConfig.isDisplayExpanded()) {
			PDOutlineItem expandedOutlineItem = new PDOutlineItem();
			expandedOutlineItem.setTitle(Outline.EXPANDED_SECTION_TEXT);
			boolean anyExecutableHasMedia = false;

			for (Feature feature : reportData.getFeatures()) {
				PDOutlineItem featureOutline = createOutlineItem(feature.getDestination(),
						TextLengthOptimizer.optimizeOutlineText(feature.getName()));
				boolean executableHasMedia = false;

				for (Scenario scenario : feature.getScenarios()) {
					PDOutlineItem scenarioOutline = createOutlineItem(scenario.getDestination(),
							TextLengthOptimizer.optimizeOutlineText(scenario.getName()));
					featureOutline.addLast(scenarioOutline);

					for (Executable executable : scenario.getStepsAndHooks()) {
						if (!executable.getMedia().isEmpty()) {
							executableHasMedia = true;
							if (!anyExecutableHasMedia)
								anyExecutableHasMedia = true;

							String name = executable.getDisplay().executableName();

							for (int i = 0; i < executable.getMedia().size(); i++) {
								PDOutlineItem executableOutline = null;
								if (executable.getMedia().size() == 1) {
									executableOutline = createOutlineItem(executable.getMediaDestinations().get(0),
											TextLengthOptimizer.optimizeOutlineText(name));
								} else {
									executableOutline = createOutlineItem(executable.getMediaDestinations().get(i),
											TextLengthOptimizer.optimizeOutlineText(name) + " - Media " + (i + 1));
								}
								scenarioOutline.addLast(executableOutline);
							}
						}
					}
				}
				if (executableHasMedia)
					expandedOutlineItem.addLast(featureOutline);
			}
			if (anyExecutableHasMedia)
				outline.addLast(expandedOutlineItem);
		}

		document.getDocumentCatalog().setDocumentOutline(outline);
		document.getDocumentCatalog().setPageMode(PageMode.USE_OUTLINES);
	}

	private static PDOutlineItem createOutlineItem(Destination destination, String title) {
		return createOutlineItem(destination.createPDPageDestination(), title);
	}

	private static PDOutlineItem createOutlineItem(Destination destination) {
		return createOutlineItem(destination, destination.getName());
	}

	private static PDOutlineItem createOutlineItem(PDDestination destination, String title) {
		PDOutlineItem outlineItem = new PDOutlineItem();
		outlineItem.setDestination(destination);
		outlineItem.setTitle(title);
		return outlineItem;
	}

	private static PDOutlineItem createChapterOutlineItems(List<Destination> destinations, String title) {
		PDOutlineItem outlineItem = createOutlineItem(destinations.get(0), title);
		destinations.forEach(d -> {
			PDOutlineItem pagesOutline = createOutlineItem(d);
			outlineItem.addLast(pagesOutline);
		});
		return outlineItem;
	}
}
