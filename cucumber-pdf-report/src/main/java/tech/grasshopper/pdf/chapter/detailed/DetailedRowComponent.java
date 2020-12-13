package tech.grasshopper.pdf.chapter.detailed;

import java.awt.Color;
import java.util.List;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.component.Component;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class DetailedRowComponent extends Component {

	private List<StepOrHookRow> rows;
	private Scenario scenario;
	private Feature feature;
	private int startHeight;
	private int initialSno;

	private static final PDFont NAME_FONT = ReportFont.REGULAR_FONT;
	private static final int NAME_FONT_SIZE = 10;
	private static final PDFont TABLE_DOCSTRING_FONT = ReportFont.REGULAR_FONT;
	private static final int TABLE_DOCSTRING_FONT_SIZE = 8;
	private static final PDFont OUTPUT_MSG_FONT = ReportFont.REGULAR_FONT;
	private static final int OUTPUT_MSG_FONT_SIZE = 9;

	private static final int STEP_COLUMN_WIDTH = 340;
	private static final int DOCSTRING_WIDTH = 290;
	private static final int DATACELL_WIDTH = 65;
	private static final int OUTPUT_MSG_WIDTH = 290;
	private static final int PADDING = 5;

	private static final int HEADER_ROW_HEIGHT = 22;

	@Builder.Default
	private final TextLengthOptimizer stepTextOptimizer = TextLengthOptimizer.builder().font(NAME_FONT)
			.fontsize(NAME_FONT_SIZE).spaceWidth(STEP_COLUMN_WIDTH - 2 * PADDING).build();

	@Builder.Default
	private final TextLengthOptimizer docStringOptimizer = TextLengthOptimizer.builder().font(TABLE_DOCSTRING_FONT)
			.fontsize(TABLE_DOCSTRING_FONT_SIZE).spaceWidth(DOCSTRING_WIDTH).build();

	@Builder.Default
	private final TextLengthOptimizer dataCellOptimizer = TextLengthOptimizer.builder().font(TABLE_DOCSTRING_FONT)
			.fontsize(TABLE_DOCSTRING_FONT_SIZE).spaceWidth(DATACELL_WIDTH).build();

	@Builder.Default
	private final TextLengthOptimizer outputMsgOptimizer = TextLengthOptimizer.builder().font(OUTPUT_MSG_FONT)
			.fontsize(OUTPUT_MSG_FONT_SIZE).spaceWidth(OUTPUT_MSG_WIDTH).build();

	@Override
	public void display() {

		TableBuilder myTableBuilder = Table.builder().addColumnsOfWidth(25, STEP_COLUMN_WIDTH, 50, 85).padding(PADDING)
				.borderColor(Color.LIGHT_GRAY).borderWidth(1).font(ReportFont.ITALIC_FONT).fontSize(12)

				.addRow(Row.builder().height((float) HEADER_ROW_HEIGHT).horizontalAlignment(HorizontalAlignment.CENTER)
						.verticalAlignment(VerticalAlignment.MIDDLE).add(TextCell.builder().text("#").build())
						.add(TextCell.builder().text("Step / Hook Text").build())
						.add(TextCell.builder().text("Status").build()).add(TextCell.builder().text("Duration").build())
						.build());

		for (int i = 0; i < rows.size(); i++) {
			StepOrHookRow row = rows.get(i);
			row.setSNo(String.valueOf(initialSno));

			myTableBuilder.addRow(row.generateRow());
			initialSno = row.incrementSerialNumber(initialSno);
		}

		Table myTable = myTableBuilder.build();
		TableDrawer tableDrawer = TableDrawer.builder().contentStream(content).startX(50).startY(startHeight).page(page)
				.table(myTable).build();
		tableDrawer.draw();
	}
}
