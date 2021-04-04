package tech.grasshopper.pdf.tablecell;

import org.vandeseer.easytable.MinimumHeightSplitCellException;
import org.vandeseer.easytable.SplitCellData;
import org.vandeseer.easytable.TableContainRowSpanCellsException;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

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

	@Override
	public SplitCellData splitCell(float height) {

		checkForRowSpanCells();

		if ((table.getRows().get(0).getHeight() + getVerticalPadding()) > height) {
			throw new MinimumHeightSplitCellException();
		}

		SplitCellData data = new SplitCellData();
		TableBuilder samePageTableBuilder = Table.builder().settings(table.getSettings());
		TableBuilder nextPageTableBuilder = Table.builder().settings(table.getSettings());

		table.getColumns().forEach(col -> {
			samePageTableBuilder.addColumnOfWidth(col.getWidth());
			nextPageTableBuilder.addColumnOfWidth(col.getWidth());
		});

		float rowHeightSum = getVerticalPadding();
		boolean samePageTableExists = false;
		boolean nextPageTableExists = false;

		for (Row row : table.getRows()) {
			rowHeightSum += row.getHeight();

			if (rowHeightSum < height) {
				samePageTableExists = true;
				samePageTableBuilder.addRow(row);
			} else {
				nextPageTableExists = true;
				nextPageTableBuilder.addRow(row);
			}
		}

		if (samePageTableExists) {
			Table samePageTable = samePageTableBuilder.build();
			data.setSamePageCell(TableWithinTableCell.builder().table(samePageTable).settings(getSettings()).build());
			data.setSamePageCellPresent(true);
			data.setSamePageCellHeight(samePageTable.getHeight() + getVerticalPadding());
		} else {
			data.setSamePageCell(TextCell.builder().text("").build());
			data.setSamePageCellPresent(false);
			data.setSamePageCellHeight(getVerticalPadding());
		}

		if (nextPageTableExists) {
			Table nextPageTable = nextPageTableBuilder.build();
			data.setNextPageCell(TableWithinTableCell.builder().table(nextPageTable).settings(getSettings()).build());
			data.setNextPageCellPresent(true);
			data.setNextPageCellHeight(nextPageTable.getHeight() + getVerticalPadding());
		} else {
			data.setNextPageCell(TextCell.builder().text("").build());
			data.setNextPageCellPresent(false);
			data.setNextPageCellHeight(getVerticalPadding());
		}

		return data;
	}

	private void checkForRowSpanCells() {
		if (table.getRows().stream().flatMap(r -> r.getCells().stream()).filter(c -> c.getRowSpan() > 1).count() > 0)
			throw new TableContainRowSpanCellsException(
					"TableWithinTable containing cells with rowspan value greater than 1 cannot be split.");
	}

}
