package tech.grasshopper.pdf.config.verify;

import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class VerifyDisplayConfiguration extends VerifyConfiguration {

	private static final Logger logger = Logger.getLogger(VerifyDisplayConfiguration.class.getName());

	@Override
	public void verify() {
		if (reportConfig.isDisplayDetailed()) {
			if (reportConfig.isDisplayExpanded() && reportConfig.isDisplayAttached()) {
				reportConfig.setDisplayExpanded(false);
				logger.log(Level.WARNING,
						"Media display as attachment (displayAttached) and expanded (displayExpanded) both set to true. Only one can be selected. Defaulting to attachment display.");
			}
		} else {
			// No media display if no detailed display selected
			if (reportConfig.isDisplayExpanded() || reportConfig.isDisplayAttached()) {
				logger.log(Level.INFO,
						"Detailed section display is not set to true, no attachment or expanded media display will be available.");

				reportConfig.setDisplayExpanded(false);
				reportConfig.setDisplayAttached(false);
			}
		}
	}
}
