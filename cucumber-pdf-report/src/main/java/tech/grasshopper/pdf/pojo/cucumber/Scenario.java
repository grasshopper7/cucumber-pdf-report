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
import tech.grasshopper.pdf.chapter.detailed.StepOrHookRow;

@Data
@Builder
public class Scenario {

	private String name;
	private Feature feature;
	@Default
    private List<Hook> before = new ArrayList<>();
	@Default
    private List<Step> steps  = new ArrayList<>();
	@Default
    private List<Hook> after = new ArrayList<>();
	@Default
    private List<String> tags = new ArrayList<>();
    
    @Default
    private List<PDAnnotationLink> annotations = new ArrayList<>();
    private PDPageXYZDestination destination;

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
    
    public List<Hook> getBeforeAfterHooks() {
    	List<Hook> hooks = new ArrayList<>();
    	hooks.addAll(before);
    	hooks.addAll(after);
    	return hooks;
    }
    
    public List<StepOrHookRow> getAllStepsAndHooks() {
    	List<StepOrHookRow> stepsAndHooks = new ArrayList<>();
    	
    	before.forEach(h -> stepsAndHooks.add(StepOrHookRow.createStepOrHook(h)));
    	steps.forEach(s -> {
    		s.getBefore().forEach(h -> stepsAndHooks.add(StepOrHookRow.createStepOrHook(h)));
    		stepsAndHooks.add(StepOrHookRow.createStepOrHook(s));
    		s.getAfter().forEach(h -> stepsAndHooks.add(StepOrHookRow.createStepOrHook(h)));
    	});
    	after.forEach(h -> stepsAndHooks.add(StepOrHookRow.createStepOrHook(h)));
    	
    	return stepsAndHooks;
    }
}