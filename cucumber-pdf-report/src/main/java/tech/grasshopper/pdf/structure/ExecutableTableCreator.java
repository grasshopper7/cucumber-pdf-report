package tech.grasshopper.pdf.structure;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.tabledrawer.ExecutableTableDrawer;

@SuperBuilder
public class ExecutableTableCreator extends TableCreator {

	@Override
	public void displayTable() {

		tableDrawer = ExecutableTableDrawer.builder().table(tableBuilder.build()).startX(startX).startY(startY)
				.splitRow(splitRow).endY(endY).numberOfRowsToRepeat(repeatRows).build();

		super.drawTable();
	}
}
