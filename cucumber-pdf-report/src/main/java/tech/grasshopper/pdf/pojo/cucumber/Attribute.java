package tech.grasshopper.pdf.pojo.cucumber;

import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
public abstract class Attribute {

	public Attribute(String name) {
		this.name = name;
	}

	protected String name;

	protected Status status;

	@Default
	private int passedScenarios = 0;
	@Default
	private int failedScenarios = 0;
	@Default
	private int skippedScenarios = 0;
	@Default
	private int totalScenarios = 0;

	@Default
	private int passedFeatures = 0;
	@Default
	private int failedFeatures = 0;
	@Default
	private int skippedFeatures = 0;
	@Default
	private int totalFeatures = 0;
}
