package tech.grasshopper.pdf.pojo.cucumber;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Data
@Builder
@AllArgsConstructor
public class Tag {

	public Tag(String name) {
		this.name = name;
	}

	private String name;

	private Status status;

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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
