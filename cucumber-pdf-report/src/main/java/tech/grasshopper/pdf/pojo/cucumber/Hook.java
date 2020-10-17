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
public class Hook {

	@Default
	private List<String> output = new ArrayList<>();
    
    private Status status;
    private String errorMessage;
    private String location;
    
    private HookType hookType;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Duration getDuration() {
		return Duration.between(startTime, endTime);
	}
    
    public static enum HookType {
    	BEFORE, AFTER, BEFORE_STEP, AFTER_STEP;
    }
}
