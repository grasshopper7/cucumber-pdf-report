package tech.grasshopper.pdf.section.scenario;

import java.awt.Color;
import java.util.stream.IntStream;

import org.knowm.xchart.style.CategoryStyler;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.chart.ReportStackedBarChart;
import tech.grasshopper.pdf.data.ScenarioData;
import tech.grasshopper.pdf.image.ImageCreator;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;
import tech.grasshopper.pdf.structure.Display;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ScenarioStackedBarChart extends Display {

	private int maxScenarios;
	private int fromXData;
	private int toXData;

	@Override
	public void display() {
		createBarChart();
	}

	private void createBarChart() {

		int[] passed = new int[toXData - fromXData];
		int[] failed = new int[toXData - fromXData];
		int[] skipped = new int[toXData - fromXData];
		int[] xData = new int[toXData - fromXData];
		xData = IntStream.rangeClosed(fromXData + 1, toXData).toArray();

		ReportStackedBarChart chart = new ReportStackedBarChart(600, 200);
		chart.setYAxisTitle("# of Steps");
		updateBarChartStyler(chart.getStyler());
		createStackedBarChartData(passed, failed, skipped);
		chart.updateData(xData, passed, failed, skipped);

		ImageCreator.builder().document(document).chart(chart).content(content).build()
				.generateAndDisplayChartImage(120, 350);
	}

	private void updateBarChartStyler(CategoryStyler styler) {
		styler.setSeriesColors(
				new Color[] { reportConfig.passedColor(), reportConfig.failedColor(), reportConfig.skippedColor() });

		styler.setAvailableSpaceFill(0.4 * (toXData - fromXData) / 10);
	}

	private void createStackedBarChartData(int[] passed, int[] failed, int[] skipped) {
		ScenarioData scenarioData = (ScenarioData) displayData;
		for (int i = 0; i < scenarioData.getScenarios().size(); i++) {
			Scenario scenario = scenarioData.getScenarios().get(i);
			passed[i] = scenario.getPassedSteps();
			failed[i] = scenario.getFailedSteps();
			skipped[i] = scenario.getSkippedSteps();
		}
	}
}