package tech.grasshopper.pdf.chapter.summary;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.component.Component;
import tech.grasshopper.pdf.component.decorator.BackgroundDecorator;
import tech.grasshopper.pdf.component.decorator.BorderDecorator;
import tech.grasshopper.pdf.component.text.MultiLineTextComponent;
import tech.grasshopper.pdf.data.SummaryData;
import tech.grasshopper.pdf.font.ReportFont;
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
		createTestStatisticsTextBox("Started At :  " + DateUtil.formatDateWOYear(summaryData.getTestRunStartTime()) + ",",
				DateUtil.formatTimeWithMillis(summaryData.getTestRunStartTime()), 50, 40,
				reportConfig.getSummaryConfig().startTimeColor());
	}

	private void createTestFinishedTextBox() {
		createTestStatisticsTextBox("Finished At :  " + DateUtil.formatDateWOYear(summaryData.getTestRunEndTime()) + ",",
				DateUtil.formatTimeWithMillis(summaryData.getTestRunEndTime()), 230, 220,
				reportConfig.getSummaryConfig().endTimeColor());
	}

	private void createTestDurationTextBox() {
		createTestStatisticsTextBox("Duration :", DateUtil.durationValue(summaryData.getTestRunDuration()), 410, 400,
				reportConfig.getSummaryConfig().durationColor());
	}

	private void createTestStatisticsTextBox(String topRow, String bottomRow, float xTextOffset, float xBoxOffset,
			Color valueTextColor) {

		List<Text> texts = new ArrayList<>();
		texts.add(Text.builder().fontSize(13).font(ReportFont.BOLD_ITALIC_FONT).xoffset(xTextOffset).yoffset(705).text(topRow)
				.build());
		texts.add(Text.builder().fontSize(15).textColor(valueTextColor).font(ReportFont.BOLD_ITALIC_FONT)
				.xoffset(xTextOffset).yoffset(685).text(bottomRow).build());

		Component component = MultiLineTextComponent.builder().content(content).texts(texts).build();
		component = BackgroundDecorator.builder().component(component).content(content).xContainerBottomLeft(xBoxOffset)
				.yContainerBottomLeft(675).containerWidth(160).containerHeight(50).build();
		component = BorderDecorator.builder().component(component).content(content).xContainerBottomLeft(xBoxOffset)
				.yContainerBottomLeft(675).containerWidth(160).containerHeight(50).build();
		component.display();
	}
}
