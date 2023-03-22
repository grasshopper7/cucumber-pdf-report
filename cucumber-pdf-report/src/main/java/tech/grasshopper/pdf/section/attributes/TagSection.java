package tech.grasshopper.pdf.section.attributes;

import java.util.List;
import java.util.function.Consumer;

import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.data.AttributeData.TagData;
import tech.grasshopper.pdf.data.DisplayData;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.pojo.cucumber.Tag;

@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class TagSection extends AttributeSection {

	@Override
	public DisplayData createDisplayData(int fromIndex, int toIndex) {
		List<Tag> pageTags = ((TagData) attributeData).getTags().subList(fromIndex, toIndex);
		return TagData.builder().tags(pageTags).build();
	}

	@Override
	public int maxDataCountPerPage() {
		return reportConfig.getTagConfig().dataCount();
	}

	@Override
	public String attributeType() {
		return "TAG";
	}

	@Override
	public Consumer<Destination> attributeDestinationConsumer() {
		return destinations::addTagDestination;
	}
}
