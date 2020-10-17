package tech.grasshopper.pdf;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tech.grasshopper.pdf.data.ReportData;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.pojo.cucumber.Hook;
import tech.grasshopper.pdf.pojo.cucumber.Row;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;
import tech.grasshopper.pdf.pojo.cucumber.Status;
import tech.grasshopper.pdf.pojo.cucumber.Step;

public class PdfReportTest2 {

	public static void main(String[] args) {

		Hook hook1 = Hook.builder().status(Status.PASSED).location("stepdefs.Stepdefs.before()")
				.startTime(LocalDateTime.now().plusSeconds(1))
				.endTime(LocalDateTime.now().plusSeconds(5).plus(456, ChronoField.MILLI_OF_DAY.getBaseUnit())).build();
		Hook hook2 = Hook.builder().status(Status.PASSED).location("stepdefs.Stepdefs.before()")
				.startTime(LocalDateTime.now().plusSeconds(1))
				.endTime(LocalDateTime.now().plusSeconds(3).plus(654, ChronoField.MILLI_OF_DAY.getBaseUnit())).build();

		List<Hook> beforeHooks = new ArrayList<>();
		beforeHooks.add(hook1);
		beforeHooks.add(hook2);

		List<Hook> afterHooks = new ArrayList<>();
		afterHooks.add(hook1);
		afterHooks.add(hook2);

		List<Step> steps = new ArrayList<>();
		Step feat1Scen1Step1 = Step.builder().keyword("Given").name("Step 1 S1 text").status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(1))
				.endTime(LocalDateTime.now().plusSeconds(1).plus(111, ChronoField.MILLI_OF_DAY.getBaseUnit()))
				.before(beforeHooks).build();
		Step feat1Scen1Step2 = Step.builder().keyword("Given").name("Step 2 S1 text").status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(2)).endTime(LocalDateTime.now().plusSeconds(5)).build();
		Step feat1Scen1Step3 = Step.builder().keyword("When").name("Step 3 S1 text").status(Status.FAILED)
				.startTime(LocalDateTime.now().plusSeconds(3)).endTime(LocalDateTime.now().plusSeconds(4)).build();
		Step feat1Scen1Step4 = Step.builder().keyword("Then").name("Step 4 S1 text").status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(4))
				.endTime(LocalDateTime.now().plusSeconds(5).plus(500, ChronoField.MILLI_OF_DAY.getBaseUnit()))
				.before(beforeHooks).after(afterHooks).build();
		Step feat1Scen1Step5 = Step.builder().keyword("Given").name("Step 5 S1 text").status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(1)).endTime(LocalDateTime.now().plusSeconds(5)).build();
		Step feat1Scen1Step6 = Step.builder().keyword("Given").name("Step 6 S1 text").status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(2)).endTime(LocalDateTime.now().plusSeconds(5)).build();
		Step feat1Scen1Step7 = Step.builder().keyword("When").name("Step 7 S1 text").status(Status.FAILED)
				.startTime(LocalDateTime.now().plusSeconds(3)).endTime(LocalDateTime.now().plusSeconds(4)).build();
		Step feat1Scen1Step8 = Step.builder().keyword("Then").name("Step 8 S1 text").status(Status.SKIPPED)
				.startTime(LocalDateTime.now().plusSeconds(4))
				.endTime(LocalDateTime.now().plusSeconds(5).plus(250, ChronoField.MILLI_OF_DAY.getBaseUnit())).build();
		Step feat1Scen1Step9 = Step.builder().keyword("Given").name("Step 9 S1 text").status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(1)).endTime(LocalDateTime.now().plusSeconds(5)).build();
		Step feat1Scen1Step10 = Step.builder().keyword("Given").name("Step 10 S1 text").status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(2)).endTime(LocalDateTime.now().plusSeconds(5)).build();
		Step feat1Scen1Step11 = Step.builder().keyword("When").name("Step 11 S1 text").status(Status.FAILED)
				.startTime(LocalDateTime.now().plusSeconds(3)).endTime(LocalDateTime.now().plusSeconds(4))
				.after(afterHooks).build();
		Step feat1Scen1Step12 = Step.builder().keyword("Then").name("Step 12 S1 text").status(Status.SKIPPED)
				.startTime(LocalDateTime.now().plusSeconds(4))
				.endTime(LocalDateTime.now().plusSeconds(5).plus(987, ChronoField.MILLI_OF_DAY.getBaseUnit())).build();

		steps.add(feat1Scen1Step1);
		steps.add(feat1Scen1Step2);

		steps.add(feat1Scen1Step3);
		steps.add(feat1Scen1Step4);
		steps.add(feat1Scen1Step5);
		steps.add(feat1Scen1Step6);

		steps.add(feat1Scen1Step7);
		steps.add(feat1Scen1Step8);
		steps.add(feat1Scen1Step9);
		steps.add(feat1Scen1Step10);
		steps.add(feat1Scen1Step11);
		steps.add(feat1Scen1Step12);

		Scenario feat1Scen1 = Scenario.builder().name("Scenario 1 Feature 1").steps(steps).status(Status.FAILED)
				.startTime(LocalDateTime.now().plusSeconds(1)).endTime(LocalDateTime.now().plusSeconds(5))
				.before(beforeHooks).after(afterHooks).build();

		steps = new ArrayList<>();
		Step feat1Scen2Step1 = Step.builder().keyword("Given").name("Step 1 S2 text").status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(1)).endTime(LocalDateTime.now().plusSeconds(5)).build();
		Step feat1Scen2Step2 = Step.builder().keyword("Given").name("Step 2 S2 text").status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(2)).endTime(LocalDateTime.now().plusSeconds(5)).build();
		Step feat1Scen2Step3 = Step.builder().keyword("When").name("Step 3 S2 text").status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(3)).endTime(LocalDateTime.now().plusSeconds(4)).build();

		steps.add(feat1Scen2Step1);
		steps.add(feat1Scen2Step2);
		steps.add(feat1Scen2Step3);

		Scenario feat1Scen2 = Scenario.builder().name("Scenario 2 Feature 1").steps(steps).status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(5)).endTime(LocalDateTime.now().plusSeconds(10)).build();

		steps = new ArrayList<>();
		Step feat1Scen3Step1 = Step.builder().keyword("Given").name("Step 1 S3 text").status(Status.SKIPPED)
				.startTime(LocalDateTime.now().plusSeconds(1)).endTime(LocalDateTime.now().plusSeconds(5)).build();
		Step feat1Scen3Step2 = Step.builder().keyword("Given").name("Step 2 S3 text").status(Status.SKIPPED)
				.startTime(LocalDateTime.now().plusSeconds(2)).endTime(LocalDateTime.now().plusSeconds(5)).build();
		Step feat1Scen3Step3 = Step.builder().keyword("When").name("Step 3 S3 text").status(Status.SKIPPED)
				.startTime(LocalDateTime.now().plusSeconds(3)).endTime(LocalDateTime.now().plusSeconds(4)).build();

		steps.add(feat1Scen3Step1);
		steps.add(feat1Scen3Step2);
		steps.add(feat1Scen3Step3);

		Scenario feat1Scen3 = Scenario.builder().name("Scenario 3 Feature 1").steps(steps).status(Status.SKIPPED)
				.startTime(LocalDateTime.now().plusSeconds(10))
				.endTime(LocalDateTime.now().plusSeconds(12).plus(753, ChronoField.MILLI_OF_DAY.getBaseUnit())).build();

		List<Scenario> scenarios = new ArrayList<>();
		scenarios.add(feat1Scen1);
		scenarios.add(feat1Scen2);
		scenarios.add(feat1Scen3);

		Feature feature1 = Feature.builder().name("Feature 1").scenarios(scenarios).status(Status.FAILED)
				.startTime(LocalDateTime.now().plusSeconds(1))
				.endTime(LocalDateTime.now().plusSeconds(20).plus(864, ChronoField.MILLI_OF_DAY.getBaseUnit())).build();

		List<Feature> features = new ArrayList<>();
		features.add(feature1);

		// ---------------------
		steps = new ArrayList<>();
		Step feat2Scen1Step1 = Step.builder().keyword("Given").name("Step 1 F2 text").status(Status.SKIPPED)
				.startTime(LocalDateTime.now().plusSeconds(1)).endTime(LocalDateTime.now().plusSeconds(5)).build();

		steps.add(feat2Scen1Step1);

		Scenario feat2Scen1 = Scenario.builder().name("Scenario 1 Feature 2").steps(steps).status(Status.SKIPPED)
				.startTime(LocalDateTime.now().plusSeconds(1)).endTime(LocalDateTime.now().plusSeconds(5)).build();

		scenarios = new ArrayList<>();
		scenarios.add(feat2Scen1);

		Feature feature2 = Feature.builder().name("Feature 2").scenarios(scenarios).status(Status.SKIPPED)
				.startTime(LocalDateTime.now().plusSeconds(1))
				.endTime(LocalDateTime.now().plusSeconds(20).plus(321, ChronoField.MILLI_OF_DAY.getBaseUnit())).build();
		features.add(feature2);

		// ---------------------

		Row row1 = Row.builder().cells(Arrays.asList("HELLO SAHA AHAS SAHA", "MOUNISHSAHA AHAS SAHA",
				"HELLOSAHA AHAS SAHA", "MOUNISHSAHA AHAS SAHA", "SAHA AHAS")).build();
		Row row2 = Row.builder().cells(Arrays.asList("THERE", "SAHA", "HELLO", "SAHA AHAS", "SAHA AHAS")).build();
		Row row3 = Row.builder().cells(Arrays.asList("CRAZY", "MISS U", "HELLO", "MOUNISH", "SAHA AHAS")).build();
		Row row4 = Row.builder().cells(Arrays.asList("FOOL", "AHSI", "FOOL", "SAHA AHAS", "SAHA AHAS")).build();
		Row row5 = Row.builder().cells(Arrays.asList("MAN", "AHLUWALIA", "SAHA AHAS", "SAHA AHAS", "SAHA AHAS"))
				.build();
		List<Row> rows = new ArrayList<>();
		rows.add(row1);
		rows.add(row2);
		rows.add(row3);
		rows.add(row4);
		rows.add(row5);

		String docStr = "Hello there how r u?Hello there how r u?Hello there how r u?Hello there how r u?Hello there how r u?\n\nDoing great.\nWhats new?\nNothing much.";

		List<String> output = new ArrayList<>();
		output.add("Hello There the message is not good");
		output.add("MESSED UP! totally");
		output.add("MESSED UP! absolutely");

		steps = new ArrayList<>();
		Step feat3Scen1Step1 = Step.builder().keyword("Given")
				.name("1 Longlong Step 1 F3 text Longlong Step 1 F3 text Longlong Step 1 F3 text").status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(1)).endTime(LocalDateTime.now().plusSeconds(5)).rows(rows)
				.build();
		Step feat3Scen1Step2 = Step.builder().keyword("Given")
				.name("2 Longlong Step 1 F3 text Longlong Step 1 F3 text Longlong Step 1 F3 text").status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(1)).endTime(LocalDateTime.now().plusSeconds(5))
				.output(output).build();
		Step feat3Scen1Step3 = Step.builder().keyword("Given")
				.name("3 Longlong Step 1 F3 text Longlong Step 1 F3 text Longlong Step 1 F3 text").status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(1)).endTime(LocalDateTime.now().plusSeconds(5)).build();
		Step feat3Scen1Step4 = Step.builder().keyword("Given")
				.name("4 Longlong Step 1 F3 text Longlong Step 1 F3 text Longlong Step 1 F3 text").status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(1)).endTime(LocalDateTime.now().plusSeconds(5)).build();
		Step feat3Scen1Step5 = Step.builder().keyword("Given")
				.name("5 Longlong Step 1 F3 text Longlong Step 1 F3 text Longlong Step 1 F3 text").status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(1)).endTime(LocalDateTime.now().plusSeconds(5)).rows(rows)
				.output(output).build();
		Step feat3Scen1Step6 = Step.builder().keyword("Given")
				.name("6 Longlong Step 1 F3 text Longlong Step 1 F3 text Longlong Step 1 F3 text").status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(1)).endTime(LocalDateTime.now().plusSeconds(5))
				.docString(docStr).build();
		Step feat3Scen1Step7 = Step.builder().keyword("Given")
				.name("7 Longlong Step 1 F3 text Longlong Step 1 F3 text Longlong Step 1 F3 text").status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(1)).endTime(LocalDateTime.now().plusSeconds(5))
				.output(output).build();
		Step feat3Scen1Step8 = Step.builder().keyword("Given")
				.name("8 Longlong Step 1 F3 text Longlong Step 1 F3 text Longlong Step 1 F3 text").status(Status.PASSED)
				.startTime(LocalDateTime.now().plusSeconds(1)).endTime(LocalDateTime.now().plusSeconds(5))
				.docString(docStr).output(output).build();

		steps.add(feat3Scen1Step1);
		steps.add(feat3Scen1Step2);
		steps.add(feat3Scen1Step3);
		steps.add(feat3Scen1Step4);
		steps.add(feat3Scen1Step5);
		steps.add(feat3Scen1Step6);
		steps.add(feat3Scen1Step7);
		steps.add(feat3Scen1Step8);

		Scenario feat3Scen1 = Scenario.builder().name("Scenario 1 Feature 3 LonglongLonglong LonglongLonglong")
				.status(Status.PASSED).steps(steps).startTime(LocalDateTime.now().plusSeconds(1))
				.endTime(LocalDateTime.now().plusSeconds(5)).build();

		scenarios = new ArrayList<>();
		scenarios.add(feat3Scen1);

		Feature feature3 = Feature.builder()
				.name("Feature3LonglongLonglongLonglongLong LonglongLonglong LonglongLonglong").scenarios(scenarios)
				.status(Status.PASSED).startTime(LocalDateTime.now().plusSeconds(10))
				.endTime(LocalDateTime.now().plusSeconds(120).plus(888, ChronoField.MILLI_OF_DAY.getBaseUnit()))
				.build();
		features.add(feature3);

		// ---------------------

		ReportData reportData = ReportData.builder().features(features).build();

		PDFCucumberReport pdfReport = new PDFCucumberReport(reportData);
		pdfReport.createReport();
	}
}
