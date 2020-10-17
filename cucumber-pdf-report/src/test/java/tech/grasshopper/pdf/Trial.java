package tech.grasshopper.pdf;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PageMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;
import org.vandeseer.easytable.structure.cell.paragraph.Hyperlink;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

import tech.grasshopper.pdf.component.chart.ChartDisplayer;
import tech.grasshopper.pdf.component.chart.ReportBarChart;
import tech.grasshopper.pdf.component.text.TextComponent;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.pojo.report.Text;

public class Trial {

	public static void main(String[] args) {

		try (PDDocument document = new PDDocument()) {
			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);

			try (PDPageContentStream content = new PDPageContentStream(document, page)) {

				System.out.println(page.getMediaBox().getWidth());
				System.out.println(page.getMediaBox().getHeight());

				/*
				 * content.setNonStrokingColor(Color.GRAY); content.addRect(50, 50, 500, 750);
				 * content.fill();
				 */

				PDFont font = PDType1Font.HELVETICA_BOLD;
				float fontSize = 14;

				float stringWidth = font.getStringWidth("Sep 21, 2020 1:06:23 PM") * fontSize;

				float stringHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() * fontSize;

				System.out.println(stringWidth / 1000 + " --- " + stringHeight / 1000);

				ReportBarChart chart = new ReportBarChart(300, 200);

				// Customize Chart
				// chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
				// chart.getStyler().setHasAnnotations(true);
				chart.getStyler().setPlotGridLinesVisible(true);
				chart.getStyler().setYAxisMax(10.0);

				// Series
				chart.addSeries("test 1", Arrays.asList(0, 1, 2, 3, 4), Arrays.asList(0.1, 0.2, 0.3, 0.7, 0.8));

				ChartDisplayer.builder().document(document).content(content).chart(chart).xBottomLeft(50)
						.yBottomLeft(500).build().display();

				PDDocumentOutline outline = new PDDocumentOutline();
				document.getDocumentCatalog().setDocumentOutline(outline);
				PDOutlineItem pagesOutline = new PDOutlineItem();
				pagesOutline.setTitle("All Pages");
				outline.addLast(pagesOutline);

				TextComponent.builder().content(content).text(Text.builder().text("HELLO THERE HOW ARE U?").xoffset(100)
						.yoffset(200).fontSize(fontSize).build()).build().display();
				;

				// PDPageDestination dest = new PDPageFitWidthDestination();
				PDPageXYZDestination dest = new PDPageXYZDestination();
				dest.setLeft(100);
				dest.setTop(220);
				// If you want to have several bookmarks pointing to different areas
				// on the same page, have a look at the other classes derived from
				// PDPageDestination.

				dest.setPage(page);
				PDOutlineItem bookmark = new PDOutlineItem();
				bookmark.setDestination(dest);
				bookmark.setTitle("Page One");
				pagesOutline.addLast(bookmark);
				
				PDOutlineItem bookmark2 = new PDOutlineItem();
				bookmark2.setDestination(dest);
				bookmark2.setTitle("Page One");
				pagesOutline.addLast(bookmark2);

				pagesOutline.openNode();
				outline.openNode();

				// optional: show the outlines when opening the file
				document.getDocumentCatalog().setPageMode(PageMode.USE_OUTLINES);

				TextLengthOptimizer to = TextLengthOptimizer.builder().font(PDType1Font.HELVETICA_BOLD_OBLIQUE).fontsize(14).spaceWidth(270).build();
				System.out.println(to.optimizeText("Mounish Saha Mounish Saha Mounish Saha"));
				
				Table myTable = Table.builder().addColumnsOfWidth(25, 310).padding(5)

				.addRow(Row.builder()
						.add(TextCell.builder().text("1").textColor(Color.BLUE).build())
						.add(ParagraphCell.builder()
	                            .padding(8)
	                            .lineSpacing(2f)
	                            .minHeight(15)
	                            .paragraph(Paragraph.builder()
	                                    .append(StyledText.builder().text("This is some text in one font.").font(PDType1Font.HELVETICA).build())
	                                    .appendNewLine()
	                                    .append(StyledText.builder().text("But this text that introduces a link that follows is different. Here comes the link: ").font(PDType1Font.COURIER_BOLD).fontSize(6f).build())
	                                    .append(Hyperlink.builder().text("github!").url("http://www.github.com").font(PDType1Font.COURIER_BOLD).fontSize(6f).color(Color.WHITE).build())
	                                    .appendNewLine(6f)
	                                    .append(StyledText.builder().text("There was the link. And now we are using the default font from the cell.").build())
	                                    .build())
	                            .build())
					.build()).build();
				
				TableDrawer tableDrawer = TableDrawer.builder().page(page).contentStream(content).startX(50).startY(400)
						.table(myTable).build();
				tableDrawer.draw();
				
				List<String> mainList = Arrays.asList("AA","BB","CC","DD","EE");
				System.out.println(mainList.subList(0, 3));
			}

			PDPage page2 = new PDPage(PDRectangle.A4);
			document.addPage(page2);

			document.save(new File("trial.pdf"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
