package tech.grasshopper.pdf.pojo.cucumber;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.section.details.executable.ExecutableDisplay;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public abstract class ExecutableEntity extends BaseEntity {

	protected Feature feature;
	protected Scenario scenario;

	@Default
	protected List<String> output = new ArrayList<>();
	@Default
	protected List<String> media = new ArrayList<>();

	protected Status status;
	@Default
	protected String errorMessage = "";
	@Default
	protected String location = "";

	public abstract ExecutableDisplay getDisplay();
}
