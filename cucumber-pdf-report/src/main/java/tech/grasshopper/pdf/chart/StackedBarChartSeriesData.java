package tech.grasshopper.pdf.chart;

public interface StackedBarChartSeriesData {
	
	void updateData(int[] xData, int[] passed, int[] failed, int[] skipped);
}
