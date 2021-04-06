package tech.grasshopper.pdf.section.details.executable;

import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_FONT;

import java.awt.Color;

import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

import lombok.Builder;
import lombok.Setter;
import tech.grasshopper.pdf.optimizer.TextSanitizer;
import tech.grasshopper.pdf.pojo.cucumber.Step;

@Builder
public class DocStringDisplay {

	@Setter
	private Step step;

	@Setter
	private Color textColor;

	@Setter
	private Color backgroundColor;

	private final int fontsize = 9;

	public AbstractCell display() {

		ParagraphBuilder paragraphBuilder = Paragraph.builder();
		TextSanitizer sanitizer = TextSanitizer.builder().build();

		String sanitizedText = sanitizer.sanitizeText(step.getDocString());
		String message = sanitizer.getStripMessage();

		paragraphBuilder.append(StyledText.builder().text(sanitizedText).font(STEP_HOOK_TEXT_FONT)
				.fontSize((float) fontsize).color(textColor).build());

		if (!message.isEmpty())
			paragraphBuilder.appendNewLine().append(StyledText.builder().text(message).font(STEP_HOOK_TEXT_FONT)
					.fontSize((float) fontsize).color(textColor).build());

		return ParagraphCell.builder().paragraph(paragraphBuilder.build()).lineSpacing(1.2f).borderColor(Color.GRAY)
				.borderWidth(1).backgroundColor(backgroundColor).build();
	}
}
