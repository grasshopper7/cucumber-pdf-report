package tech.grasshopper.pdf.section.details.executable.logs;

import java.awt.Color;

import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class TextLog extends Log {

	@Override
	public AbstractCell display() {
		return TextCell.builder().text(sanitizer.sanitizeText(content)).textColor(color).fontSize(fontsize)
				.borderColor(Color.GRAY).borderWidthLeft(1f).borderWidthRight(1f).build();
	}
}
