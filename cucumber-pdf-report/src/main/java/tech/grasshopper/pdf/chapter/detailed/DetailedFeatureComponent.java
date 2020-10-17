package tech.grasshopper.pdf.chapter.detailed;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.knowm.xchart.style.PieStyler;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.component.Component;
import tech.grasshopper.pdf.component.chart.ChartComponent;
import tech.grasshopper.pdf.component.chart.ChartDisplayer;
import tech.grasshopper.pdf.component.chart.ReportDonutChart;
import tech.grasshopper.pdf.component.decorator.BackgroundDecorator;
import tech.grasshopper.pdf.component.line.HorizontalLineComponent;
import tech.grasshopper.pdf.component.text.MultiLineTextComponent;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.destination.DestinationAware;
import tech.grasshopper.pdf.optimizer.TextContentSanitizer;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.pojo.report.Text;
import tech.grasshopper.pdf.util.DateUtil;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class DetailedFeatureComponent extends ChartComponent implements DestinationAware {

	public static final int CONTENT_HEIGHT = 195;

	private Feature feature;
	private int startHeight;

	private static final PDFont NAME_FONT = PDType1Font.HELVETICA_BOLD;
	private static final int NAME_FONT_SIZE = 16;

	private static final int FEATURE_NAME_WIDTH = 400;
	private static final int PADDING = 5;

	private final TextLengthOptimizer featureNameTextOptimizer = TextLengthOptimizer.builder().font(NAME_FONT)
			.fontsize(NAME_FONT_SIZE).spaceWidth(FEATURE_NAME_WIDTH - 2 * PADDING).build();

	private final TextContentSanitizer textSanitizer = TextContentSanitizer.builder().font(NAME_FONT).build();

	@Override
	public void display() {

		createHeader();
		createDuration();
		createScenariosData();
		createScenariosDonut();
		createStepsData();
		createStepsDonut();
		createEndLine();
	}

	@Override
	public Destination createDestination() {
		Destination destination = Destination.builder().name(feature.getName()).yCoord(startHeight - 15).page(page)
				.build();
		feature.setDestination(destination.createPDPageDestination());
		return destination;
	}

	private void createHeader() {
		List<Text> texts = new ArrayList<>();

		texts.add(Text.builder().textColor(reportConfig.getDetailedFeatureConfig().featureNameColor()).font(NAME_FONT)
				.fontSize(NAME_FONT_SIZE).xoffset(50).yoffset(startHeight - 30)
				.text(featureNameTextOptimizer.optimizeText("(F) - " + textSanitizer.sanitizeText(feature.getName())))
				.build());

		texts.add(Text.builder().textColor(reportConfig.getDetailedFeatureConfig().startEndTimeColor())
				.font(PDType1Font.HELVETICA_OBLIQUE).fontSize(12).xoffset(50).yoffset(startHeight - 50)
				.text("//  " + DateUtil.formatDateTimeWOYear(feature.getStartTime()) + "  //  "
						+ DateUtil.formatDateTimeWOYear(feature.getEndTime()) + "  //")
				.build());

		Collections.sort(feature.getTags());
		String tags = "@Tags - " + feature.getTags().stream().collect(Collectors.joining(" "));
		texts.add(Text.builder().font(PDType1Font.HELVETICA).fontSize(11)
				.textColor(reportConfig.getDetailedFeatureConfig().tagColor()).xoffset(50).yoffset(startHeight - 70)
				.text(featureNameTextOptimizer.optimizeText(textSanitizer.sanitizeText(tags))).build());

		MultiLineTextComponent.builder().content(content).texts(texts).build().display();
	}

	private void createDuration() {
		List<Text> texts = new ArrayList<>();

		texts.add(Text.builder().textColor(reportConfig.getDetailedFeatureConfig().durationColor())
				.font(PDType1Font.HELVETICA_BOLD_OBLIQUE).fontSize(10).xoffset(450).yoffset(startHeight - 40)
				.text("Duration").build());
		texts.add(Text.builder().textColor(reportConfig.getDetailedFeatureConfig().durationColor())
				.font(PDType1Font.HELVETICA_BOLD_OBLIQUE).fontSize(11).xoffset(450).yoffset(startHeight - 60)
				.text(DateUtil.durationValue(feature.getDuration())).build());

		Component component = MultiLineTextComponent.builder().content(content).texts(texts).build();
		component = BackgroundDecorator.builder().component(component).content(content)
				.containerColor(reportConfig.getDetailedFeatureConfig().durationBackgroundColor())
				.xContainerBottomLeft(445).yContainerBottomLeft(startHeight - 70).containerWidth(100)
				.containerHeight(45).build();
		component.display();
	}

	private void createScenariosData() {
		createData("Scenarios", feature.getTotalScenarios(), feature.getPassedScenarios(), feature.getFailedScenarios(),
				feature.getSkippedScenarios(), 55, 65);
	}

	private void createScenariosDonut() {
		ReportDonutChart chart = new ReportDonutChart(100, 100);
		updateChartStyler(chart.getStyler());

		Map<String, Number> data = new HashMap<>();
		data.put("Passed", feature.getPassedScenarios());
		data.put("Failed", feature.getFailedScenarios());
		data.put("Skipped", feature.getSkippedScenarios());

		chart.updateData(data);
		ChartDisplayer.builder().document(document).content(content).chart(chart).xBottomLeft(145)
				.yBottomLeft(startHeight - 180).build().display();
	}

	private void createStepsData() {
		createData("Steps", feature.getTotalSteps(), feature.getPassedSteps(), feature.getFailedSteps(),
				feature.getSkippedSteps(), 300, 65);
	}

	private void createStepsDonut() {
		ReportDonutChart chart = new ReportDonutChart(100, 100);
		updateChartStyler(chart.getStyler());

		Map<String, Number> data = new HashMap<>();
		data.put("Passed", feature.getPassedSteps());
		data.put("Failed", feature.getFailedSteps());
		data.put("Skipped", feature.getSkippedSteps());

		chart.updateData(data);
		ChartDisplayer.builder().document(document).content(content).chart(chart).xBottomLeft(390)
				.yBottomLeft(startHeight - 180).build().display();
	}

	private void createEndLine() {
		HorizontalLineComponent.builder().content(content).color(Color.GRAY).xStartCord(50).xEndCord(550)
				.yCord(startHeight - 190).build().display();
	}

	private void createData(String title, int total, int pass, int fail, int skip, int xOffset, int width) {
		List<Text> texts = new ArrayList<>();

		texts.add(Text.builder().textColor(reportConfig.getDetailedFeatureConfig().dataHeaderColor())
				.font(PDType1Font.HELVETICA_BOLD).fontSize(11).xoffset(xOffset).yoffset(startHeight - 95).text(title)
				.build());
		texts.add(Text.builder().textColor(reportConfig.getDetailedFeatureConfig().totalColor())
				.font(PDType1Font.HELVETICA_BOLD).fontSize(9).xoffset(xOffset).yoffset(startHeight - 115)
				.text("Total " + total).build());
		texts.add(Text.builder().textColor(reportConfig.passedColor()).font(PDType1Font.HELVETICA_BOLD).fontSize(9)
				.xoffset(xOffset).yoffset(startHeight - 135).text("Pass " + pass).build());
		texts.add(Text.builder().textColor(reportConfig.failedColor()).font(PDType1Font.HELVETICA_BOLD).fontSize(9)
				.xoffset(xOffset).yoffset(startHeight - 155).text("Fail " + fail).build());
		texts.add(Text.builder().textColor(reportConfig.skippedColor()).font(PDType1Font.HELVETICA_BOLD).fontSize(9)
				.xoffset(xOffset).yoffset(startHeight - 175).text("Skip " + skip).build());

		Component component = MultiLineTextComponent.builder().content(content).texts(texts).build();
		component = BackgroundDecorator.builder().component(component).content(content)
				.containerColor(reportConfig.getDetailedFeatureConfig().dataBackgroundColor())
				.xContainerBottomLeft(xOffset - 5).yContainerBottomLeft(startHeight - 180).containerWidth(width)
				.containerHeight(100).build();
		component.display();
	}

	private void updateChartStyler(PieStyler styler) {
		styler.setSumFontSize(16f);
		styler.setDonutThickness(0.4);
		styler.setSeriesColors(
				new Color[] { reportConfig.passedColor(), reportConfig.failedColor(), reportConfig.skippedColor() });
	}
}
