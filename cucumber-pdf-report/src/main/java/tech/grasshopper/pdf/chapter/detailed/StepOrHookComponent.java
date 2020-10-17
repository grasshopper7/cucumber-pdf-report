package tech.grasshopper.pdf.chapter.detailed;

import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;

public interface StepOrHookComponent {

	int componentHeight();
	
	void componentText(ParagraphBuilder paragraphBuilder);
}
