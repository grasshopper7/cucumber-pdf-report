package tech.grasshopper.pdf.destination;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class Destination {

	private String name;
	private PDPage page;
	@Default
	private int xCoord = 0;
	@Default
	private int yCoord = 0;

	public PDPageXYZDestination createPDPageDestination() {
		PDPageXYZDestination destination = new PDPageXYZDestination();
		destination.setPage(page);
		destination.setLeft(xCoord);
		destination.setTop(yCoord);
		return destination;
	}

	@Data
	public static class ChapterDestinationStore {

		private Destination summaryChapterDestination;
		private List<Destination> featureChapterDestinations = new ArrayList<>();
		private List<Destination> scenarioChapterDestinations = new ArrayList<>();

		public void addFeatureChapterDestinations(Destination destination) {
			featureChapterDestinations.add(destination);
		}

		public void addScenarioChapterDestinations(Destination destination) {
			scenarioChapterDestinations.add(destination);
		}
	}
}
