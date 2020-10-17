package tech.grasshopper.pdf.chapter.summary;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.component.Component;
import tech.grasshopper.pdf.component.decorator.BorderDecorator;
import tech.grasshopper.pdf.component.text.TextComponent;
import tech.grasshopper.pdf.pojo.report.Text;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class SummaryChartTitles extends Component {

	@Override
	public void display() {
		createFeaturesChartTitle();
		createScenariosChartTitle();
		createStepsChartTitle();
	}

	private void createFeaturesChartTitle() {
		Text text = Text.builder().fontSize(14).xoffset(95).yoffset(620).text("Features")
				.font(PDType1Font.HELVETICA_OBLIQUE).build();
		Component component = TextComponent.builder().content(content).text(text).build();
		component = BorderDecorator.builder().component(component).content(content).borderColor(Color.LIGHT_GRAY)
				.xContainerBottomLeft(40).yContainerBottomLeft(613).containerWidth(160).containerHeight(25).build();
		component.display();
	}

	private void createScenariosChartTitle() {
		Text text = Text.builder().fontSize(14).xoffset(270).yoffset(620).text("Scenarios")
				.font(PDType1Font.HELVETICA_OBLIQUE).build();
		Component component = TextComponent.builder().content(content).text(text).build();
		component = BorderDecorator.builder().component(component).content(content).borderColor(Color.LIGHT_GRAY)
				.xContainerBottomLeft(220).yContainerBottomLeft(613).containerWidth(160).containerHeight(25).build();
		component.display();
	}

	private void createStepsChartTitle() {
		Text text = Text.builder().fontSize(14).xoffset(465).yoffset(620).text("Steps")
				.font(PDType1Font.HELVETICA_OBLIQUE).build();
		Component component = TextComponent.builder().content(content).text(text).build();
		component = BorderDecorator.builder().component(component).content(content).borderColor(Color.LIGHT_GRAY)
				.xContainerBottomLeft(400).yContainerBottomLeft(613).containerWidth(160).containerHeight(25).build();
		component.display();
	}
}
