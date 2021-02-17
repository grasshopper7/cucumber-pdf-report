package tech.grasshopper.pdf.section.details;

import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_COLUMN_WIDTH;
import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_FONT;
import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_FONT_SIZE;

import java.awt.Color;
import java.util.List;

import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;
import org.vandeseer.easytable.util.PdfUtil;

import lombok.Builder;
import lombok.Data;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextSanitizer;
import tech.grasshopper.pdf.pojo.cucumber.Step;

@Data
@Builder
public class DocStringDisplay {

	private Step step;
	private Color stepTextColor;

	private static final int MAX_LINES = 5;

	public AbstractCell display() {

		ParagraphBuilder paragraphBuilder = Paragraph.builder();
		final TextSanitizer sanitizer = TextSanitizer.builder().font(ReportFont.REGULAR_FONT).build();

		String sanitizedText = sanitizer.sanitizeText(step.getDocString());
		String stripNewLineTab = sanitizedText.replaceAll("[\\t\\n\\r ]+", " ").trim();

		List<String> lines = PdfUtil.getOptimalTextBreakLines("    " + stripNewLineTab, STEP_HOOK_TEXT_FONT,
				STEP_HOOK_TEXT_FONT_SIZE, STEP_HOOK_TEXT_COLUMN_WIDTH);

		int index = lines.size() <= MAX_LINES ? lines.size() : MAX_LINES;

		for (int i = 0; i < index; i++) {
			paragraphBuilder.append(StyledText.builder().text(lines.get(i)).font(STEP_HOOK_TEXT_FONT)
					.fontSize((float) STEP_HOOK_TEXT_FONT_SIZE).color(stepTextColor).build()).appendNewLine();
		}
		return ParagraphCell.builder().paragraph(paragraphBuilder.build()).lineSpacing(1.1f).build();
	}
}
