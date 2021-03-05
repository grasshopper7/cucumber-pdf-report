package tech.grasshopper.pdf.tablecell;

import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.drawing.cell.AbstractCellDrawer;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TableWithinTableDrawer extends AbstractCellDrawer<TableWithinTableCell> {

	public TableWithinTableDrawer(TableWithinTableCell tableWithinTableCell) {
		this.cell = tableWithinTableCell;
	}

	@Override
	public void drawContent(DrawingContext drawingContext) {
		TableDrawer.builder().startX(drawingContext.getStartingPoint().x + this.cell.getPaddingLeft())
				.startY(drawingContext.getStartingPoint().y + cell.getHeight() - this.cell.getPaddingTop())
				.table(this.cell.getTable()).contentStream(drawingContext.getContentStream()).build().draw();
	}

	@Override
	protected float calculateInnerHeight() {

		return cell.getTable().getHeight();
	}
}
