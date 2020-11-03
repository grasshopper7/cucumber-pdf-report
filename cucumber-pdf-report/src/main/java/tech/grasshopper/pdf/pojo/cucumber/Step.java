package tech.grasshopper.pdf.pojo.cucumber;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Step extends TimeDetails {

    private String name;
    private String keyword;
    
    @Default
    private List<Row> rows = new ArrayList<>();
    private String docString;
    
    @Default
    private List<Hook> before = new ArrayList<>();
    @Default
    private List<Hook> after = new ArrayList<>();
    
    @Default
    private List<String> output = new ArrayList<>();
	@Default
	private List<String> media = new ArrayList<>();
    
    private Status status;
    @Default
    private String errorMessage="";
    @Default
    private String location="";

    
    public List<Hook> getBeforeAfterHooks() {
    	List<Hook> hooks = new ArrayList<>();
    	hooks.addAll(before);
    	hooks.addAll(after);
    	return hooks;
    }
    
    public void addBeforeStepHook(Hook hook) {
    	before.add(hook);
    }
    
    public void addAfterStepHook(Hook hook) {
    	after.add(hook);
    }
}
