package tech.grasshopper.pdf.pojo.cucumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.annotation.Annotation;
import tech.grasshopper.pdf.destination.Destination;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public abstract class NonExecutable extends BaseEntity {

	@Default
	protected List<String> tags = new ArrayList<>();

	@Default
	protected List<Annotation> annotations = new ArrayList<>();
	protected Destination destination;

	@Default
	protected Map<String, Object> additionalData = new HashMap();

	public void addAnnotation(Annotation annotation) {
		annotations.add(annotation);
	}

	public void addAdditionalData(String key, Object value) {
		additionalData.put(key, value);
	}

	public Optional<Object> getAdditionalDataValue(String key) {
		return Optional.ofNullable(additionalData.get(key));
	}
}
