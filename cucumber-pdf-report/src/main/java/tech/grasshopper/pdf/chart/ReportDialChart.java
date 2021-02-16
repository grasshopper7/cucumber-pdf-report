package tech.grasshopper.pdf.chart;

import org.knowm.xchart.DialChart;
import org.knowm.xchart.style.DialStyler;
import org.knowm.xchart.style.Styler.ChartTheme;

public class ReportDialChart extends DialChart implements CustomStyler, DialChartSeriesData {

	public ReportDialChart(int width, int height) {
		super(width, height, ChartTheme.XChart);
		updateStyler();
	}

	@Override
	public void updateStyler() {
		DialStyler styler = getStyler();
		
		styler.setLegendVisible(false);
		styler.setPlotContentSize(1.0);
		styler.setPlotBorderVisible(false);
		styler.setChartPadding(0);
		styler.setHasAnnotations(true);
	}

	@Override
	public void updateData(String name, double value, int display) {
		addSeries(name, value, display + " %");
	}
}
