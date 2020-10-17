package tech.grasshopper.pdf.chapter.summary;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.component.Component;
import tech.grasshopper.pdf.component.decorator.BackgroundDecorator;
import tech.grasshopper.pdf.component.decorator.BorderDecorator;
import tech.grasshopper.pdf.component.text.MultiLineTextComponent;
import tech.grasshopper.pdf.data.SummaryData;
import tech.grasshopper.pdf.pojo.report.Text;
import tech.grasshopper.pdf.util.DateUtil;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class SummaryStatistics extends Component {

	private SummaryData summaryData;

	@Override
	public void display() {
		summaryData = (SummaryData) displayData;
		createTestStartedTextBox();
		createTestFinishedTextBox();
		createTestDurationTextBox();
	}

	private void createTestStartedTextBox() {
		createTestStatisticsTextBox("Started At :", DateUtil.formatDateTimeWOYear(summaryData.getTestRunStartTime()),
				45, 40, reportConfig.getSummaryConfig().startTimeColor());
	}

	private void createTestFinishedTextBox() {
		createTestStatisticsTextBox("Finished At :", DateUtil.formatDateTimeWOYear(summaryData.getTestRunEndTime()),
				225, 220, reportConfig.getSummaryConfig().endTimeColor());
	}

	private void createTestDurationTextBox() {
		createTestStatisticsTextBox("Duration :", DateUtil.durationValue(summaryData.getTestRunDuration()), 405, 400,
				reportConfig.getSummaryConfig().durationColor());
	}

	private void createTestStatisticsTextBox(String title, String details, float xTextOffset, float xBoxOffset,
			Color valueTextColor) {

		List<Text> texts = new ArrayList<>();
		texts.add(Text.builder().fontSize(12).font(PDType1Font.HELVETICA).xoffset(xTextOffset).yoffset(705).text(title)
				.build());
		texts.add(Text.builder().fontSize(14).textColor(valueTextColor).font(PDType1Font.HELVETICA_BOLD).xoffset(xTextOffset)
				.yoffset(685).text(details).build());

		Component component = MultiLineTextComponent.builder().content(content).texts(texts).build();
		component = BackgroundDecorator.builder().component(component).content(content).xContainerBottomLeft(xBoxOffset)
				.yContainerBottomLeft(675).containerWidth(160).containerHeight(50).build();
		component = BorderDecorator.builder().component(component).content(content).xContainerBottomLeft(xBoxOffset)
				.yContainerBottomLeft(675).containerWidth(160).containerHeight(50).build();
		component.display();
	}
}
