package tech.grasshopper.pdf.section.tag;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.data.DisplayData;
import tech.grasshopper.pdf.data.TagData;
import tech.grasshopper.pdf.pojo.cucumber.Tag;
import tech.grasshopper.pdf.structure.paginate.PaginatedSection;
import tech.grasshopper.pdf.structure.paginate.PaginationData;
import tech.grasshopper.pdf.structure.paginate.TagPaginator;

@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class TagSection extends PaginatedSection {

	static final String SECTION_TITLE = "TAGS SUMMARY";

	private final int maxDataCountPerPage = reportConfig.getTagConfig().dataCount();

	private TagData tagData;

	@Override
	public void generateDisplay(int fromIndex, int toIndex) {
		TagDisplay.builder().displayData(createDisplayData(fromIndex, toIndex)).document(document)
				.reportConfig(reportConfig).destinations(destinations).paginationData(PaginationData.builder()
						.itemsPerPage(maxDataCountPerPage).itemFromIndex(fromIndex).itemToIndex(toIndex).build())
				.build().display();
	}

	@Override
	public DisplayData createDisplayData(int fromIndex, int toIndex) {
		List<Tag> pageTags = tagData.getTags().subList(fromIndex, toIndex);
		return TagData.builder().tags(pageTags).build();
	}

	@Override
	public void createSection() {
		tagData = (TagData) displayData;
		TagPaginator paginator = TagPaginator.builder().data(tagData).maxDataCountPerPage(maxDataCountPerPage)
				.section(this).build();
		paginator.paginate();
	}
}
