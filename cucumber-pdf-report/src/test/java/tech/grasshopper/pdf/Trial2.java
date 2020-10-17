package tech.grasshopper.pdf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import tech.grasshopper.pdf.config.ReportConfig;
import tech.grasshopper.pdf.pojo.cucumber.Status;
import tech.grasshopper.pdf.util.DateUtil;

public class Trial2 {

	public static void main(String[] args) throws FileNotFoundException {
		
		List<Status> statuses = Arrays.asList(Status.PASSED, Status.PASSED, Status.AMBIGUOUS);
		System.out.println(Collections.max(statuses));
		
		LocalDateTime dt1 = LocalDateTime.now();
		LocalDateTime dt2 = LocalDateTime.now().plus(55, ChronoField.MILLI_OF_DAY.getBaseUnit());
		
		System.out.println(Duration.between(dt1, dt2));
		System.out.println(DateUtil.durationValue(Duration.between(dt1, dt2)));
		
		/*
		 * DestinationDetail d1 =
		 * ScenarioDestinationDetail.builder().featureName("feat").name("scen").build();
		 * DestinationDetail d2 =
		 * ScenarioDestinationDetail.builder().featureName("feat").name("scen").build();
		 * 
		 * System.out.println(d1 == d2); System.out.println(d2.equals(d1));
		 */
		
		Yaml yaml = new Yaml(new Constructor(ReportConfig.class));
		InputStream inputStream = new FileInputStream("src/test/resources/pdf-config.yaml");
		/*
		 * Map<String, Object> obj = yaml.load(inputStream); System.out.println(obj);
		 */
		ReportConfig report = yaml.load(inputStream);
		System.out.println(report);
	}

}
