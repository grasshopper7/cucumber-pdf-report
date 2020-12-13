package tech.grasshopper.pdf.chapter.detailed;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

import lombok.Builder;
import lombok.Data;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;

@Data
@Builder
public class DocStringComponent implements StepOrHookComponent {

	private String docString;

	private static final int LINE_HEIGHT = 15;
	private static final int MAX_LINES = 4;
	private static final PDFont FONT = ReportFont.REGULAR_FONT;
	private static final int FONT_SIZE = 8;
	private static final int WIDTH = 290;

	@Builder.Default
	private final TextLengthOptimizer docStringOptimizer = TextLengthOptimizer.builder().font(FONT).fontsize(FONT_SIZE)
			.spaceWidth(WIDTH).build();

	@Override
	public int componentHeight() {
		int height = 0;
		if (docString != null) {
			String[] lines = docString.split("\\r?\\n");
			if (lines.length <= MAX_LINES)
				height = lines.length * LINE_HEIGHT;
			else
				height = MAX_LINES * LINE_HEIGHT;
		}
		return height;
	}

	@Override
	public void componentText(ParagraphBuilder paragraphBuilder) {
		if (docString != null) {
			String[] lines = docString.split("\\r?\\n");
			int index = lines.length <= MAX_LINES ? lines.length : MAX_LINES;

			for (int i = 0; i < index; i++) {
				String lineData = "    " + lines[i];
				paragraphBuilder.append(StyledText.builder().text(docStringOptimizer.optimizeText(lineData)).font(FONT)
						.fontSize((float) FONT_SIZE).build()).appendNewLine();
			}
		}
	}
}
