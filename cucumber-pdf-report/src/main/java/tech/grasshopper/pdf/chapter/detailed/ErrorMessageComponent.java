package tech.grasshopper.pdf.chapter.detailed;

import java.awt.Color;

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
public class ErrorMessageComponent implements StepOrHookComponent {

	@Default
	private String stackTrace = "";
	@Default
	private boolean hasRowOrString = false;
	@Default
	private Color textColor = Color.RED;

	private static final int ERROR_MSG_LINE_HEIGHT = 15;

	private static final PDFont FONT = ReportFont.ITALIC_FONT;
	private static final int FONT_SIZE = 9;
	private static final int WIDTH = 290;

	private static final int MAX_EXCEPTION_LINES = 10;

	private final TextLengthOptimizer messageOptimizer = TextLengthOptimizer.builder().font(FONT).fontsize(FONT_SIZE)
			.spaceWidth(WIDTH).build();

	@Override
	public int componentHeight() {
		int height = 0;
		if (stackTrace != null && !stackTrace.isEmpty()) {
			String[] lines = stackTrace.split("\\r?\\n");
			height = (lines.length > MAX_EXCEPTION_LINES ? MAX_EXCEPTION_LINES : lines.length) * ERROR_MSG_LINE_HEIGHT;
		}
		return height;
	}

	@Override
	public void componentText(ParagraphBuilder paragraphBuilder) {
		if (stackTrace != null && !stackTrace.isEmpty()) {
			String[] lines = stackTrace.split("\\r?\\n");
			int dispayCount = (lines.length > MAX_EXCEPTION_LINES ? MAX_EXCEPTION_LINES : lines.length);

			for (int i = 0; i < dispayCount; i++) {
				paragraphBuilder.append(StyledText.builder().text(messageOptimizer.optimizeText(lines[i])).font(FONT)
						.fontSize((float) FONT_SIZE).color(textColor).build()).appendNewLine();
			}
		}
	}
}
