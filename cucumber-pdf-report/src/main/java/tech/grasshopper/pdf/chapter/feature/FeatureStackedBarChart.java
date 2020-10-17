package tech.grasshopper.pdf.chapter.feature;

import java.awt.Color;
import java.util.stream.IntStream;

import org.knowm.xchart.style.CategoryStyler;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.component.chart.ChartComponent;
import tech.grasshopper.pdf.component.chart.ChartDisplayer;
import tech.grasshopper.pdf.component.chart.ReportStackedBarChart;
import tech.grasshopper.pdf.data.FeatureData;
import tech.grasshopper.pdf.pojo.cucumber.Feature;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class FeatureStackedBarChart extends ChartComponent {
	
	private int itemCount;
	private int fromXData;
	private int toXData;
		
	@Override
	public void display() {
		createBarChart();
	}

	private void createBarChart() {
		int[] passed = new int[itemCount];
		int[] failed = new int[itemCount];
		int[] skipped = new int[itemCount];
		int[] xData = new int[itemCount];
		xData = IntStream.rangeClosed(fromXData + 1, fromXData + itemCount).toArray();
		
		ReportStackedBarChart chart = new ReportStackedBarChart(520, 300);	
		chart.setYAxisTitle("# of Scenarios");
		updateBarChartStyler(chart.getStyler());
		createStackedBarChartData(passed, failed, skipped);
		chart.updateData(xData, passed, failed, skipped);
		
		ChartDisplayer.builder().document(document).content(content).chart(chart).xBottomLeft(40).yBottomLeft(460)
				.build().display();
	}
	
	private void updateBarChartStyler(CategoryStyler styler) {
		styler.setSeriesColors(
				new Color[] { reportConfig.passedColor(), reportConfig.failedColor(), reportConfig.skippedColor() });
	}
	
	private void createStackedBarChartData(int[] passed, int[] failed, int[] skipped) {
		FeatureData featureData = (FeatureData) displayData;
		for(int i =0 ; i < featureData.getFeatures().size() ; i++) {
			Feature feature = featureData.getFeatures().get(i);
			passed[i] = feature.getPassedScenarios();
			failed[i] = feature.getFailedScenarios();
			skipped[i] = feature.getSkippedScenarios();
		}
	}
}
