package tech.grasshopper.pdf.chapter.summary;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.component.Component;
import tech.grasshopper.pdf.component.decorator.BackgroundDecorator;
import tech.grasshopper.pdf.component.text.MultiLineTextComponent;
import tech.grasshopper.pdf.data.SummaryData;
import tech.grasshopper.pdf.pojo.report.Text;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class SummaryChartData extends Component {

	private SummaryData summaryData;

	@Override
	public void display() {
		summaryData = (SummaryData) displayData;
		createFeaturesDataTextBox();
		createScenariosDataTextBox();
		createStepsDataTextBox();
	}

	private void createFeaturesDataTextBox() {
		createDataTextBox(summaryData.getPassedFeatures(), summaryData.getFailedFeatures(),
				summaryData.getSkippedFeatures(), 40, 45);
	}

	private void createScenariosDataTextBox() {
		createDataTextBox(summaryData.getPassedScenarios(), summaryData.getFailedScenarios(),
				summaryData.getSkippedScenarios(), 220, 225);
	}

	private void createStepsDataTextBox() {
		createDataTextBox(summaryData.getPassedSteps(), summaryData.getFailedSteps(), summaryData.getSkippedSteps(),
				400, 405);
	}

	private void createDataTextBox(Number passed, Number failed, Number skipped, float xBoxOffset, float xOffset) {
		List<Text> texts = new ArrayList<>();
		texts.add(Text.builder().textColor(reportConfig.passedColor()).fontSize(12).xoffset(xOffset).yoffset(380)
				.text("Passed - " + passed).build());
		texts.add(Text.builder().textColor(reportConfig.failedColor()).fontSize(12).xoffset(xOffset).yoffset(360)
				.text("Failed - " + failed).build());
		texts.add(Text.builder().textColor(reportConfig.skippedColor()).fontSize(12).xoffset(xOffset).yoffset(340)
				.text("Skipped - " + skipped).build());

		Component component = MultiLineTextComponent.builder().content(content).texts(texts).build();
		component = BackgroundDecorator.builder().component(component).content(content)
				.containerColor(reportConfig.getSummaryConfig().dataBackgroundColor()).xContainerBottomLeft(xBoxOffset)
				.yContainerBottomLeft(330).containerWidth(160).containerHeight(70).build();
		component.display();
	}
}
