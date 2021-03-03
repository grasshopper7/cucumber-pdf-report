package tech.grasshopper.pdf.section.details;

import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_COLUMN_WIDTH;
import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_FONT;
import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_PADDING;

import java.awt.Color;

import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

import lombok.Builder;
import lombok.Setter;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
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

	private static final int MAX_LINES = 4;

	public AbstractCell display() {

		ParagraphBuilder paragraphBuilder = Paragraph.builder();
		final TextSanitizer sanitizer = TextSanitizer.builder().font(ReportFont.REGULAR_FONT).build();

		final String sanitizedText = sanitizer.sanitizeText(step.getDocString());
		final String stripNewLineTab = sanitizedText.replaceAll("[\\t\\n\\r ]+", " ").trim();

		final TextLengthOptimizer textOptimizer = TextLengthOptimizer.builder().font(STEP_HOOK_TEXT_FONT)
				.fontsize(fontsize).availableSpace((STEP_HOOK_TEXT_COLUMN_WIDTH) - 2 * STEP_HOOK_TEXT_PADDING)
				.maxLines(MAX_LINES).build();

		final String maxLinesMsg = "* Maximum of " + MAX_LINES + " lines are displayed.";
		String croppedMsg = "^ Tabs, spaces, new line has been trimmed to fit in the available space.";

		String text = textOptimizer.optimizeTextLines(stripNewLineTab);
		if (!textOptimizer.isTextTrimmed())
			text = text + " ^";
		else {
			text = text + "^";
			croppedMsg = maxLinesMsg + " " + croppedMsg;
		}

		paragraphBuilder.append(StyledText.builder().text(text).font(STEP_HOOK_TEXT_FONT).fontSize((float) fontsize)
				.color(textColor).build());

		paragraphBuilder.appendNewLine().append(StyledText.builder().text(croppedMsg).font(STEP_HOOK_TEXT_FONT)
				.fontSize((float) fontsize).color(textColor).build());

		return ParagraphCell.builder().paragraph(paragraphBuilder.build()).lineSpacing(1.1f)
				.backgroundColor(backgroundColor).build();
	}
}
