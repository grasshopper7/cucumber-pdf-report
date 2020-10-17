package tech.grasshopper.pdf.chapter.detailed;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import tech.grasshopper.pdf.optimizer.TextContentSanitizer;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;

@Data
@Builder
public class ErrorMessageComponent implements StepOrHookComponent {

	private String stackTrace;
	@Default
	private boolean hasRowOrString = false;
	@Default
	private Color textColor = Color.RED;

	private static final int ERROR_MSG_LINE_HEIGHT = 15;

	private static final PDFont FONT = PDType1Font.HELVETICA_OBLIQUE;
	private static final int FONT_SIZE = 9;
	private static final int WIDTH = 290;

	private final TextLengthOptimizer messageOptimizer = TextLengthOptimizer.builder().font(FONT).fontsize(FONT_SIZE)
			.spaceWidth(WIDTH).build();

	private final TextContentSanitizer textSanitizer = TextContentSanitizer.builder().font(FONT).build();

	@Override
	public int componentHeight() {
		int height = 0;
		if (!stackTrace.isEmpty()) {
			String[] lines = stackTrace.split("\\r?\\n");
			height = lines.length * ERROR_MSG_LINE_HEIGHT;
		}
		return height;
	}

	@Override
	public void componentText(ParagraphBuilder paragraphBuilder) {
		if (!stackTrace.isEmpty()) {
			String[] lines = stackTrace.split("\\r?\\n");

			for (int i = 0; i < lines.length; i++) {
				paragraphBuilder.append(
						StyledText.builder().text(messageOptimizer.optimizeText(textSanitizer.sanitizeText(lines[i])))
								.font(FONT).fontSize((float) FONT_SIZE).color(textColor).build())
						.appendNewLine();
			}
		}
	}
}
