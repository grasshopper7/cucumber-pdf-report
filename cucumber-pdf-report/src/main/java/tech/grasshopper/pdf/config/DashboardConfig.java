package tech.grasshopper.pdf.config;

import java.awt.Color;

import lombok.Data;
import tech.grasshopper.pdf.util.NumberUtil;

@Data
public class DashboardConfig {

	private String title = "Cucumber Report";
	private String titleColor;
	private String dateColor;
	private String timeColor;
	private DialConfig dial = new DialConfig();
	private String dataBackgroundColor;

	public Color dataBackgroundColor() {
		return ReportConfig.createColor(dataBackgroundColor, Color.DARK_GRAY);
	}

	public Color titleColor() {
		return ReportConfig.createColor(titleColor, Color.BLACK);
	}

	public Color dateColor() {
		return ReportConfig.createColor(dateColor, Color.BLUE);
	}

	public Color timeColor() {
		return ReportConfig.createColor(timeColor, Color.RED);
	}

	@Data
	public static class DialConfig {
		private String featureRanges = "0.60 0.85";
		private String scenarioRanges = "0.60 0.85";
		private String stepRanges = "0.60 0.85";
		private String badColor;
		private String averageColor;
		private String goodColor;

		public double[] featureRange() {
			return ranges(featureRanges);
		}

		public double[] scenarioRange() {
			return ranges(scenarioRanges);
		}

		public double[] stepRange() {
			return ranges(stepRanges);
		}

		private double[] ranges(String rangeStr) {
			try {
				String[] range = rangeStr.split(" ");
				return new double[] { NumberUtil.divideAndRound(Integer.parseInt(range[0]), 100),
						NumberUtil.divideAndRound(Integer.parseInt(range[1]), 100) };
			} catch (Exception e) {
				return new double[] { 0.60, 0.85 };
			}
		}

		public Color badColor() {
			return ReportConfig.createColor(badColor, Color.RED);
		}

		public Color averageColor() {
			return ReportConfig.createColor(averageColor, Color.ORANGE);
		}

		public Color goodColor() {
			return ReportConfig.createColor(goodColor, Color.GREEN);
		}
	}
}
