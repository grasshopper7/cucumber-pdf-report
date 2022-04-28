package tech.grasshopper.pdf.section.details;

import java.awt.Color;
import java.awt.Font;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
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
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.optimizer.TextSanitizer;
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

	private static final PDFont FEATURE_FONT = ReportFont.ITALIC_FONT;
	private static final int FEATURE_FONT_SIZE = 10;
	private static final PDFont SCENARIO_FONT = ReportFont.BOLD_FONT;
	private static final int SCENARIO_FONT_SIZE = 11;
	private static final PDFont TAGS_FONT = ReportFont.REGULAR_FONT;
	private static final int TAGS_FONT_SIZE = 10;

	private static final float STATUS_COLUMN_WIDTH = 75f;
	private static final float DURATION_COLUMN_WIDTH = 185f;
	private static final float STEP_DURATION_BAR_COLUMN_WIDTH = 340f;
	private static final float STEP_COUNT_COLUMN_WIDTH = 60f;
	private static final float STEP_CHART_COLUMN_WIDTH = 100f;
	private static final float FEATURE_PADDING = 4f;
	private static final float SCENARIO_PADDING = 5f;
	private static final float SCENARIO_LEFT_PADDING = 25f;

	private static final TextLengthOptimizer featureNameTextOptimizer = TextLengthOptimizer.builder().font(FEATURE_FONT)
			.fontsize(FEATURE_FONT_SIZE)
			.availableSpace((STATUS_COLUMN_WIDTH + DURATION_COLUMN_WIDTH) - (2 * FEATURE_PADDING)).maxLines(2).build();

	private static final TextLengthOptimizer tagsTextOptimizer = TextLengthOptimizer.builder().font(TAGS_FONT)
			.fontsize(TAGS_FONT_SIZE)
			.availableSpace((STATUS_COLUMN_WIDTH + DURATION_COLUMN_WIDTH) - (2 * DEFAULT_PADDING)).maxLines(3).build();

	@Override
	public void display() {

		TextSanitizer sanitizer = TextSanitizer.builder().build();
		PDPage initialPage = document.getPage(document.getNumberOfPages() - 1);
		destinationY = (int) ylocation;

		String tags = scenario.getTags().stream().collect(Collectors.joining(" "));

		TableBuilder nameTableBuilder = Table.builder()
				.addColumnsOfWidth(STATUS_COLUMN_WIDTH + DURATION_COLUMN_WIDTH + STEP_DURATION_BAR_COLUMN_WIDTH
						+ STEP_COUNT_COLUMN_WIDTH + STEP_CHART_COLUMN_WIDTH)
				.horizontalAlignment(HorizontalAlignment.LEFT).verticalAlignment(VerticalAlignment.TOP)

				.addRow(Row.builder().font(SCENARIO_FONT).fontSize(SCENARIO_FONT_SIZE).borderWidth(0f)
						.padding(SCENARIO_PADDING)
						.add(TextCell.builder().wordBreak(true).paddingLeft(SCENARIO_LEFT_PADDING)
								.text(sanitizer.sanitizeText(scenario.getName()))
								.textColor(reportConfig.getDetailedScenarioConfig().scenarioNameColor()).build())
						.build());

		TableCreator nameTableCreator = TableCreator.builder().tableBuilder(nameTableBuilder).document(document)
				.startX(CONTENT_START_X).startY(ylocation).endY(DETAILED_CONTENT_END_Y).splitRow(true).repeatRows(0)
				.pageSupplier(PageCreator.builder().document(document).build()
						.landscapePageWithHeaderAndNumberSupplier(DetailedSection.SECTION_TITLE))
				.build();

		nameTableCreator.displayTable();
		ylocation = nameTableCreator.getFinalY();

		page = nameTableCreator.getTableStartPage();

		if (!initialPage.equals(page))
			destinationY = (int) Display.CONTENT_START_Y;

		createDestination();

		TableBuilder tableBuilder = Table.builder()
				.addColumnsOfWidth(STATUS_COLUMN_WIDTH, DURATION_COLUMN_WIDTH, STEP_DURATION_BAR_COLUMN_WIDTH,
						STEP_COUNT_COLUMN_WIDTH, STEP_CHART_COLUMN_WIDTH)
				.borderWidth(1f).borderColor(Color.GRAY).horizontalAlignment(HorizontalAlignment.LEFT)
				.verticalAlignment(VerticalAlignment.TOP).font(ReportFont.REGULAR_FONT)

				.addRow(Row.builder().fontSize(10).font(ReportFont.ITALIC_FONT)
						.add(TextCell.builder().text(scenario.getStatus().toString())
								.backgroundColor(statusColor(scenario.getStatus())).build())
						.add(TextCell.builder()
								.text("DURATION - " + DateUtil.durationValue(scenario.calculatedDuration()))
								.textColor(reportConfig.getDetailedScenarioConfig().durationColor())
								.backgroundColor(reportConfig.getDetailedScenarioConfig().durationBackgroundColor())
								.build())

						.add(ImageCell.builder().rowSpan(4).image(stepsChart()).build())

						.add(ParagraphCell.builder().lineSpacing(1.5f).rowSpan(4).paragraph(stepsData())
								.font(ReportFont.REGULAR_FONT)
								.backgroundColor(reportConfig.getDetailedScenarioConfig().dataBackgroundColor())
								.build())
						.add(ImageCell.builder().rowSpan(4).image(stepsDonut())
								.horizontalAlignment(HorizontalAlignment.CENTER)
								.verticalAlignment(VerticalAlignment.MIDDLE).build())
						.build())

				.addRow(Row.builder().fontSize(10).font(ReportFont.ITALIC_FONT)
						.add(TextCell.builder().colSpan(2)
								.text("/ " + DateUtil.formatTimeWithMillis(scenario.getStartTime()) + " // "
										+ DateUtil.formatTimeWithMillis(scenario.getEndTime()) + " /")
								.textColor(reportConfig.getDetailedScenarioConfig().startEndTimeColor()).build())
						.build())

				.addRow(Row.builder().fontSize(FEATURE_FONT_SIZE).font(FEATURE_FONT).add(TextCell.builder().colSpan(2)
						.padding(FEATURE_PADDING).wordBreak(true)
						.text(sanitizer.sanitizeText(featureNameTextOptimizer.optimizeTextLines(feature.getName())))
						.textColor(reportConfig.getDetailedScenarioConfig().featureNameColor()).build()).build())

				.addRow(Row.builder().fontSize(TAGS_FONT_SIZE).font(TAGS_FONT)
						.add(TextCell.builder().colSpan(2).wordBreak(true)
								.text(sanitizer.sanitizeText(tagsTextOptimizer.optimizeTextLines(tags)))
								.textColor(reportConfig.getDetailedScenarioConfig().tagColor()).build())
						.build());

		TableCreator tableCreator = TableCreator.builder().tableBuilder(tableBuilder).document(document)
				.startX(CONTENT_START_X).startY(ylocation).endY(DETAILED_CONTENT_END_Y).repeatRows(4)
				.pageSupplier(PageCreator.builder().document(document).build()
						.landscapePageWithHeaderAndNumberSupplier(DetailedSection.SECTION_TITLE))
				.build();
		tableCreator.displayTable();

		finalY = tableCreator.getFinalY();
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

		ReportBarChart chart = new ReportBarChart((int) STEP_DURATION_BAR_COLUMN_WIDTH, 110);

		List<Double> data = scenario.getSteps().stream().map(s -> DateUtil.duration(s.getStartTime(), s.getEndTime()))
				.collect(Collectors.toList());

		if (data.size() > reportConfig.getDetailedStepHookConfig().stepCount())
			data = data.subList(0, reportConfig.getDetailedStepHookConfig().stepCount());

		updateBarChartStyler(chart.getStyler(), data);
		chart.updateData(data);

		return ImageCreator.builder().chart(chart).document(document).build().generateChartImageXObject();
	}

	private void updateBarChartStyler(CategoryStyler styler, List<Double> data) {

		double maxVal = data.stream().max(Comparator.naturalOrder()).get();

		if (maxVal <= 0.1)
			styler.setYAxisMax(0.15);
		else if (maxVal <= 0.25)
			styler.setYAxisMax(0.35);
		else if (maxVal <= 0.5)
			styler.setYAxisMax(0.65);
		else
			styler.setYAxisMax(Math.floor(maxVal) + 1);

		styler.setSeriesColors(new Color[] { reportConfig.getDetailedScenarioConfig().stepChartBarColor() });

		styler.setAvailableSpaceFill(0.4 * data.size() / 10);

		Font axisFont = new Font(Font.DIALOG, Font.PLAIN, 8);
		styler.setAxisTickLabelsFont(axisFont);
	}

	private Paragraph createData(String header, int total, int pass, int fail, int skip) {

		return Paragraph.builder()
				.append(StyledText.builder().fontSize(11f).text(header)
						.color(reportConfig.getDetailedScenarioConfig().dataHeaderColor()).build())
				.appendNewLine().append(createDataTitle("Total", reportConfig.getDetailedScenarioConfig().totalColor()))
				.append(createDataValue(total, reportConfig.getDetailedScenarioConfig().totalColor())).appendNewLine()

				.append(createDataTitle("Pass", reportConfig.passedColor()))
				.append(createDataValue(pass, reportConfig.passedColor())).appendNewLine()

				.append(createDataTitle("Fail", reportConfig.failedColor()))
				.append(createDataValue(fail, reportConfig.failedColor())).appendNewLine()

				.append(createDataTitle("Skip", reportConfig.skippedColor()))
				.append(createDataValue(skip, reportConfig.skippedColor())).appendNewLine().build();
	}

	private StyledText createDataTitle(String text, Color color) {
		return StyledText.builder().fontSize(10f).text(text + " - ").color(color).build();
	}

	private StyledText createDataValue(int value, Color color) {
		return StyledText.builder().fontSize(11f).text(String.valueOf(value)).color(color).build();
	}
}
