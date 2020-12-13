package tech.grasshopper.pdf.chapter.detailed;

import java.util.List;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

import lombok.Builder;
import lombok.Data;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.pojo.cucumber.Row;

@Data
@Builder
public class DataTableComponent implements StepOrHookComponent {

	private List<Row> rows;

	private static final int LINE_HEIGHT = 15;
	private static final int MAX_ROWS = 4;
	private static final int MAX_COLS = 4;
	private static final PDFont FONT = ReportFont.REGULAR_FONT;
	private static final int FONT_SIZE = 8;
	private static final int WIDTH = 65;

	@Builder.Default
	private final TextLengthOptimizer dataCellOptimizer = TextLengthOptimizer.builder().font(FONT).fontsize(FONT_SIZE)
			.spaceWidth(WIDTH).build();

	@Override
	public int componentHeight() {
		int height = 0;
		if (!rows.isEmpty()) {
			if (rows.size() <= MAX_ROWS)
				height = rows.size() * LINE_HEIGHT;
			else
				height = MAX_ROWS * LINE_HEIGHT;
		}
		return height;
	}

	@Override
	public void componentText(ParagraphBuilder paragraphBuilder) {
		if (!rows.isEmpty()) {
			List<Row> dataTableRows = rows.size() <= MAX_ROWS ? rows : rows.subList(0, MAX_ROWS);

			for (tech.grasshopper.pdf.pojo.cucumber.Row row : dataTableRows) {
				List<String> columns = row.getCells().size() <= MAX_COLS ? row.getCells()
						: row.getCells().subList(0, MAX_COLS);
				String rowData = "    | ";
				for (String col : columns)
					rowData = rowData + dataCellOptimizer.optimizeDataCellText(col) + " | ";
				paragraphBuilder
						.append(StyledText.builder().text(rowData).font(FONT).fontSize((float) FONT_SIZE).build())
						.appendNewLine();
			}
		}
	}
}
