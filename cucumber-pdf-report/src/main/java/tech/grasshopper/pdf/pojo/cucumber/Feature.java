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
public class Feature extends TimeDetails {

	@Default
	private List<Scenario> scenarios = new ArrayList<>();
	private String name;
	@Default
	private List<String> tags = new ArrayList<>();

	@Default
	private List<Annotation> annotations = new ArrayList<>();
	private Destination destination;

	@Default
	private int passedScenarios = 0;
	@Default
	private int failedScenarios = 0;
	@Default
	private int skippedScenarios = 0;
	@Default
	private int totalScenarios = 0;

	@Default
	private int passedSteps = 0;
	@Default
	private int failedSteps = 0;
	@Default
	private int skippedSteps = 0;
	@Default
	private int totalSteps = 0;

	private Status status;

	public void addAnnotation(Annotation annotation) {
		annotations.add(annotation);
	}

	public void checkData() {

		if (name == null || name.isEmpty())
			throw new PdfReportException("Feature name is null or empty.");

		if (scenarios == null || scenarios.isEmpty())
			throw new PdfReportException("No scenarios present for feature - " + getName());

		if (status == null)
			throw new PdfReportException("No status present for feature - " + getName());

		checkTimeData();
	}
}
