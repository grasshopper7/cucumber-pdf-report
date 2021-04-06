package tech.grasshopper.pdf.section.details.executable;

import java.awt.Color;

import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

import lombok.Builder;
import lombok.Setter;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextSanitizer;
import tech.grasshopper.pdf.pojo.cucumber.Executable;

@Builder
public class LogMessageDisplay {

	@Setter
	private Executable executable;

	@Setter
	private Color color;

	private final float fontsize = 9f;

	public AbstractCell display() {

		TextSanitizer sanitizer = TextSanitizer.builder().build();

		ParagraphBuilder paragraphBuilder = Paragraph.builder();
		boolean started = true;

		for (String log : executable.getOutput()) {
			if (started)
				started = false;
			else
				paragraphBuilder.appendNewLine();

			paragraphBuilder.append(StyledText.builder().font(ReportFont.REGULAR_FONT).fontSize(fontsize)
					.text(sanitizer.sanitizeText(log)).color(color).build());
		}

		String message = sanitizer.getStripMessage();

		if (!message.isEmpty())
			paragraphBuilder.appendNewLine().append(StyledText.builder().font(ReportFont.REGULAR_FONT)
					.fontSize(fontsize).text(message).color(color).build());

		return ParagraphCell.builder().paragraph(paragraphBuilder.build()).borderColor(Color.GRAY).borderWidth(1)
				.lineSpacing(1.2f).build();
	}
}
