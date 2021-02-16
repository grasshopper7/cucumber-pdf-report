package tech.grasshopper.pdf.section.dashboard;

import java.awt.Color;

import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.cell.TextCell;

public class DashboardDisplayUtil {

	public static Row spacerRow() {
		return Row.builder().add(TextCell.builder().text("").padding(0f).colSpan(5).build()).build();
	}

	public static TextCell spacerCell() {
		return TextCell.builder().text("").backgroundColor(Color.WHITE).borderWidth(0f).build();
	}
}
