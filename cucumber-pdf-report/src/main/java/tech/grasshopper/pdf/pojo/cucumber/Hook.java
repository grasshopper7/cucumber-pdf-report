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
public class Hook extends TimeDetails {

	@Default
	private List<String> output = new ArrayList<>();
	@Default
	private List<String> media = new ArrayList<>();
    
    private Status status;
    @Default
    private String errorMessage="";
    @Default
    private String location="";
    
    private HookType hookType;
    
    
    public static enum HookType {
    	BEFORE, AFTER, BEFORE_STEP, AFTER_STEP;
    }
}
