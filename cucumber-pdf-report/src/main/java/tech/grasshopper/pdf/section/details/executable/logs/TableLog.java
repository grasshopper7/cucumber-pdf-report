package tech.grasshopper.pdf.section.details.executable.logs;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.exception.TableCellSpanException;
import tech.grasshopper.pdf.pojo.cucumber.Row;
import tech.grasshopper.pdf.section.details.executable.table.TableCellWithMessage;
import tech.grasshopper.pdf.section.details.executable.table.TableColumnOptimizer;

@SuperBuilder
public class TableLog extends Log {

	@Override
	public AbstractCell display() {

		try {
			List<Row> rows = collectCellData();

			List<Float> columnTextWidths = TableColumnOptimizer.builder().rows(rows).fontsize(fontsize).build()
					.organizeColumnStructure();

			return TableCellWithMessage.builder().rows(rows).columnTextWidths(columnTextWidths).fontsize(fontsize)
					.textColor(color).build().createTableCell();
		} catch (TableCellSpanException e) {
			String message = e.getMessage().concat(System.lineSeparator()).concat(content);
			return TextLog.builder().content(message).color(color).build().display();
		} catch (Exception e) {
			String message = "Exception occured in displaying log.".concat(System.lineSeparator()).concat(content);
			return TextLog.builder().content(message).color(color).build().display();
		}
	}

	private List<Row> collectCellData() {

		List<Row> rows = new ArrayList<>();

		// Only the first table is displayed.
		Document doc = Jsoup.parseBodyFragment(content);
		Element tableElement = doc.selectFirst("table");

		if (tableElement != null) {
			for (Element rowElement : tableElement.select("tr")) {

				List<String> cells = new ArrayList<>();
				rows.add(Row.builder().cells(cells).build());

				Elements cellElements = rowElement.select("th");
				cellElements.addAll(rowElement.select("td"));

				if (cellElements.isEmpty())
					continue;

				for (Element cell : cellElements) {
					checkCellSpanAttribute(cell);
					cells.add(cell.text());
				}
			}
		}
		return rows;
	}

	private void checkCellSpanAttribute(Element cell) {

		String rowSpanAttr = cell.attr("rowspan").trim();
		String colSpanAttr = cell.attr("colspan").trim();

		if (rowSpanAttr.matches("\\d+") && Integer.parseInt(rowSpanAttr) != 1)
			throw new TableCellSpanException("Rowspan greater than 1 is not supported.");

		if (colSpanAttr.matches("\\d+") && Integer.parseInt(colSpanAttr) != 1)
			throw new TableCellSpanException("Colspan greater than 1 is not supported.");
	}
}
