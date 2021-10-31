package tech.grasshopper.pdf.section.details.executable.logs;

import java.awt.Color;

import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.Setter;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.optimizer.TextSanitizer;

@SuperBuilder
public class Log {

	@Setter
	protected String content;

	@Setter
	protected Color color;

	protected final int fontsize = 8;

	protected final TextSanitizer sanitizer = TextSanitizer.builder().build();

	public AbstractCell display() {

		if (content.trim().startsWith("<table"))
			return TableLog.builder().content(content).color(color).build().display();

		return TextCell.builder().text(content).textColor(color).fontSize(fontsize).borderColor(Color.GRAY)
				.borderWidthLeft(1f).borderWidthRight(1f).build();
	}

}
