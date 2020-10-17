package tech.grasshopper.pdf.chapter.summary;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.knowm.xchart.style.PieStyler;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.component.chart.ChartComponent;
import tech.grasshopper.pdf.component.chart.ChartDisplayer;
import tech.grasshopper.pdf.component.chart.ReportDonutChart;
import tech.grasshopper.pdf.data.SummaryData;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class SummaryDonutCharts extends ChartComponent {

	private SummaryData summaryData;

	@Override
	public void display() {
		summaryData = (SummaryData) displayData;
		createFeatureDonutChart();
		createScenarioDonutChart();
		createStepDonutChart();
	}

	private void createFeatureDonutChart() {
		Map<String, Number> data = createDonutChartDataMap(summaryData.getPassedFeatures(),
				summaryData.getFailedFeatures(), summaryData.getSkippedFeatures());
		createDonutChart(data, 40);
	}

	private void createScenarioDonutChart() {
		Map<String, Number> data = createDonutChartDataMap(summaryData.getPassedScenarios(),
				summaryData.getFailedScenarios(), summaryData.getSkippedScenarios());
		createDonutChart(data, 220);
	}

	private void createStepDonutChart() {
		Map<String, Number> data = createDonutChartDataMap(summaryData.getPassedSteps(), summaryData.getFailedSteps(),
				summaryData.getSkippedSteps());
		createDonutChart(data, 400);
	}

	private void createDonutChart(Map<String, Number> data, float xOffset) {
		ReportDonutChart chart = new ReportDonutChart(160, 160);
		updateChartStyler(chart.getStyler());
		chart.updateData(data);

		ChartDisplayer.builder().document(document).content(content).chart(chart).xBottomLeft(xOffset).yBottomLeft(430)
				.build().display();
	}

	private void updateChartStyler(PieStyler styler) {
		styler.setSumFontSize(20f);
		styler.setDonutThickness(0.5);
		styler.setSeriesColors(
				new Color[] { reportConfig.passedColor(), reportConfig.failedColor(), reportConfig.skippedColor() });
	}

	private Map<String, Number> createDonutChartDataMap(Number passed, Number failed, Number skipped) {
		Map<String, Number> data = new HashMap<>();
		data.put("Passed", passed);
		data.put("Failed", failed);
		data.put("Skipped", skipped);
		return data;
	}
}
