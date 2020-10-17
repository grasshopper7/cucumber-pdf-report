package tech.grasshopper.pdf.util;

public class NumberUtil {

	public static double divideAndRound(int arg1, int arg2) {	
		double r = (arg1 * 1.0)/arg2;
		return Math.round(r * 100.0) / 100.0;
	}
	
	public static int divideToPercent(int arg1, int arg2) {	
		return (arg1 * 100)/arg2;
	}
}
