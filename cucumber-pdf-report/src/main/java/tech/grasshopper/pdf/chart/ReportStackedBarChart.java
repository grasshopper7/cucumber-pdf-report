package tech.grasshopper.pdf.chart;

import java.awt.Color;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.Styler.ChartTheme;

public class ReportStackedBarChart extends CategoryChart implements CustomStyler, StackedBarChartSeriesData {

	private Color[] sliceColors = new Color[] { Color.GREEN, Color.RED, Color.ORANGE };

	public ReportStackedBarChart(int width, int height) {
		super(width, height, ChartTheme.XChart);
		updateStyler();
	}

	@Override
	public void updateStyler() {
		CategoryStyler styler = getStyler();

		styler.setLegendVisible(false);
		styler.setPlotContentSize(0.95);
		styler.setChartPadding(10);
		styler.setSeriesColors(sliceColors);
		styler.setHasAnnotations(false);
		styler.setStacked(true);
		styler.setAvailableSpaceFill(0.4);
		styler.setChartBackgroundColor(Color.WHITE);
	}

	@Override
	public void updateData(int[] xData, int[] passed, int[] failed, int[] skipped) {
		addSeries("passed", xData, passed);
		addSeries("failed", xData, failed);
		addSeries("skipped", xData, skipped);
	}
}
