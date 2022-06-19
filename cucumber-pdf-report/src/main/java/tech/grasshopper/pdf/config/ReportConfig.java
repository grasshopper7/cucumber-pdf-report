package tech.grasshopper.pdf.config;

import java.awt.Color;

import lombok.Data;
import tech.grasshopper.pdf.config.DetailedConfig.DetailedFeatureConfig;
import tech.grasshopper.pdf.config.DetailedConfig.DetailedScenarioConfig;
import tech.grasshopper.pdf.config.DetailedConfig.DetailedStepHookConfig;
import tech.grasshopper.pdf.config.verify.VerifyDisplayConfiguration;
import tech.grasshopper.pdf.config.verify.VerifyHookConfiguration;

@Data
public class ReportConfig {

	private String passColor;
	private String failColor;
	private String skipColor;

	private boolean displayFeature = true;
	private boolean displayScenario = true;
	private boolean displayDetailed = true;
	private boolean displayAttached = true;
	private boolean displayExpanded = false;

	private DashboardConfig dashboardConfig = new DashboardConfig();
	private FeatureConfig featureConfig = new FeatureConfig();
	private ScenarioConfig scenarioConfig = new ScenarioConfig();
	private SummaryConfig summaryConfig = new SummaryConfig();
	private TagConfig tagConfig = new TagConfig();

	private DetailedFeatureConfig detailedFeatureConfig = new DetailedFeatureConfig();
	private DetailedScenarioConfig detailedScenarioConfig = new DetailedScenarioConfig();
	private DetailedStepHookConfig detailedStepHookConfig = new DetailedStepHookConfig();

	public void verifyAndUpdate() {

		VerifyDisplayConfiguration.builder().reportConfig(this).build().verify();

		VerifyHookConfiguration.builder().reportConfig(this).build().verify();
	}

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

	static int createCount(String itemCount, int defaultCount) {
		int count = 0;
		try {
			count = Integer.parseInt(itemCount);
			if (count > defaultCount)
				return defaultCount;
		} catch (NumberFormatException e) {
			return defaultCount;
		}
		return count;
	}

	public void mergeParameterConfig(ParameterConfig parameterConfig) {
		parameterConfig.mergeConfigurations(this);
	}

	@Data
	public static class FeatureConfig {

		private String featureCount;
		private String totalColor;
		private String durationColor;
		private final int defaultCount = 10;

		public int featureCount() {
			return createCount(featureCount, defaultCount);
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

		private String scenarioCount;
		private String totalColor;
		private String durationColor;
		private final int defaultCount = 10;

		public int scenarioCount() {
			return createCount(scenarioCount, defaultCount);
		}

		public Color totalColor() {
			return createColor(totalColor, Color.BLACK);
		}

		public Color durationColor() {
			return createColor(durationColor, Color.BLACK);
		}
	}

	@Data
	public static class SummaryConfig {

		private String totalColor;
		private String durationColor;

		public int dataCount() {
			return 20;
		}

		public Color totalColor() {
			return createColor(totalColor, Color.BLACK);
		}

		public Color durationColor() {
			return createColor(durationColor, Color.BLACK);
		}
	}

	@Data
	public static class TagConfig {

		private String totalColor;

		public int dataCount() {
			return 20;
		}

		public Color totalColor() {
			return createColor(totalColor, Color.BLACK);
		}
	}
}
