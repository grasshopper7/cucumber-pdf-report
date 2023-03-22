package tech.grasshopper.pdf.section.attributes;

import java.util.function.Consumer;

import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.data.AttributeData;
import tech.grasshopper.pdf.data.DisplayData;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.structure.paginate.AttributePaginator;
import tech.grasshopper.pdf.structure.paginate.PaginatedSection;
import tech.grasshopper.pdf.structure.paginate.PaginationData;

@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public abstract class AttributeSection extends PaginatedSection {

	protected AttributeData attributeData;

	@Override
	public void generateDisplay(int fromIndex, int toIndex) {
		AttributeDisplay.builder().displayData(createDisplayData(fromIndex, toIndex)).document(document)
				.reportConfig(reportConfig).attributeType(attributeType()).destinations(destinations)
				.attributeDestinationConsumer(attributeDestinationConsumer()).paginationData(PaginationData.builder()
						.itemsPerPage(maxDataCountPerPage()).itemFromIndex(fromIndex).itemToIndex(toIndex).build())
				.build().display();
	}

	@Override
	public abstract DisplayData createDisplayData(int fromIndex, int toIndex);

	public abstract String attributeType();

	public abstract int maxDataCountPerPage();

	public abstract Consumer<Destination> attributeDestinationConsumer();

	@Override
	public void createSection() {
		attributeData = (AttributeData) displayData;
		if (attributeData.getAttributes().isEmpty())
			return;

		AttributePaginator.builder().data(attributeData).maxDataCountPerPage(maxDataCountPerPage()).section(this)
				.build().paginate();
	}
}
