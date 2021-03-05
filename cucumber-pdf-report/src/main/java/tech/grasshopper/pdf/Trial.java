package tech.grasshopper.pdf;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.vandeseer.easytable.util.PdfUtil;

import tech.grasshopper.pdf.data.ReportData;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;
import tech.grasshopper.pdf.pojo.cucumber.Status;
import tech.grasshopper.pdf.pojo.cucumber.Step;
import tech.grasshopper.pdf.util.TextUtil;

public class Trial {

	public static void main(String[] args) {

		List<String> logs = new ArrayList<>();
		logs.add("MOUNISH");
		logs.add("SAHA");
		logs.add(
				"ROX MY WORLD ROX MY WORLD ROX MY WORLDROX MY WORLD ROX MY WORLD ROX MY WORLD ROX MY WORLD ROX MY WORLD ROX MY WORLD ROX MY WORLD ROX MY WORLD ROX MY WORLD ROX MY WORLD ROX MY WORLD ROX MY WORLD ROX MY WORLD ROX MY WORLD ROX MY WORLD ROX MY WORLD ROX MY WORLD ROX MY WORLD");
		logs.add("MOUNISH");
		logs.add("SAHA");
		logs.add("ROX MY WORLD");

		String error = "java.lang.AssertionError: expected:\u003cfalse\u003e but was:\u003ctrue\u003e\r\n\tat org.junit.Assert.fail(Assert.java:88)\r\n\tat org.junit.Assert.failNotEquals(Assert.java:834)\r\n\tat org.junit.Assert.assertEquals(Assert.java:118)\r\n\tat org.junit.Assert.assertEquals(Assert.java:144)\r\n\tat stepdefs.Stepdefs.raiseExcep(Stepdefs.java:110)\r\n\tat âœ½.Raise exception(classpath:stepdefs/exceptions.feature:12)\r\n";

		String docString = "   Hello there how r u?\r\n" + "\r\n" + "   Doing great.\r\n" + "Whats new?\r\n" + "\r\n"
				+ "Nothing much.";

		List<String> medias = new ArrayList<>();
		medias.add("src/main/resources/embedded1.png");
		medias.add("src/main/resources/embedded2.png");
		medias.add("src/main/resources/embedded1.png");
		medias.add("src/main/resources/embedded2.png");
		medias.add("src/main/resources/embedded1.png");
		medias.add("src/main/resources/embedded2.png");
		medias.add("src/main/resources/embedded1.png");
		medias.add("src/main/resources/embedded2.png");

		Step step1 = Step.builder().keyword("Given").name("Step One").startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusSeconds(25)).status(Status.PASSED).docString(docString).output(logs)
				.errorMessage(error).media(medias).build();
		Step step2 = Step.builder().keyword("When").name("Step Two").startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusSeconds(25)).status(Status.FAILED).build();
		Step step3 = Step.builder().keyword("Then").name("Step Three").startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusSeconds(25)).status(Status.SKIPPED).build();
		List<Step> steps = new ArrayList<>();
		steps.add(step1);
		steps.add(step2);
		steps.add(step3);

		List<String> scenTags = new ArrayList<>();
		scenTags.add("ScenTag1");
		scenTags.add("ScenTag2");
		Scenario scenario = Scenario.builder().name(
				"Scenario One Scenario One Scenario One Scenario One Scenario One Scenario One Scenario One Scenario One Scenario One Scenario One Scenario One Scenario One Scenario One")
				.steps(steps).startTime(LocalDateTime.now()).endTime(LocalDateTime.now().plusSeconds(75))
				.status(Status.SKIPPED).tags(scenTags).build();
		List<Scenario> scenarios = new ArrayList<>();
		scenarios.add(scenario);

		List<String> featTags = new ArrayList<>();
		featTags.add("FeatureTag1");
		featTags.add("FeatureTag2");
		featTags.add("FeatureTag3");
		featTags.add("FeatureTag4");
		featTags.add("FeatureTag5");
		featTags.add("FeatureTag6");
		featTags.add("FeatureTag7");
		featTags.add("FeatureTag8");
		featTags.add("FeatureTag9");

		/*
		 * featTags.add("FeatureTag10"); featTags.add("FeatureTag11");
		 * featTags.add("FeatureTag12"); featTags.add("FeatureTag13");
		 * featTags.add("FeatureTag14"); featTags.add("FeatureTag15");
		 * featTags.add("FeatureTag16"); featTags.add("FeatureTag17");
		 * featTags.add("FeatureTag18"); featTags.add("FeatureTag19");
		 * featTags.add("FeatureTag20"); featTags.add("FeatureTag21");
		 * featTags.add("FeatureTag22"); featTags.add("FeatureTag23");
		 * featTags.add("FeatureTag24"); featTags.add("FeatureTag25");
		 */

		Feature feature = Feature.builder().name(
				"Feature One Feature One Feature One Feature One Feature One Feature One Feature One Feature One Feature One Feature One")
				.scenarios(scenarios).startTime(LocalDateTime.now()).endTime(LocalDateTime.now().plusSeconds(75))
				.status(Status.PASSED).tags(featTags).build();
		List<Feature> features = new ArrayList<>();
		features.add(feature);

		ReportData data = ReportData.builder().features(features).build();

		PDFCucumberReport report = new PDFCucumberReport(data, "target");
		report.createReport();
	}
}
