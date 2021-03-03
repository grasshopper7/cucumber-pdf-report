package tech.grasshopper.pdf.data;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SummaryData implements DisplayData {

	private LocalDateTime testRunStartTime;
	private LocalDateTime testRunEndTime;

	private int passedFeatures;
	private int failedFeatures;
	private int skippedFeatures;
	private int totalFeatures;

	private int passedScenarios;
	private int failedScenarios;
	private int skippedScenarios;
	private int totalScenarios;

	private int passedSteps;
	private int failedSteps;
	private int skippedSteps;
	private int totalSteps;

	public Duration getTestRunDuration() {
		return Duration.between(testRunStartTime, testRunEndTime);
	}
}
