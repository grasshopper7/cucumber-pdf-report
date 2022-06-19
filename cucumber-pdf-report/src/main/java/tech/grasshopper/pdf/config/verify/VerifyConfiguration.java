package tech.grasshopper.pdf.config.verify;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.config.ReportConfig;

@SuperBuilder
public abstract class VerifyConfiguration {

	protected ReportConfig reportConfig;

	public abstract void verify();
}
