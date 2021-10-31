package tech.grasshopper.pdf.section.details.executable;

import static tech.grasshopper.pdf.section.details.executable.DummyCellDisplay.dummyCellLeftBorder;
import static tech.grasshopper.pdf.section.details.executable.DummyCellDisplay.dummyCellRightBorder;

import java.awt.Color;

import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table.TableBuilder;

import lombok.Builder;
import lombok.Setter;
import tech.grasshopper.pdf.pojo.cucumber.Executable;
import tech.grasshopper.pdf.section.details.executable.logs.Log;

@Builder
public class LogMessageDisplay {

	@Setter
	private Executable executable;

	@Setter
	private Color color;

	@Setter
	private TableBuilder tableBuilder;

	public void display() {

		for (String log : executable.getOutput()) {
			tableBuilder.addRow(Row.builder().add(dummyCellLeftBorder())
					.add(Log.builder().content(log).color(color).build().display()).add(dummyCellRightBorder())
					.add(dummyCellRightBorder()).build());
		}

		/*
		 * String message = sanitizer.getStripMessage();
		 * 
		 * if (!message.isEmpty())
		 * paragraphBuilder.appendNewLine().append(StyledText.builder().font(ReportFont.
		 * REGULAR_FONT) .fontSize(fontsize).text(message).color(color).build());
		 * 
		 * return
		 * ParagraphCell.builder().paragraph(paragraphBuilder.build()).borderColor(Color
		 * .GRAY).borderWidth(1) .lineSpacing(1.2f).build();
		 */
	}
}
