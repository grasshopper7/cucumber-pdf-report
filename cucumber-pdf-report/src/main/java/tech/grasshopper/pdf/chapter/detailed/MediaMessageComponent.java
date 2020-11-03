package tech.grasshopper.pdf.chapter.detailed;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

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
public class MediaMessageComponent implements StepOrHookComponent {

	private List<String> mediaMessages;
	@Default
	private boolean hasRowOrStringOrError = false;
	@Default
	private Color textColor = Color.GRAY;

	private static final int MEDIA_MSG_LINE_HEIGHT = 15;
	private static final int ROWS_DOC_SEPARATOR_HEIGHT = 15;
	private static final int SEP_CHAR_REPEAT_COUNT = 95;

	private static final PDFont FONT = PDType1Font.HELVETICA;
	private static final int FONT_SIZE = 9;
	private static final int WIDTH = 290;

	private final TextLengthOptimizer messageOptimizer = TextLengthOptimizer.builder().font(FONT).fontsize(FONT_SIZE)
			.spaceWidth(WIDTH).build();
	
	private final TextContentSanitizer textSanitizer = TextContentSanitizer.builder().font(FONT).build();

	@Override
	public int componentHeight() {
		int height = 0;
		if (mediaMessages != null && !mediaMessages.isEmpty()) {
			height = mediaMessages.size() * MEDIA_MSG_LINE_HEIGHT;
			if (hasRowOrStringOrError)
				height = height + ROWS_DOC_SEPARATOR_HEIGHT;
		}
		return height;
	}

	@Override
	public void componentText(ParagraphBuilder paragraphBuilder) {
		if (mediaMessages != null) {
			if (hasRowOrStringOrError && !mediaMessages.isEmpty())
				paragraphBuilder
						.append(StyledText.builder()
								.text(String.join("", Collections.nCopies(SEP_CHAR_REPEAT_COUNT, "-"))).build())
						.appendNewLine();

			for (String media : mediaMessages)
				paragraphBuilder.append(StyledText.builder()
						.text(messageOptimizer.optimizeText(
								textSanitizer.sanitizeText("Media saved - " + media)))
						.fontSize((float) FONT_SIZE).color(textColor).build()).appendNewLine();
		}
	}
}
