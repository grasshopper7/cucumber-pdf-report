package tech.grasshopper.pdf.chapter.summary;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.chapter.Chapter;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class SummaryChapter extends Chapter {

	public void createChapter() {
		SummaryPage.builder().displayData(displayData).reportConfig(reportConfig).document(document)
				.destinations(destinations).build().createPage();
	}
}
