package tech.grasshopper.pdf.section.details;

import java.awt.Color;

import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

import lombok.Builder;
import lombok.Data;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextSanitizer;
import tech.grasshopper.pdf.pojo.cucumber.Executable;

@Data
@Builder
public class StackTraceDisplay {

	private Executable executable;

	private Color color;

	private static final int MAX_EXCEPTION_LINES = 6;

	public AbstractCell display() {

		ParagraphBuilder paragraphBuilder = Paragraph.builder();
		final TextSanitizer sanitizer = TextSanitizer.builder().font(ReportFont.REGULAR_FONT).build();

		if (executable.getErrorMessage() != null && !executable.getErrorMessage().isEmpty()) {
			String[] lines = executable.getErrorMessage().split("\\r?\\n");
			int dispayCount = (lines.length > MAX_EXCEPTION_LINES ? MAX_EXCEPTION_LINES : lines.length);

			for (int i = 0; i < dispayCount; i++) {
				String sanitizedLine = sanitizer.sanitizeText(lines[i]);
				paragraphBuilder.append(StyledText.builder().font(ReportFont.REGULAR_FONT).fontSize(10f)
						.text(sanitizedLine).color(color).build()).appendNewLine();
			}
		}
		return ParagraphCell.builder().paragraph(paragraphBuilder.build()).lineSpacing(1.1f).build();
	}
}
