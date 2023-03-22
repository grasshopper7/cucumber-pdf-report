package tech.grasshopper.pdf.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParameterConfig {

	private String title;
	private String titleColor;

	private String passColor;
	private String failColor;
	private String skipColor;

	private String displayTag;
	private String displayDevice;
	private String displayAuthor;
	private String displayFeature;
	private String displayScenario;
	private String displayDetailed;
	private String displayExpanded;
	private String displayAttached;

	private String skipHooks;
	private String skipScenarioHooks;
	private String skipStepHooks;

	public void mergeConfigurations(ReportConfig reportConfig) {
		if (validStringParameter(title))
			reportConfig.getDashboardConfig().setTitle(title);
		if (validStringParameter(titleColor))
			reportConfig.getDashboardConfig().setTitleColor(titleColor);

		if (validStringParameter(passColor))
			reportConfig.setPassColor(passColor);
		if (validStringParameter(failColor))
			reportConfig.setFailColor(failColor);
		if (validStringParameter(skipColor))
			reportConfig.setSkipColor(skipColor);

		if (validBooleanParameter(displayTag))
			reportConfig.setDisplayTag(Boolean.parseBoolean(displayTag));
		if (validBooleanParameter(displayDevice))
			reportConfig.setDisplayDevice(Boolean.parseBoolean(displayDevice));
		if (validBooleanParameter(displayAuthor))
			reportConfig.setDisplayAuthor(Boolean.parseBoolean(displayAuthor));
		if (validBooleanParameter(displayFeature))
			reportConfig.setDisplayFeature(Boolean.parseBoolean(displayFeature));
		if (validBooleanParameter(displayScenario))
			reportConfig.setDisplayScenario(Boolean.parseBoolean(displayScenario));
		if (validBooleanParameter(displayDetailed))
			reportConfig.setDisplayDetailed(Boolean.parseBoolean(displayDetailed));
		if (validBooleanParameter(displayAttached))
			reportConfig.setDisplayAttached(Boolean.parseBoolean(displayAttached));
		if (validBooleanParameter(displayExpanded))
			reportConfig.setDisplayExpanded(Boolean.parseBoolean(displayExpanded));

		if (validBooleanParameter(skipScenarioHooks))
			reportConfig.getDetailedStepHookConfig().setSkipScenarioHooks(Boolean.parseBoolean(skipScenarioHooks));
		if (validBooleanParameter(skipStepHooks))
			reportConfig.getDetailedStepHookConfig().setSkipStepHooks(Boolean.parseBoolean(skipStepHooks));
		if (validBooleanParameter(skipHooks))
			reportConfig.getDetailedStepHookConfig().setSkipHooks(Boolean.parseBoolean(skipHooks));
	}

	private boolean validStringParameter(String value) {
		if (value == null || value.isEmpty())
			return false;
		return true;
	}

	private boolean validBooleanParameter(String value) {
		if (value == null || value.isEmpty() || !(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")))
			return false;
		return true;
	}
}
