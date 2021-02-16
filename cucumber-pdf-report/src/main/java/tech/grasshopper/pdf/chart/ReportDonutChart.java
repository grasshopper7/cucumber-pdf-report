package tech.grasshopper.pdf.chart;

import java.awt.Color;
import java.util.Map;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.PieStyler.ClockwiseDirectionType;
import org.knowm.xchart.style.Styler.ChartTheme;

public class ReportDonutChart extends PieChart implements CustomStyler, DonutChartSeriesData {

	private Color[] sliceColors = new Color[] { Color.GREEN, Color.RED, Color.ORANGE };

	public ReportDonutChart(int width, int height) {
		super(width, height, ChartTheme.XChart);
		updateStyler();
	}

	@Override
	public void updateStyler() {
		PieStyler styler = getStyler();

		styler.setLegendVisible(false);
		styler.setPlotContentSize(1.0);
		styler.setPlotBorderVisible(false);
		styler.setChartPadding(0);
		styler.setClockwiseDirectionType(ClockwiseDirectionType.CLOCKWISE);
		styler.setSeriesColors(sliceColors);
		styler.setHasAnnotations(false);
		styler.setDefaultSeriesRenderStyle(PieSeriesRenderStyle.Donut);
		styler.setSumVisible(true);
		styler.setDecimalPattern("#");
	}

	@Override
	public void updateData(Map<String, Number> data) {

		addSeries("Passed", data.getOrDefault("Passed", 0));
		addSeries("Failed", data.getOrDefault("Failed", 0));
		addSeries("Skipped", data.getOrDefault("Skipped", 0));
	}
}
