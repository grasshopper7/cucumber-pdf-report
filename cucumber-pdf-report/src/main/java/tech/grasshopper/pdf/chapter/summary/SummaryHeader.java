package tech.grasshopper.pdf.chapter.summary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.component.Component;
import tech.grasshopper.pdf.component.text.TextComponent;
import tech.grasshopper.pdf.data.SummaryData;
import tech.grasshopper.pdf.optimizer.TextContentSanitizer;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.pojo.report.Text;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class SummaryHeader extends Component {

	private SummaryData summaryData;

	private static final PDFont TITLE_FONT = PDType1Font.HELVETICA_BOLD;
	private static final int TITLE_FONT_SIZE = 16;

	private final TextLengthOptimizer featureNameTextOptimizer = TextLengthOptimizer.builder().font(TITLE_FONT)
			.fontsize(TITLE_FONT_SIZE).spaceWidth(340).build();

	private final TextContentSanitizer textSanitizer = TextContentSanitizer.builder().font(TITLE_FONT).build();

	@Override
	public void display() {
		summaryData = (SummaryData) displayData;
		createReportTitleText();
		createReportDateText();
	}

	private void createReportTitleText() {
		Text text = Text.builder().textColor(reportConfig.getSummaryConfig().titleColor()).font(TITLE_FONT)
				.fontSize(TITLE_FONT_SIZE).xoffset(40).yoffset(750).text(featureNameTextOptimizer
						.optimizeText(textSanitizer.sanitizeText(reportConfig.getSummaryConfig().getTitle())))
				.build();
		TextComponent.builder().content(content).text(text).build().display();
	}

	private void createReportDateText() {
		Text text = Text.builder().textColor(reportConfig.getSummaryConfig().dateColor()).font(PDType1Font.HELVETICA).fontSize(12).xoffset(420)
				.yoffset(750)
				.text(LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))).build();
		TextComponent.builder().content(content).text(text).build().display();
	}
}
