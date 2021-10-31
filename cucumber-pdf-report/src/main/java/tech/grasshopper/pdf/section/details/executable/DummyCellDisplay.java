package tech.grasshopper.pdf.section.details.executable;

import java.awt.Color;

import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

public class DummyCellDisplay {

	public static AbstractCell dummyCellLeftBorder() {
		return TextCell.builder().text("").fontSize(0).borderColor(Color.GRAY).borderWidthLeft(1f).build();
	}

	public static AbstractCell dummyCellRightBorder() {
		return TextCell.builder().text("").fontSize(0).borderColor(Color.GRAY).borderWidthRight(1f).build();
	}
}
