package tech.grasshopper.pdf.structure.paginate;

import static tech.grasshopper.pdf.section.attributes.AttributeDisplay.TABLE_SPACE;
import static tech.grasshopper.pdf.section.attributes.AttributeDisplay.headerRowHeight;
import static tech.grasshopper.pdf.section.attributes.AttributeDisplay.attributeNameTextOptimizer;
import static tech.grasshopper.pdf.section.attributes.AttributeDisplay.attributeNameTextUtil;

import java.util.List;

import lombok.Builder;
import tech.grasshopper.pdf.data.AttributeData;
import tech.grasshopper.pdf.pojo.cucumber.Attribute;
import tech.grasshopper.pdf.util.TextUtil;

@Builder
public class AttributePaginator {

	private AttributeData data;
	private PaginatedSection section;
	private int maxDataCountPerPage;

	public void paginate() {

		float currentHeight = headerRowHeight();

		int fromIndex = 0;
		int toIndex = 0;

		TextUtil textUtil = attributeNameTextUtil();

		List<? extends Attribute> attributes = data.getAttributes();

		for (int i = 0; i < attributes.size(); i++) {

			textUtil.setText(attributeNameTextOptimizer.optimizeTextLines(attributes.get(i).getName()));

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
