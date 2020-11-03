package tech.grasshopper.pdf.pojo.cucumber;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

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
    private List<PDAnnotationLink> annotations = new ArrayList<>();
    private PDPageXYZDestination destination;
    
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
}
