package tech.grasshopper.pdf.tablecell;

import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class TableWithinTableCell extends AbstractCell {

	private Table table;

	@Override
	public float getMinHeight() {
		return table.getHeight() + getVerticalPadding();
	}

	@Override
	protected Drawer createDefaultDrawer() {
		return new TableWithinTableDrawer(this);
	}
}
