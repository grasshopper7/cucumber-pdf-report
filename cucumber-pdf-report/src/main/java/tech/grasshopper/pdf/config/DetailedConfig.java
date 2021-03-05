package tech.grasshopper.pdf.config;

import java.awt.Color;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public abstract class DetailedConfig {

	private String startEndTimeColor;
	private String tagColor;
	private String dataHeaderColor;
	private String dataBackgroundColor;
	private String totalColor;
	private String durationColor;
	private String durationBackgroundColor;

	public Color startEndTimeColor() {
		return ReportConfig.createColor(startEndTimeColor, Color.BLUE);
	}

	public Color tagColor() {
		return ReportConfig.createColor(tagColor, Color.BLACK);
	}

	public Color dataHeaderColor() {
		return ReportConfig.createColor(dataHeaderColor, Color.BLACK);
	}

	public Color dataBackgroundColor() {
		return ReportConfig.createColor(dataBackgroundColor, Color.DARK_GRAY);
	}

	public Color totalColor() {
		return ReportConfig.createColor(totalColor, Color.WHITE);
	}

	public Color durationColor() {
		return ReportConfig.createColor(durationColor, Color.BLUE);
	}

	public Color durationBackgroundColor() {
		return ReportConfig.createColor(durationBackgroundColor, Color.LIGHT_GRAY);
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class DetailedFeatureConfig extends DetailedConfig {

		private String featureNameColor;

		public Color featureNameColor() {
			return ReportConfig.createColor(featureNameColor, Color.RED);
		}
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class DetailedScenarioConfig extends DetailedConfig {

		private String featureNameColor;
		private String scenarioNameColor;
		private String stepChartBarColor;

		public Color scenarioNameColor() {
			return ReportConfig.createColor(scenarioNameColor, Color.RED);
		}

		public Color featureNameColor() {
			return ReportConfig.createColor(featureNameColor, Color.RED);
		}

		public Color stepChartBarColor() {
			return ReportConfig.createColor(stepChartBarColor, Color.BLACK);
		}
	}

	@Data
	public static class DetailedStepHookConfig {

		private String stepCount;
		private String stepTextColor;
		private String stepBackgroundColor;
		private String hookTextColor;
		private String hookBackgroundColor;
		private String durationColor;
		private String errorMsgColor;
		private String logMsgColor;
		private final int defaultCount = 15;

		public int stepCount() {
			int count = 0;
			try {
				count = Integer.parseInt(stepCount);
				if (count > defaultCount)
					return defaultCount;
			} catch (NumberFormatException e) {
				return defaultCount;
			}
			return count;
		}

		public Color stepTextColor() {
			return ReportConfig.createColor(stepTextColor, Color.BLACK);
		}

		public Color stepBackgroundColor() {
			return ReportConfig.createColor(stepBackgroundColor, Color.LIGHT_GRAY);
		}

		public Color hookTextColor() {
			return ReportConfig.createColor(hookTextColor, Color.BLACK);
		}

		public Color hookBackgroundColor() {
			return ReportConfig.createColor(hookBackgroundColor, Color.WHITE);
		}

		public Color durationColor() {
			return ReportConfig.createColor(durationColor, Color.BLACK);
		}

		public Color errorMsgColor() {
			return ReportConfig.createColor(errorMsgColor, Color.RED);
		}

		public Color logMsgColor() {
			return ReportConfig.createColor(logMsgColor, Color.GREEN);
		}
	}
}
