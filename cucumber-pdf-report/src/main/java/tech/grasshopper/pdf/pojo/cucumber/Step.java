package tech.grasshopper.pdf.pojo.cucumber;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class Step {

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
    private String errorMessage;
    private String location;
    
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
}
