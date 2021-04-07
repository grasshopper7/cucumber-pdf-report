package tech.grasshopper.pdf.pojo.cucumber;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.annotation.Annotation;
import tech.grasshopper.pdf.destination.Destination;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public abstract class NonExecutableEntity extends BaseEntity {

	@Default
	protected List<String> tags = new ArrayList<>();

	@Default
	protected List<Annotation> annotations = new ArrayList<>();
	protected Destination destination;

	public void addAnnotation(Annotation annotation) {
		annotations.add(annotation);
	}
}
