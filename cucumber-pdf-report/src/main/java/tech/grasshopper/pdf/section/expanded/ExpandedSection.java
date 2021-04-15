package tech.grasshopper.pdf.section.expanded;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.data.ExecutableData;
import tech.grasshopper.pdf.pojo.cucumber.Executable;
import tech.grasshopper.pdf.structure.Display;
import tech.grasshopper.pdf.structure.PageCreator;
import tech.grasshopper.pdf.structure.Section;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ExpandedSection extends Section {

	static final String SECTION_TITLE = "EXPANDED SECTION";

	@Getter
	private ExecutableData detailedData;

	@Override
	public void createSection() {

		detailedData = (ExecutableData) displayData;

		boolean initialPageAdded = false;
		float ylocation = Display.CONTENT_START_Y;

		for (Executable executable : detailedData.getExecutables()) {

			if (!executable.getMedia().isEmpty()) {

				if (!initialPageAdded) {
					PageCreator.builder().document(document).build()
							.createLandscapePageWithHeaderAndNumberAndAddToDocument(SECTION_TITLE);
					initialPageAdded = true;
				}

				ExpandedMediaDisplay expandedMediaDisplay = ExpandedMediaDisplay.builder().executable(executable)
						.ylocation(ylocation).document(document).reportConfig(reportConfig).build();
				expandedMediaDisplay.display();

				ylocation = expandedMediaDisplay.getFinalY();
			}
		}
	}
}
