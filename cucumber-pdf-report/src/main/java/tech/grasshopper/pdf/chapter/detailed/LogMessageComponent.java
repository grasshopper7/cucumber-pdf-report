package tech.grasshopper.pdf.chapter.detailed;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;

@Data
@Builder
public class LogMessageComponent implements StepOrHookComponent {

	private List<String> logMessages;
	@Default
	private boolean hasRowOrStringOrError = false;
	@Default
	private Color textColor = Color.GREEN;

	private static final int OUTPUT_MSG_LINE_HEIGHT = 15;
	private static final int ROWS_DOC_SEPARATOR_HEIGHT = 15;
	private static final int SEP_CHAR_REPEAT_COUNT = 95;

	private static final PDFont FONT = ReportFont.REGULAR_FONT;
	private static final int FONT_SIZE = 9;
	private static final int WIDTH = 290;

	private final TextLengthOptimizer messageOptimizer = TextLengthOptimizer.builder().font(FONT).fontsize(FONT_SIZE)
			.spaceWidth(WIDTH).build();

	@Override
	public int componentHeight() {
		int height = 0;
		if (logMessages != null && !logMessages.isEmpty()) {
			height = logMessages.size() * OUTPUT_MSG_LINE_HEIGHT;
			if (hasRowOrStringOrError)
				height = height + ROWS_DOC_SEPARATOR_HEIGHT;
		}
		return height;
	}

	@Override
	public void componentText(ParagraphBuilder paragraphBuilder) {
		if (logMessages != null) {
			if (hasRowOrStringOrError && !logMessages.isEmpty())
				paragraphBuilder
						.append(StyledText.builder()
								.text(String.join("", Collections.nCopies(SEP_CHAR_REPEAT_COUNT, "-"))).build())
						.appendNewLine();
			for (String output : logMessages)
				paragraphBuilder.append(StyledText.builder().text(messageOptimizer.optimizeText(output))
						.fontSize((float) FONT_SIZE).color(textColor).build()).appendNewLine();
		}
	}
}
