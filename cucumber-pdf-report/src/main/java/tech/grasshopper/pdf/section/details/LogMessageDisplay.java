package tech.grasshopper.pdf.section.details;

import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

import lombok.Builder;
import lombok.Data;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.pojo.cucumber.Executable;

@Data
@Builder
public class LogMessageDisplay {

	private Executable executable;

	public AbstractCell display() {

		ParagraphBuilder paragraphBuilder = Paragraph.builder();
		boolean started = true;

		for (String log : executable.getOutput()) {
			if (started)
				started = false;
			else
				paragraphBuilder.appendNewLine();

			paragraphBuilder.append(StyledText.builder().font(ReportFont.REGULAR_FONT).fontSize(10f).text(log).build());
		}

		return ParagraphCell.builder().paragraph(paragraphBuilder.build()).lineSpacing(1.1f).build();
	}
}
