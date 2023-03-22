package tech.grasshopper.pdf.section.attributes;

import java.util.List;
import java.util.function.Consumer;

import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.data.AttributeData.AuthorData;
import tech.grasshopper.pdf.data.DisplayData;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.pojo.cucumber.Author;

@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class AuthorSection extends AttributeSection {

	@Override
	public DisplayData createDisplayData(int fromIndex, int toIndex) {
		List<Author> pageAuthors = ((AuthorData) attributeData).getAuthors().subList(fromIndex, toIndex);
		return AuthorData.builder().authors(pageAuthors).build();
	}

	@Override
	public int maxDataCountPerPage() {
		return reportConfig.getAuthorConfig().dataCount();
	}

	@Override
	public String attributeType() {
		return "AUTHOR";
	}

	@Override
	public Consumer<Destination> attributeDestinationConsumer() {
		return destinations::addAuthorDestination;
	}
}
