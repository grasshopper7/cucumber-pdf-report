package tech.grasshopper.pdf.bookmark;

import java.util.List;

import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;

import lombok.Builder;
import tech.grasshopper.pdf.config.ReportConfig;
import tech.grasshopper.pdf.data.ReportData;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.destination.Destination.ChapterDestinationStore;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;

@Builder
public class Bookmark {

	private ReportConfig reportConfig;

	private static String REPORT_CHAPTERS_BOOKMARK_TEXT = "Report Chapters";
	private static String DETAILED_CHAPTER_BOOKMARK_TEXT = "Detailed Chapter";

	public PDDocumentOutline createDocumentOutline(ChapterDestinationStore destinations, ReportData reportData) {
		PDDocumentOutline outline = new PDDocumentOutline();

		PDOutlineItem chaptersOutline = new PDOutlineItem();
		chaptersOutline.setTitle(REPORT_CHAPTERS_BOOKMARK_TEXT);
		chaptersOutline.setBold(true);
		outline.addLast(chaptersOutline);

		chaptersOutline.addLast(createOutlineItem(destinations.getSummaryChapterDestination(), "Summary"));

		if (reportConfig.isDisplayFeature()) {
			PDOutlineItem featureChapterBookmark = createChapterOutlineItems(
					destinations.getFeatureChapterDestinations(), "Features");
			chaptersOutline.addLast(featureChapterBookmark);
		}

		if (reportConfig.isDisplayScenario()) {
			PDOutlineItem scenarioChapterBookmark = createChapterOutlineItems(
					destinations.getScenarioChapterDestinations(), "Scenarios");
			chaptersOutline.addLast(scenarioChapterBookmark);

			chaptersOutline.openNode();
		}

		if (reportConfig.isDisplayDetailed()) {
			PDOutlineItem detailedPagesOutline = new PDOutlineItem();
			detailedPagesOutline.setTitle(DETAILED_CHAPTER_BOOKMARK_TEXT);
			detailedPagesOutline.setBold(true);

			for (Feature feature : reportData.getFeatures()) {
				PDOutlineItem featureBookmark = createOutlineItem(feature.getDestination(),
						TextLengthOptimizer.optimizeOutlineText("F " + feature.getName()));
				detailedPagesOutline.addLast(featureBookmark);

				for (Scenario scenario : feature.getScenarios()) {
					PDOutlineItem scenaroBookmark = createOutlineItem(scenario.getDestination(),
							TextLengthOptimizer.optimizeOutlineText("S " + scenario.getName()));
					detailedPagesOutline.addLast(scenaroBookmark);
				}
			}
			outline.addLast(detailedPagesOutline);
		}
		return outline;
	}

	private PDOutlineItem createOutlineItem(Destination destination, String title) {
		return createOutlineItem(destination.createPDPageDestination(), title);
	}

	private PDOutlineItem createOutlineItem(Destination destination) {
		return createOutlineItem(destination, destination.getName());
	}

	private PDOutlineItem createOutlineItem(PDDestination destination, String title) {
		PDOutlineItem bookmark = new PDOutlineItem();
		bookmark.setDestination(destination);
		bookmark.setTitle(title);
		return bookmark;
	}

	private PDOutlineItem createChapterOutlineItems(List<Destination> destinations, String title) {
		PDOutlineItem chapterBookmark = createOutlineItem(destinations.get(0), title);
		destinations.forEach(d -> {
			PDOutlineItem pagesBookmark = createOutlineItem(d);
			chapterBookmark.addLast(pagesBookmark);
		});
		return chapterBookmark;
	}
}
