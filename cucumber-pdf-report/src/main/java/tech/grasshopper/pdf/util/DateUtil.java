package tech.grasshopper.pdf.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {
	
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm:ss a");
	
	private static final DateTimeFormatter dateTimeWOYearFormatter = DateTimeFormatter.ofPattern("MMM dd, h:mm:ss a");

	public static String durationValue(LocalDateTime start, LocalDateTime end) {
		Duration duration = Duration.between(start, end);
		return durationValue(duration);
	}
	
	public static String durationValue(Duration duration) {
		long minutes = duration.toMinutes();
		long seconds = duration.getSeconds() - (60 * duration.toMinutes());
		long millis = duration.toMillis() - (1000 * duration.getSeconds());				
				
		if(minutes > 0)
			return String.format("%d m %d.%03d s", minutes, seconds, millis);
		return String.format("%d.%03d s", seconds, millis);	
	}
	
	public static double duration(LocalDateTime start, LocalDateTime end) {
		return (Duration.between(start, end).toMillis() * 1.0) / 1000.0;
	}
	
	public static String formatDateTime(LocalDateTime dateTime) {
		return dateTime.format(dateTimeFormatter);
	}
	
	public static String formatDateTimeWOYear(LocalDateTime dateTime) {
		return dateTime.format(dateTimeWOYearFormatter);
	}
	
	public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
}