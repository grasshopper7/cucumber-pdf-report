package tech.grasshopper.pdf.component.chart;

public interface StackedBarChartSeriesData {
	
	void updateData(int[] xData, int[] passed, int[] failed, int[] skipped);
}
