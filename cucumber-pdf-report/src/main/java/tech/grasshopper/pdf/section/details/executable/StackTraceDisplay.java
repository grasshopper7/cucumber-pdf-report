package tech.grasshopper.pdf.section.details.executable;

import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_COLUMN_WIDTH;
import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_FONT;
import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_PADDING;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

import lombok.Builder;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.optimizer.TextSanitizer;
import tech.grasshopper.pdf.pojo.cucumber.Executable;

@Builder
public class StackTraceDisplay {

	private Executable executable;

	private Color color;

	private final float fontsize = 9f;

	private static final int START_EXCEPTION_LINES = 4;
	private static final int FINISH_EXCEPTION_LINES = 3;
	private static final int MAX_LINES_PER_EXCEPTION = 3;

	public AbstractCell display() {

		ParagraphBuilder paragraphBuilder = Paragraph.builder();
		final TextSanitizer sanitizer = TextSanitizer.builder().build();
		final TextLengthOptimizer textOptimizer = TextLengthOptimizer.builder().font(STEP_HOOK_TEXT_FONT)
				.fontsize((int) fontsize).availableSpace((STEP_HOOK_TEXT_COLUMN_WIDTH) - 2 * STEP_HOOK_TEXT_PADDING)
				.maxLines(MAX_LINES_PER_EXCEPTION).build();

		List<String> displayLines = new ArrayList<>();
		boolean trimStackLine = false;
		boolean extraLines = false;

		if (executable.getErrorMessage() != null && !executable.getErrorMessage().isEmpty()) {
			String[] lines = executable.getErrorMessage().split("\\r?\\n");
			String nonTraceLine = "";

			for (String line : lines) {
				if (line.startsWith("\tat")) {
					if (!nonTraceLine.isEmpty()) {
						displayLines.add(nonTraceLine);
						nonTraceLine = "";
					}
					displayLines.add(line);
				} else {
					if (nonTraceLine.isEmpty())
						nonTraceLine = line;
					else
						nonTraceLine = nonTraceLine + " " + line;
				}
			}
		}

		if (!displayLines.isEmpty()) {

			List<String> lines = displayLines;
			if (displayLines.size() > START_EXCEPTION_LINES + FINISH_EXCEPTION_LINES) {
				extraLines = true;
				lines = displayLines.subList(0, START_EXCEPTION_LINES);
				lines.add(".....");
				lines.addAll(displayLines.subList(displayLines.size() - FINISH_EXCEPTION_LINES, displayLines.size()));
			}

			for (String line : lines) {
				paragraphBuilder.append(StyledText.builder().font(ReportFont.REGULAR_FONT).fontSize(fontsize)
						.text(sanitizer.sanitizeText(textOptimizer.optimizeTextLines(line))).color(color).build())
						.appendNewLine();

				if (!trimStackLine && textOptimizer.isTextTrimmed())
					trimStackLine = true;
			}
		}

		final String croppedMsgSuffix = " to fit in the available space.";
		String message = "";

		if (trimStackLine)
			message = "* Each stack line length is trimmed to " + MAX_LINES_PER_EXCEPTION + " lines";

		if (extraLines) {
			String capitalizeText = trimStackLine ? " and f" : "* F";
			message = message + capitalizeText + "irst " + START_EXCEPTION_LINES + " and last " + FINISH_EXCEPTION_LINES
					+ " stack lines are displayed";

		}

		if (!message.isEmpty())
			paragraphBuilder.append(StyledText.builder().font(ReportFont.REGULAR_FONT).fontSize(fontsize)
					.text(message + croppedMsgSuffix).color(color).build());

		return ParagraphCell.builder().paragraph(paragraphBuilder.build()).lineSpacing(1.1f).build();
	}
}
