package tech.grasshopper.pdf.section.details.executable;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

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
public class StackTraceDisplay {

	@Setter
	private Executable executable;

	@Setter
	private Color color;

	private final float fontsize = 9f;

	public AbstractCell display() {

		ParagraphBuilder paragraphBuilder = Paragraph.builder();
		TextSanitizer sanitizer = TextSanitizer.builder().build();

		List<String> displayLines = new ArrayList<>();

		if (executable.getErrorMessage() != null && !executable.getErrorMessage().isEmpty()) {
			String[] lines = executable.getErrorMessage().split("\\r?\\n");
			String nonTraceLine = "";

			for (String line : lines) {
				if (line.startsWith("\tat")) {
					if (!nonTraceLine.isEmpty()) {
						displayLines.add(nonTraceLine);
						nonTraceLine = "";
					}
					// Replace tab with spaces to avoid warning message from sanitizer. HACK!!
					// displayLines.add(line.replaceFirst("\t", " "));
					displayLines.add(line);
				} else {
					if (nonTraceLine.isEmpty())
						nonTraceLine = line;
					else
						nonTraceLine = nonTraceLine + " " + line;
				}
			}

			if (displayLines.isEmpty())
				displayLines.add(executable.getErrorMessage());
		}

		if (!displayLines.isEmpty()) {
			for (String line : displayLines) {
				paragraphBuilder.append(StyledText.builder().font(ReportFont.REGULAR_FONT).fontSize(fontsize)
						.text(sanitizer.sanitizeText(line)).color(color).build()).appendNewLine();
			}
		}

		String message = sanitizer.getStripMessage();

		if (!message.isEmpty())
			paragraphBuilder.append(StyledText.builder().font(ReportFont.REGULAR_FONT).fontSize(fontsize).text(message)
					.color(color).build());

		return ParagraphCell.builder().paragraph(paragraphBuilder.build()).lineSpacing(1.2f).borderColor(Color.GRAY)
				.borderWidth(1).build();
	}
}
