package tech.grasshopper.pdf.pojo.cucumber;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.exception.PdfReportException;

@Data
@SuperBuilder
public abstract class TimeDetails {

	protected LocalDateTime startTime;
	protected LocalDateTime endTime;

	public Duration calculatedDuration() {

		return Duration.between(startTime, endTime);
	}

	public void checkTimeData() {

		if (startTime == null)
			throw new PdfReportException("Start Time not present for " + this.getClass().getName() + " - " + getName());

		if (endTime == null)
			throw new PdfReportException("End Time not present for " + this.getClass().getName() + " - " + getName());

		if (startTime.compareTo(endTime) > 0)
			throw new PdfReportException(
					"Start Time is later than End time for " + this.getClass().getName() + " - " + getName());
	}

	abstract String getName();
}
