package tech.grasshopper.pdf.config;

import java.awt.Color;

import lombok.Data;
import tech.grasshopper.pdf.config.DetailedConfig.DetailedFeatureConfig;
import tech.grasshopper.pdf.config.DetailedConfig.DetailedScenarioConfig;
import tech.grasshopper.pdf.config.DetailedConfig.DetailedStepHookConfig;

@Data
public class ReportConfig {

	private String passColor;
	private String failColor;
	private String skipColor;
	private boolean displayFeature = true;
	private boolean displayScenario = true;
	private boolean displayDetailed = true;

	private SummaryConfig summaryConfig = new SummaryConfig();
	private FeatureConfig featureConfig = new FeatureConfig();
	private ScenarioConfig scenarioConfig = new ScenarioConfig();
	private DetailedFeatureConfig detailedFeatureConfig = new DetailedFeatureConfig();
	private DetailedScenarioConfig detailedScenarioConfig = new DetailedScenarioConfig();
	private DetailedStepHookConfig detailedStepHookConfig = new DetailedStepHookConfig();

	public Color passedColor() {
		return createColor(passColor, Color.GREEN);
	}

	public Color failedColor() {
		return createColor(failColor, Color.RED);
	}

	public Color skippedColor() {
		return createColor(skipColor, Color.ORANGE);
	}

	static Color createColor(String hexCode, Color defaultColor) {
		try {
			return Color.decode("#" + hexCode);
		} catch (NumberFormatException e) {
			return defaultColor;
		}
	}

	@Data
	public static class FeatureConfig {
		// min 6 max 12
		private int itemcount = 8;
		private String totalColor;
		private String durationColor;

		public int getItemcount() {
			if (itemcount < 6)
				itemcount = 6;
			else if (itemcount > 12)
				itemcount = 12;
			return itemcount;
		}
		
		public Color totalColor() {
			return createColor(totalColor, Color.BLACK);
		}

		public Color durationColor() {
			return createColor(durationColor, Color.BLACK);
		}
	}

	@Data
	public static class ScenarioConfig {
		// min 8 max 15
		private int itemcount = 12;
		private String totalColor;
		private String durationColor;
		
		public int getItemcount() {
			if (itemcount < 8)
				itemcount = 8;
			else if (itemcount > 15)
				itemcount = 15;
			return itemcount;
		}
		
		public Color totalColor() {
			return createColor(totalColor, Color.BLACK);
		}

		public Color durationColor() {
			return createColor(durationColor, Color.BLACK);
		}
	}
}
