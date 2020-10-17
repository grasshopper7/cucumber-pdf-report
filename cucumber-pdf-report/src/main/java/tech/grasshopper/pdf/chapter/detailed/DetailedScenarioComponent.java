package tech.grasshopper.pdf.chapter.detailed;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.PieStyler;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.component.Component;
import tech.grasshopper.pdf.component.chart.ChartComponent;
import tech.grasshopper.pdf.component.chart.ChartDisplayer;
import tech.grasshopper.pdf.component.chart.ReportBarChart;
import tech.grasshopper.pdf.component.chart.ReportDonutChart;
import tech.grasshopper.pdf.component.decorator.BackgroundDecorator;
import tech.grasshopper.pdf.component.text.MultiLineTextComponent;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.destination.DestinationAware;
import tech.grasshopper.pdf.optimizer.TextContentSanitizer;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;
import tech.grasshopper.pdf.pojo.report.Text;
import tech.grasshopper.pdf.util.DateUtil;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class DetailedScenarioComponent extends ChartComponent implements DestinationAware {

	public static final int CONTENT_HEIGHT = 195;
	public static final int MAX_CHART_COUNT = 15;

	private Scenario scenario;
	private Feature feature;
	private int startHeight;

	private static final PDFont FEATURE_NAME_FONT = PDType1Font.HELVETICA;
	private static final int FEATURE_NAME_FONT_SIZE = 12;
	private static final PDFont SCENARIO_NAME_FONT = PDType1Font.HELVETICA_BOLD;
	private static final int SCENARIO_NAME_FONT_SIZE = 15;

	private static final int NAME_WIDTH = 400;
	private static final int PADDING = 5;

	private final TextLengthOptimizer featureNameTextOptimizer = TextLengthOptimizer.builder().font(FEATURE_NAME_FONT)
			.fontsize(FEATURE_NAME_FONT_SIZE).spaceWidth(NAME_WIDTH - 2 * PADDING).build();

	private final TextLengthOptimizer scenarioNameTextOptimizer = TextLengthOptimizer.builder().font(SCENARIO_NAME_FONT)
			.fontsize(SCENARIO_NAME_FONT_SIZE).spaceWidth(NAME_WIDTH - 2 * PADDING).build();

	private final TextContentSanitizer textSanitizerFeature = TextContentSanitizer.builder().font(FEATURE_NAME_FONT)
			.build();

	private final TextContentSanitizer textSanitizerScenario = TextContentSanitizer.builder().font(SCENARIO_NAME_FONT)
			.build();

	@Override
	public void display() {

		createHeader();
		createDuration();
		createStepsChart();
		createStepsData();
		createStepsDonut();
	}

	@Override
	public Destination createDestination() {
		Destination destination = Destination.builder().name(scenario.getName()).yCoord(startHeight - 30).page(page)
				.build();
		scenario.setDestination(destination.createPDPageDestination());
		return destination;
	}

	private void createHeader() {
		List<Text> texts = new ArrayList<>();

		texts.add(
				Text.builder().textColor(reportConfig.getDetailedScenarioConfig().featureNameColor())
						.font(FEATURE_NAME_FONT).fontSize(FEATURE_NAME_FONT_SIZE).xoffset(50).yoffset(startHeight - 25)
						.text(featureNameTextOptimizer
								.optimizeText("(F) - " + textSanitizerFeature.sanitizeText(feature.getName())))
						.build());

		texts.add(
				Text.builder().textColor(reportConfig.getDetailedScenarioConfig().scenarioNameColor())
						.font(SCENARIO_NAME_FONT).fontSize(SCENARIO_NAME_FONT_SIZE).xoffset(50)
						.yoffset(startHeight - 45)
						.text(scenarioNameTextOptimizer
								.optimizeText("(S) - " + textSanitizerScenario.sanitizeText(scenario.getName())))
						.build());

		texts.add(Text.builder().textColor(reportConfig.getDetailedScenarioConfig().startEndTimeColor())
				.font(PDType1Font.HELVETICA_OBLIQUE).fontSize(12).xoffset(50).yoffset(startHeight - 60)
				.text("//  " + DateUtil.formatDateTimeWOYear(scenario.getStartTime()) + "  //  "
						+ DateUtil.formatDateTimeWOYear(scenario.getEndTime()) + "  //")
				.build());

		Collections.sort(scenario.getTags());
		String tags = "@Tags - " + scenario.getTags().stream().collect(Collectors.joining(" "));
		texts.add(Text.builder().font(PDType1Font.HELVETICA).fontSize(11)
				.textColor(reportConfig.getDetailedScenarioConfig().tagColor()).xoffset(50).yoffset(startHeight - 75)
				.text(featureNameTextOptimizer.optimizeText(textSanitizerFeature.sanitizeText(tags))).build());

		MultiLineTextComponent.builder().content(content).texts(texts).build().display();
	}

	private void createDuration() {
		List<Text> texts = new ArrayList<>();

		texts.add(Text.builder().textColor(reportConfig.getDetailedScenarioConfig().durationColor())
				.font(PDType1Font.HELVETICA_BOLD_OBLIQUE).fontSize(10).xoffset(450).yoffset(startHeight - 35)
				.text("Duration").build());
		texts.add(Text.builder().textColor(reportConfig.getDetailedScenarioConfig().durationColor())
				.font(PDType1Font.HELVETICA_BOLD_OBLIQUE).fontSize(11).xoffset(450).yoffset(startHeight - 55)
				.text(DateUtil.durationValue(scenario.getStartTime(), scenario.getEndTime())).build());

		Component component = MultiLineTextComponent.builder().content(content).texts(texts).build();
		component = BackgroundDecorator.builder().component(component).content(content)
				.containerColor(reportConfig.getDetailedScenarioConfig().durationBackgroundColor())
				.xContainerBottomLeft(445).yContainerBottomLeft(startHeight - 65).containerWidth(100)
				.containerHeight(45).build();
		component.display();
	}

	private void createStepsChart() {
		ReportBarChart chart = new ReportBarChart(330, 105);

		List<Double> data = scenario.getSteps().stream().map(s -> DateUtil.duration(s.getStartTime(), s.getEndTime()))
				.collect(Collectors.toList());

		if (data.size() > MAX_CHART_COUNT)
			data = data.subList(0, MAX_CHART_COUNT);

		updateBarChartStyler(chart.getStyler(), data);
		chart.updateData(data);

		ChartDisplayer.builder().document(document).content(content).chart(chart).xBottomLeft(50)
				.yBottomLeft(startHeight - 185).build().display();
	}

	private void createStepsData() {
		List<Text> texts = new ArrayList<>();

		texts.add(Text.builder().textColor(reportConfig.getDetailedScenarioConfig().dataHeaderColor())
				.font(PDType1Font.HELVETICA_BOLD).fontSize(11).xoffset(395).yoffset(startHeight - 95).text("Steps")
				.build());
		texts.add(Text.builder().textColor(reportConfig.getDetailedScenarioConfig().totalColor())
				.font(PDType1Font.HELVETICA_BOLD).fontSize(9).xoffset(395).yoffset(startHeight - 115)
				.text("Total " + scenario.getTotalSteps()).build());
		texts.add(Text.builder().textColor(reportConfig.passedColor()).font(PDType1Font.HELVETICA_BOLD).fontSize(9)
				.xoffset(395).yoffset(startHeight - 135).text("Pass " + scenario.getPassedSteps()).build());
		texts.add(Text.builder().textColor(reportConfig.failedColor()).font(PDType1Font.HELVETICA_BOLD).fontSize(9)
				.xoffset(395).yoffset(startHeight - 155).text("Fail " + scenario.getFailedSteps()).build());
		texts.add(Text.builder().textColor(reportConfig.skippedColor()).font(PDType1Font.HELVETICA_BOLD).fontSize(9)
				.xoffset(395).yoffset(startHeight - 175).text("Skip " + scenario.getSkippedSteps()).build());

		Component component = MultiLineTextComponent.builder().content(content).texts(texts).build();
		component = BackgroundDecorator.builder().component(component).content(content)
				.containerColor(reportConfig.getDetailedScenarioConfig().dataBackgroundColor())
				.xContainerBottomLeft(390).yContainerBottomLeft(startHeight - 180).containerWidth(50)
				.containerHeight(100).build();
		component.display();
	}

	private void createStepsDonut() {
		ReportDonutChart chart = new ReportDonutChart(100, 100);
		updateDonutChartStyler(chart.getStyler());

		Map<String, Number> data = new HashMap<>();
		data.put("Passed", scenario.getPassedSteps());
		data.put("Failed", scenario.getFailedSteps());
		data.put("Skipped", scenario.getSkippedSteps());

		chart.updateData(data);
		ChartDisplayer.builder().document(document).content(content).chart(chart).xBottomLeft(450)
				.yBottomLeft(startHeight - 180).build().display();
	}

	private void updateDonutChartStyler(PieStyler styler) {
		styler.setSumFontSize(16f);
		styler.setDonutThickness(0.4);
		styler.setSeriesColors(
				new Color[] { reportConfig.passedColor(), reportConfig.failedColor(), reportConfig.skippedColor() });
	}

	private void updateBarChartStyler(CategoryStyler styler, List<Double> data) {
		double maxVal = data.stream().max(Comparator.naturalOrder()).get();
		//styler.setYAxisMax(Math.floor(maxVal) + 1);
		if(maxVal <= 0.25)
			styler.setYAxisMax(0.5);
		else if(maxVal <= 0.5)
			styler.setYAxisMax(0.75);
		else
			styler.setYAxisMax(Math.floor(maxVal) + 1);
		styler.setSeriesColors(new Color[] { reportConfig.getDetailedScenarioConfig().stepChartBarColor() });
	}
}
