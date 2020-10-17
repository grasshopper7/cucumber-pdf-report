package tech.grasshopper.pdf.component.chart;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.Styler.ChartTheme;

public class ReportBarChart extends CategoryChart implements CustomStyler, BarChartSeriesData {
	
	public ReportBarChart(int width, int height) {
		super(width, height, ChartTheme.XChart);
		updateStyler();
	}

	@Override
	public void updateStyler() {
		CategoryStyler styler = getStyler();
		
		styler.setLegendVisible(false);
		styler.setPlotContentSize(0.98);
		styler.setChartPadding(3);
		styler.setHasAnnotations(false);
		styler.setAvailableSpaceFill(0.2);
		styler.setChartBackgroundColor(Color.WHITE);
	}

	@Override
	public void updateData(List<? extends Number> data) {	
		List<Integer> xData = IntStream.rangeClosed(1, data.size()).boxed().collect(Collectors.toList());		
		addSeries("data", xData, data);
	}	
}
