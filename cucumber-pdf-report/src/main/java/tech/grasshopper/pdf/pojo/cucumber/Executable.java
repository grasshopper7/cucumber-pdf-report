package tech.grasshopper.pdf.pojo.cucumber;

import java.util.List;

import tech.grasshopper.pdf.section.details.executable.ExecutableDisplay;

public interface Executable {

	ExecutableDisplay getDisplay();

	List<String> getOutput();

	Status getStatus();

	String getErrorMessage();

	List<String> getMedia();

	void checkData();

	Feature getFeature();

	void setFeature(Feature feature);

	Scenario getScenario();

	void setScenario(Scenario scenario);
}
