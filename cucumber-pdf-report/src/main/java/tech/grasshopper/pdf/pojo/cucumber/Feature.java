package tech.grasshopper.pdf.pojo.cucumber;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class Feature {

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
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    public Duration getDuration() {
		return Duration.between(startTime, endTime);
	}
    
}
