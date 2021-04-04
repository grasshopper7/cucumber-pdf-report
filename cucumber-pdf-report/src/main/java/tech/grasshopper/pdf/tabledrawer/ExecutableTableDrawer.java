package tech.grasshopper.pdf.tabledrawer;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.function.BiConsumer;

import org.vandeseer.easytable.RepeatedHeaderTableDrawer;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.structure.Row;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class ExecutableTableDrawer extends RepeatedHeaderTableDrawer {

	@Override
	protected void drawWithFunction(PageData pageData, Point2D.Float startingPoint,
			BiConsumer<Drawer, DrawingContext> consumer) {
		float y = startingPoint.y;

		for (int rowIndex = pageData.firstRowOnPage; rowIndex < pageData.firstRowOnNextPage; rowIndex++) {
			final Row row = table.getRows().get(rowIndex);
			y -= row.getHeight();

			if ((rowIndex + 1) == pageData.firstRowOnNextPage) {
				row.getCells().forEach(c -> {
					c.getSettings().setBorderWidthBottom(1f);
					c.getSettings().setBorderColor(Color.GRAY);
				});
			}

			drawRow(new Point2D.Float(startingPoint.x, y), row, rowIndex, consumer);
			finalY = y;
		}
	}
}
