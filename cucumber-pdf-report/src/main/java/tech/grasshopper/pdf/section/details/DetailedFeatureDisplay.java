package tech.grasshopper.pdf.section.details;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.knowm.xchart.style.PieStyler;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.ImageCell;
import org.vandeseer.easytable.structure.cell.TextCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.chart.ReportDonutChart;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.destination.DestinationAware;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.image.ImageCreator;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.structure.Display;
import tech.grasshopper.pdf.structure.PageCreator;
import tech.grasshopper.pdf.structure.TableCreator;
import tech.grasshopper.pdf.util.DateUtil;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class DetailedFeatureDisplay extends Display implements DestinationAware {

	private Feature feature;

	@Getter
	private float finalY;

	private int destinationY;

	@Override
	public void display() {

		PDPage initialPage = document.getPage(document.getNumberOfPages() - 1);
		destinationY = (int) ylocation;

		String tags = feature.getTags().stream().collect(Collectors.joining(" "));

		TableBuilder tableBuilder = Table.builder().addColumnsOfWidth(100f, 300f, 70f, 110f, 70f, 110f).borderWidth(1f)
				.borderColor(Color.LIGHT_GRAY).horizontalAlignment(HorizontalAlignment.LEFT)
				.verticalAlignment(VerticalAlignment.TOP).font(ReportFont.REGULAR_FONT)

				.addRow(Row.builder().font(ReportFont.BOLD_FONT).fontSize(14).borderWidth(0f).padding(7f)
						.add(TextCell.builder().colSpan(6).wordBreak(true).text("(F)- " + feature.getName())
								.textColor(reportConfig.getDetailedFeatureConfig().featureNameColor()).build())
						.build())

				.addRow(Row.builder().fontSize(13).font(ReportFont.ITALIC_FONT)
						.add(TextCell.builder().text(feature.getStatus().toString())
								.backgroundColor(statusColor(feature.getStatus())).build())
						.add(TextCell.builder()
								.text("DURATION - " + DateUtil.durationValue(feature.calculatedDuration()))
								.textColor(reportConfig.getDetailedFeatureConfig().durationColor()).build())

						.add(ParagraphCell.builder().lineSpacing(1.4f).rowSpan(3).paragraph(scenariosData())
								.font(ReportFont.REGULAR_FONT).build())
						.add(ImageCell.builder().rowSpan(3).image(scenariosDonut())
								.horizontalAlignment(HorizontalAlignment.CENTER)
								.verticalAlignment(VerticalAlignment.MIDDLE).build())

						.add(ParagraphCell.builder().lineSpacing(1.4f).rowSpan(3).paragraph(stepsData()).build())
						.add(ImageCell.builder().rowSpan(3).image(stepsDonut())
								.horizontalAlignment(HorizontalAlignment.CENTER)
								.verticalAlignment(VerticalAlignment.MIDDLE).build())
						.build())

				.addRow(Row.builder().fontSize(12).font(ReportFont.ITALIC_FONT)
						.add(TextCell.builder().colSpan(2)
								.text("/ " + DateUtil.formatTimeWithMillis(feature.getStartTime()) + " // "
										+ DateUtil.formatTimeWithMillis(feature.getEndTime()) + " /")
								.textColor(reportConfig.getDetailedFeatureConfig().durationColor()).build())
						.build())

				.addRow(Row.builder().fontSize(11).add(TextCell.builder().colSpan(2).text(tags).build()).build());

		TableCreator tableCreator = TableCreator.builder().tableBuilder(tableBuilder).document(document)
				.startX(CONTENT_START_X).startY(ylocation).repeatRows(4)
				.pageSupplier(PageCreator.landscapePageWithHeaderAndNumberSupplier(document)).build();
		tableCreator.displayTable();

		finalY = tableCreator.getFinalY();

		page = tableCreator.getTableStartPage();

		if (!initialPage.equals(page))
			destinationY = (int) Display.CONTENT_START_Y;

		createDestination();
	}

	@Override
	public void createDestination() {

		Destination destination = Destination.builder().yCoord(destinationY).page(page).build();
		feature.setDestination(destination);
	}

	private Paragraph scenariosData() {

		return createData("Scenarios", feature.getTotalScenarios(), feature.getPassedScenarios(),
				feature.getFailedScenarios(), feature.getSkippedScenarios());
	}

	private PDImageXObject scenariosDonut() {
		ReportDonutChart chart = new ReportDonutChart(80, 80);
		updateChartStyler(chart.getStyler());

		Map<String, Number> data = new HashMap<>();
		data.put("Passed", feature.getPassedScenarios());
		data.put("Failed", feature.getFailedScenarios());
		data.put("Skipped", feature.getSkippedScenarios());

		chart.updateData(data);
		return ImageCreator.builder().chart(chart).document(document).build().generateChartImageXObject();
	}

	private Paragraph stepsData() {

		return createData("Steps", feature.getTotalSteps(), feature.getPassedSteps(), feature.getFailedSteps(),
				feature.getSkippedSteps());
	}

	private PDImageXObject stepsDonut() {
		ReportDonutChart chart = new ReportDonutChart(80, 80);
		updateChartStyler(chart.getStyler());

		Map<String, Number> data = new HashMap<>();
		data.put("Passed", feature.getPassedSteps());
		data.put("Failed", feature.getFailedSteps());
		data.put("Skipped", feature.getSkippedSteps());

		chart.updateData(data);
		return ImageCreator.builder().chart(chart).document(document).build().generateChartImageXObject();
	}

	private void updateChartStyler(PieStyler styler) {
		styler.setSumFontSize(16f);
		styler.setDonutThickness(0.4);
		styler.setSeriesColors(
				new Color[] { reportConfig.passedColor(), reportConfig.failedColor(), reportConfig.skippedColor() });
	}

	private Paragraph createData(String header, int total, int pass, int fail, int skip) {

		return Paragraph.builder().append(StyledText.builder().fontSize(11f).text(header).build()).appendNewLine()
				.append(StyledText.builder().fontSize(10f).text("Total - ").build())
				.append(StyledText.builder().fontSize(11f).text(String.valueOf(total)).build()).appendNewLine()

				.append(StyledText.builder().fontSize(10f).text("Pass - ").color(reportConfig.passedColor()).build())
				.append(StyledText.builder().fontSize(11f).text(String.valueOf(pass)).color(reportConfig.passedColor())
						.build())
				.appendNewLine()

				.append(StyledText.builder().fontSize(10f).text("Fail - ").color(reportConfig.failedColor()).build())
				.append(StyledText.builder().fontSize(11f).text(String.valueOf(fail)).color(reportConfig.failedColor())
						.build())
				.appendNewLine()

				.append(StyledText.builder().fontSize(10f).text("Skip - ").color(reportConfig.skippedColor()).build())
				.append(StyledText.builder().fontSize(11f).text(String.valueOf(skip)).color(reportConfig.skippedColor())
						.build())
				.appendNewLine().build();
	}
}
