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
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;

public class Outline {

	public static final String DASHBOARD_SECTION_TEXT = "DASHBOARD";
	public static final String FEATURES_SECTION_TEXT = "FEATURES";
	public static final String SCENARIOS_SECTION_TEXT = "SCENARIOS";
	public static final String DETAILED_SECTION_TEXT = "DETAILS";

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
