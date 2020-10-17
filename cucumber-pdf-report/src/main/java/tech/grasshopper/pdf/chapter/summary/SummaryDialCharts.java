package tech.grasshopper.pdf.chapter.summary;

import org.knowm.xchart.style.DialStyler;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.component.chart.ChartComponent;
import tech.grasshopper.pdf.component.chart.ChartDisplayer;
import tech.grasshopper.pdf.component.chart.ReportDialChart;
import tech.grasshopper.pdf.data.SummaryData;
import tech.grasshopper.pdf.util.NumberUtil;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class SummaryDialCharts extends ChartComponent {
		
	private SummaryData summaryData;
	
	@Override
	public void display() {
		summaryData = (SummaryData) displayData;
		createFeaturesDialChart();
		createScenariosDialChart();
		createStepsDialChart();
	}	
	
	private void createFeaturesDialChart() {
		double dialValue = NumberUtil.divideAndRound(summaryData.getPassedFeatures(), summaryData.getTotalFeatures());
		int dialDisplay = NumberUtil.divideToPercent(summaryData.getPassedFeatures(), summaryData.getTotalFeatures());
		double[] featureRange = reportConfig.getSummaryConfig().getDial().featureRange();
		createDialChart(dialValue, dialDisplay, 40, featureRange);
	}

	private void createScenariosDialChart() {
		double dialValue = NumberUtil.divideAndRound(summaryData.getPassedScenarios(), summaryData.getTotalScenarios());
		int dialDisplay = NumberUtil.divideToPercent(summaryData.getPassedScenarios(), summaryData.getTotalScenarios());
		double[] scenarioRange = reportConfig.getSummaryConfig().getDial().scenarioRange();
		createDialChart(dialValue, dialDisplay, 220, scenarioRange);
	}

	private void createStepsDialChart() {
		double dialValue = NumberUtil.divideAndRound(summaryData.getPassedSteps(), summaryData.getTotalSteps());
		int dialDisplay = NumberUtil.divideToPercent(summaryData.getPassedSteps(), summaryData.getTotalSteps());
		double[] stepRange = reportConfig.getSummaryConfig().getDial().stepRange();
		createDialChart(dialValue, dialDisplay, 400, stepRange);
	}
	
	private void createDialChart(double dialValue, int dialDisplay, float xOffset, double[] range) {
		ReportDialChart chart = new ReportDialChart(160, 160);
		updateChartStyler(chart.getStyler(), range);
		chart.updateData("Pass %", dialValue, dialDisplay);
		
		ChartDisplayer.builder().document(document).content(content).chart(chart).xBottomLeft(xOffset)
				.yBottomLeft(130).build().display();
	}
	
	private void updateChartStyler(DialStyler styler, double[] range) {
		
		styler.setGreenColor(reportConfig.getSummaryConfig().getDial().badColor());
		styler.setGreenFrom(0);
		styler.setGreenTo(range[0]);
		styler.setNormalColor(reportConfig.getSummaryConfig().getDial().averageColor());
		styler.setNormalFrom(range[0]);
		styler.setNormalTo(range[1]);
		styler.setRedColor(reportConfig.getSummaryConfig().getDial().goodColor());
		styler.setRedFrom(range[1]);
		styler.setRedTo(1);
	}
}
