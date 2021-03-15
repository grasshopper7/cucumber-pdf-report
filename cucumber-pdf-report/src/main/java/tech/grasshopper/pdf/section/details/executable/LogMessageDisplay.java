package tech.grasshopper.pdf.section.details.executable;

import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_COLUMN_WIDTH;
import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_FONT;
import static tech.grasshopper.pdf.section.details.DetailedStepHookDisplay.STEP_HOOK_TEXT_PADDING;

import java.awt.Color;
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
public class LogMessageDisplay {

	private Executable executable;

	private Color color;

	private final float fontsize = 9f;

	private static final int MAX_COUNT_LOGS = 4;

	private static final int MAX_LINES_PER_LOG = 2;

	public AbstractCell display() {

		final TextSanitizer sanitizer = TextSanitizer.builder().build();

		final TextLengthOptimizer textOptimizer = TextLengthOptimizer.builder().font(STEP_HOOK_TEXT_FONT)
				.fontsize((int) fontsize).availableSpace((STEP_HOOK_TEXT_COLUMN_WIDTH) - 2 * STEP_HOOK_TEXT_PADDING)
				.maxLines(MAX_LINES_PER_LOG).build();

		final ParagraphBuilder paragraphBuilder = Paragraph.builder();
		boolean started = true;
		boolean trimLog = false;

		List<String> logs = executable.getOutput().size() > MAX_COUNT_LOGS
				? executable.getOutput().subList(0, MAX_COUNT_LOGS)
				: executable.getOutput();

		for (String log : logs) {
			if (started)
				started = false;
			else
				paragraphBuilder.appendNewLine();

			paragraphBuilder.append(StyledText.builder().font(ReportFont.REGULAR_FONT).fontSize(fontsize)
					.text(sanitizer.sanitizeText(textOptimizer.optimizeTextLines(log))).color(color).build());

			if (!trimLog && textOptimizer.isTextTrimmed())
				trimLog = true;
		}

		final String croppedMsgSuffix = " to fit in the available space.";
		String message = "";

		if (trimLog)
			message = "* Each log length is trimmed to " + MAX_LINES_PER_LOG + " lines";

		if (executable.getOutput().size() > MAX_COUNT_LOGS) {
			String capitalizeText = trimLog ? " and m" : "* M";
			message = message + capitalizeText + "aximum of " + MAX_COUNT_LOGS + " logs are displayed";

		}

		if (!message.isEmpty())
			paragraphBuilder.appendNewLine().append(StyledText.builder().font(ReportFont.REGULAR_FONT)
					.fontSize(fontsize).text(message + croppedMsgSuffix).color(color).build());

		return ParagraphCell.builder().paragraph(paragraphBuilder.build()).lineSpacing(1.1f).build();
	}
}
