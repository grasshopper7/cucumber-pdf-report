package tech.grasshopper.pdf.section.details.executable.logs;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.pojo.cucumber.Row;
import tech.grasshopper.pdf.section.details.executable.table.TableCellWithMessage;
import tech.grasshopper.pdf.section.details.executable.table.TableColumnOptimizer;

@SuperBuilder
public class TableLog extends Log {

	@Override
	public AbstractCell display() {

		/*
		 * TableBuilder tableBuilder =
		 * Table.builder().font(ReportFont.REGULAR_FONT).fontSize(fontsize).textColor(
		 * color) .borderColor(Color.GRAY).borderWidth(1f).padding(2f);
		 * tableBuilder.addColumnsOfWidth(200f, 100f);
		 */

		Document doc = Jsoup.parseBodyFragment(content);
		Element tableElement = doc.selectFirst("table");
		List<Row> rows = new ArrayList<>();

		if (tableElement != null) {
			Elements rowElements = tableElement.select("tr");

			for (Element rowElement : rowElements) {

				List<String> cells = new ArrayList<>();
				rows.add(Row.builder().cells(cells).build());

				// RowBuilder rowBuilder = Row.builder();
				for (Element cell : rowElement.select("td")) {
					// rowBuilder.add(TextCell.builder().text(cell.text()).build());
					cells.add(cell.text());
				}
				// tableBuilder.addRow(rowBuilder.build());
			}
		}

		System.out.println(rows);

		List<Float> columnTextWidths = TableColumnOptimizer.builder().rows(rows).fontsize(fontsize).build()
				.organizeColumnStructure();

		return TableCellWithMessage.builder().rows(rows).columnTextWidths(columnTextWidths).fontsize(fontsize)
				.textColor(color).build().createTableCell();
	}
}
