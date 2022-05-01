package tech.grasshopper.pdf.structure.paginate;

import static tech.grasshopper.pdf.section.tag.TagDisplay.TABLE_SPACE;
import static tech.grasshopper.pdf.section.tag.TagDisplay.tagNameTextOptimizer;
import static tech.grasshopper.pdf.section.tag.TagDisplay.tagNameTextUtil;
import static tech.grasshopper.pdf.section.tag.TagDisplay.headerRowHeight;

import lombok.Builder;
import tech.grasshopper.pdf.data.TagData;
import tech.grasshopper.pdf.util.TextUtil;

@Builder
public class TagPaginator {

	private TagData data;
	private PaginatedSection section;
	private int maxDataCountPerPage;

	public void paginate() {

		float currentHeight = headerRowHeight();

		int fromIndex = 0;
		int toIndex = 0;

		TextUtil textUtil = tagNameTextUtil();

		for (int i = 0; i < data.getTags().size(); i++) {

			textUtil.setText(tagNameTextOptimizer.optimizeTextLines(data.getTags().get(i).getName()));

			currentHeight = currentHeight + textUtil.tableRowHeight();

			if (currentHeight > TABLE_SPACE || (toIndex - fromIndex) + 1 > maxDataCountPerPage) {
				section.generateDisplay(fromIndex, toIndex);
				fromIndex = toIndex;
				currentHeight = headerRowHeight();
				i--;
			} else {
				toIndex++;
			}
		}

		// Remaining data
		section.generateDisplay(fromIndex, toIndex);
	}
}
