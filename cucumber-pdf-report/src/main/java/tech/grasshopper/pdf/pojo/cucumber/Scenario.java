package tech.grasshopper.pdf.pojo.cucumber;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.annotation.Annotation;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.exception.PdfReportException;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Scenario extends TimeDetails {

	private String name;
	
	private Feature feature;

	@Default
	private List<Hook> before = new ArrayList<>();
	@Default
	private List<Step> steps = new ArrayList<>();
	@Default
	private List<Hook> after = new ArrayList<>();
	@Default
	private List<String> tags = new ArrayList<>();

	@Default
	private List<Annotation> annotations = new ArrayList<>();
	private Destination destination;

	@Default
	private int passedSteps = 0;
	@Default
	private int failedSteps = 0;
	@Default
	private int skippedSteps = 0;
	@Default
	private int totalSteps = 0;

	private Status status;

	public List<Hook> getBeforeAfterHooks() {
		List<Hook> hooks = new ArrayList<>();
		hooks.addAll(before);
		hooks.addAll(after);
		return hooks;
	}

	public List<Executable> getStepsAndHooks() {
		List<Executable> executables = new ArrayList<>();

		before.forEach(h -> executables.add(h));
		steps.forEach(s -> {
			s.getBefore().forEach(h -> executables.add(h));
			executables.add(s);
			s.getAfter().forEach(h -> executables.add(h));
		});
		after.forEach(h -> executables.add(h));

		return executables;
	}

	public void addAnnotation(Annotation annotation) {
		annotations.add(annotation);
	}

	public void checkData() {

		if (name == null || name.isEmpty())
			throw new PdfReportException("Scenario name is null or empty.");

		if (feature == null)
			throw new PdfReportException("No feature present for scenario - " + getName());

		if (steps == null || steps.isEmpty())
			throw new PdfReportException("No steps present for scenario - " + getName());

		if (status == null)
			throw new PdfReportException("No status present for scenario - " + getName());

		checkTimeData();
	}
}
