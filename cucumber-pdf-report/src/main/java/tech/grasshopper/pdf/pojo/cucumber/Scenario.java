package tech.grasshopper.pdf.pojo.cucumber;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.chapter.detailed.StepOrHookRow;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Scenario extends TimeDetails {

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
