package tech.grasshopper.pdf.destination;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.Getter;

@Getter
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
	public static class DestinationStore {

		private Destination dashboardDestination;
		private List<Destination> featuresDestinations = new ArrayList<>();
		private List<Destination> scenariosDestinations = new ArrayList<>();

		public void addFeatureDestination(Destination destination) {
			featuresDestinations.add(destination);
		}

		public void addScenarioDestination(Destination destination) {
			scenariosDestinations.add(destination);
		}
	}
}
