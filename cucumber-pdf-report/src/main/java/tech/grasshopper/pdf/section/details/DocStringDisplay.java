package tech.grasshopper.pdf.section.details;

import java.util.List;

import org.apache.pdfbox.pdmodel.font.PDFont;
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

	private static final int MAX_LINES = 6;
	private static final PDFont FONT = ReportFont.REGULAR_FONT;
	private static final int FONT_SIZE = 10;

	public AbstractCell display() {

		ParagraphBuilder paragraphBuilder = Paragraph.builder();
		final TextSanitizer sanitizer = TextSanitizer.builder().font(ReportFont.REGULAR_FONT).build();

		String sanitizedText = sanitizer.sanitizeText(step.getDocString());
		String stripNewLineTab = sanitizedText.replaceAll("[\\t\\n\\r ]+", " ");

		List<String> lines = PdfUtil.getOptimalTextBreakLines(stripNewLineTab, ReportFont.REGULAR_FONT, 10, 580f);

		int index = lines.size() <= MAX_LINES ? lines.size() : MAX_LINES;

		for (int i = 0; i < index; i++) {
			paragraphBuilder
					.append(StyledText.builder().text(lines.get(i)).font(FONT).fontSize((float) FONT_SIZE).build())
					.appendNewLine();
		}
		return ParagraphCell.builder().paragraph(paragraphBuilder.build()).lineSpacing(1.1f).build();
	}
}
