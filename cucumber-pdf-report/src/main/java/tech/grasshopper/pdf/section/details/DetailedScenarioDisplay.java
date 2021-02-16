package tech.grasshopper.pdf.section.details;

import java.awt.Color;
import java.awt.Font;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.knowm.xchart.style.CategoryStyler;
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
import tech.grasshopper.pdf.chart.ReportBarChart;
import tech.grasshopper.pdf.chart.ReportDonutChart;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.destination.DestinationAware;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.image.ImageCreator;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;
import tech.grasshopper.pdf.structure.Display;
import tech.grasshopper.pdf.structure.PageCreator;
import tech.grasshopper.pdf.structure.TableCreator;
import tech.grasshopper.pdf.util.DateUtil;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class DetailedScenarioDisplay extends Display implements DestinationAware {

	private Scenario scenario;
	private Feature feature;

	@Getter
	private float finalY;

	private int destinationY;

	@Override
	public void display() {

		PDPage initialPage = document.getPage(document.getNumberOfPages() - 1);
		destinationY = (int) ylocation;

		String tags = scenario.getTags().stream().collect(Collectors.joining(" "));

		TableBuilder tableBuilder = Table.builder().addColumnsOfWidth(100f, 180f, 320f, 60f, 100f).borderWidth(1f)
				.borderColor(Color.LIGHT_GRAY).horizontalAlignment(HorizontalAlignment.LEFT)
				.verticalAlignment(VerticalAlignment.TOP).font(ReportFont.REGULAR_FONT)

				.addRow(Row.builder().font(ReportFont.BOLD_FONT).fontSize(13).borderWidth(0f)
						.add(TextCell.builder().colSpan(5).wordBreak(true).text("(S)- " + scenario.getName())
								.textColor(reportConfig.getDetailedScenarioConfig().scenarioNameColor()).build())
						.build())

				.addRow(Row.builder().fontSize(13).add(TextCell.builder().text(scenario.getStatus().toString()).build())
						.add(TextCell.builder()
								.text("DURATION - " + DateUtil.durationValue(scenario.calculatedDuration())).build())

						.add(ImageCell.builder().rowSpan(4).image(stepsChart()).build())

						.add(ParagraphCell.builder().lineSpacing(1.5f).rowSpan(4).paragraph(stepsData()).build())
						.add(ImageCell.builder().rowSpan(4).image(stepsDonut())
								.horizontalAlignment(HorizontalAlignment.CENTER)
								.verticalAlignment(VerticalAlignment.MIDDLE).build())
						.build())

				.addRow(Row.builder().fontSize(12)
						.add(TextCell.builder().colSpan(2)
								.text("/ " + DateUtil.formatTimeWithMillis(scenario.getStartTime()) + " // "
										+ DateUtil.formatTimeWithMillis(scenario.getEndTime()) + " /")
								.build())
						.build())

				.addRow(Row.builder().fontSize(11)
						.add(TextCell.builder().colSpan(2).wordBreak(true).text(feature.getName())
								.textColor(reportConfig.getDetailedFeatureConfig().featureNameColor()).build())
						.build())

				.addRow(Row.builder().fontSize(11).add(TextCell.builder().colSpan(2).text(tags).build()).build());

		TableCreator tableCreator = TableCreator.builder().tableBuilder(tableBuilder).document(document)
				.startX(CONTENT_START_X).startY(ylocation).repeatRows(5)
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
		scenario.setDestination(destination);
	}

	private Paragraph stepsData() {

		return createData("Steps", scenario.getTotalSteps(), scenario.getPassedSteps(), scenario.getFailedSteps(),
				scenario.getSkippedSteps());
	}

	private PDImageXObject stepsDonut() {

		ReportDonutChart chart = new ReportDonutChart(80, 80);
		updateChartStyler(chart.getStyler());

		Map<String, Number> data = new HashMap<>();
		data.put("Passed", scenario.getPassedSteps());
		data.put("Failed", scenario.getFailedSteps());
		data.put("Skipped", scenario.getSkippedSteps());

		chart.updateData(data);
		return ImageCreator.builder().chart(chart).document(document).build().generateChartImageXObject();
	}

	private void updateChartStyler(PieStyler styler) {

		styler.setSumFontSize(16f);
		styler.setDonutThickness(0.4);
		styler.setSeriesColors(
				new Color[] { reportConfig.passedColor(), reportConfig.failedColor(), reportConfig.skippedColor() });
	}

	private PDImageXObject stepsChart() {

		ReportBarChart chart = new ReportBarChart(320, 110);

		List<Double> data = scenario.getSteps().stream().map(s -> DateUtil.duration(s.getStartTime(), s.getEndTime()))
				.collect(Collectors.toList());

		/*
		 * if (data.size() > MAX_CHART_COUNT) data = data.subList(0, MAX_CHART_COUNT);
		 */

		updateBarChartStyler(chart.getStyler(), data);
		chart.updateData(data);

		return ImageCreator.builder().chart(chart).document(document).build().generateChartImageXObject();
	}

	private void updateBarChartStyler(CategoryStyler styler, List<Double> data) {

		double maxVal = data.stream().max(Comparator.naturalOrder()).get();
		// styler.setYAxisMax(Math.floor(maxVal) + 1);
		if (maxVal <= 0.25)
			styler.setYAxisMax(0.5);
		else if (maxVal <= 0.5)
			styler.setYAxisMax(0.75);
		else
			styler.setYAxisMax(Math.floor(maxVal) + 1);
		styler.setSeriesColors(new Color[] { reportConfig.getDetailedScenarioConfig().stepChartBarColor() });

		Font axisFont = new Font(Font.DIALOG, Font.PLAIN, 8);
		styler.setAxisTickLabelsFont(axisFont);
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
