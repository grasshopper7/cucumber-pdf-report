package tech.grasshopper.pdf.config.verify;

import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.config.DetailedConfig.DetailedStepHookConfig;

@SuperBuilder
public class VerifyHookConfiguration extends VerifyConfiguration {

	private static final Logger logger = Logger.getLogger(VerifyHookConfiguration.class.getName());

	@Override
	public void verify() {
		DetailedStepHookConfig detailedStepHookConfig = reportConfig.getDetailedStepHookConfig();

		if (detailedStepHookConfig.isSkipScenarioHooks() && !detailedStepHookConfig.isSkipHooks()) {
			logger.log(Level.INFO,
					"'skipScenarioBeforeHooks' and 'skipScenarioAfterHooks' configuration values will be ignored as 'skipScenarioHooks' is set to true.");

			detailedStepHookConfig.setSkipScenarioBeforeHooks(true);
			detailedStepHookConfig.setSkipScenarioAfterHooks(true);
		}
		if (detailedStepHookConfig.isSkipStepHooks() && !detailedStepHookConfig.isSkipHooks()) {
			logger.log(Level.INFO,
					"'skipStepBeforeHooks' and 'skipStepAfterHooks' configuration values will be ignored as 'skipStepHooks' is set to true.");

			detailedStepHookConfig.setSkipStepBeforeHooks(true);
			detailedStepHookConfig.setSkipStepAfterHooks(true);
		}
		// This has to come at the end to override previous settings if wrong.
		if (detailedStepHookConfig.isSkipHooks()) {
			logger.log(Level.INFO,
					"All other hook configuration values will be ignored as 'skipHooks' is set to true.");

			detailedStepHookConfig.setSkipScenarioHooks(true);
			detailedStepHookConfig.setSkipScenarioBeforeHooks(true);
			detailedStepHookConfig.setSkipScenarioAfterHooks(true);
			detailedStepHookConfig.setSkipStepHooks(true);
			detailedStepHookConfig.setSkipStepBeforeHooks(true);
			detailedStepHookConfig.setSkipStepAfterHooks(true);
		}
	}
}
