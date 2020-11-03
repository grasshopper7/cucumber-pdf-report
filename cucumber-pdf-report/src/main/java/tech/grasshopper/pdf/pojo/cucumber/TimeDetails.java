package tech.grasshopper.pdf.pojo.cucumber;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class TimeDetails {

	protected LocalDateTime startTime;
	protected LocalDateTime endTime;

	public Duration calculatedDuration() {

		return Duration.between(startTime, endTime);
	}
}
