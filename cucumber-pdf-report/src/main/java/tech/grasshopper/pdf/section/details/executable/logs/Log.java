package tech.grasshopper.pdf.section.details.executable.logs;

import java.awt.Color;

import org.vandeseer.easytable.structure.cell.AbstractCell;

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
		return logType(content, color).display();
	}

	private static Log logType(String content, Color color) {
		if (content.contains("<table"))
			return TableLog.builder().content(content).color(color).build();

		return TextLog.builder().content(content).color(color).build();
	}
}
