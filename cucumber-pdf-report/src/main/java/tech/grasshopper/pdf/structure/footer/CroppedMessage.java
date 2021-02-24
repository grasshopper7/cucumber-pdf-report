package tech.grasshopper.pdf.structure.footer;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.vandeseer.easytable.drawing.DrawingUtil;
import org.vandeseer.easytable.drawing.PositionedStyledText;

import lombok.Builder;
import lombok.Setter;
import lombok.SneakyThrows;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.structure.Display;

@Builder
public class CroppedMessage {

	@Setter
	private PDPageContentStream content;

	@Setter
	private String message;

	@SneakyThrows
	public void displayMessage() {

		PositionedStyledText textDetails = PositionedStyledText.builder().x(Display.HEADER_SECTION_DETAILS_START_X)
				.y(Display.TRIMMED_MESSAGE_START_Y).text(message).font(ReportFont.ITALIC_FONT).fontSize(11)
				.color(Color.LIGHT_GRAY).build();

		DrawingUtil.drawText(content, textDetails);
	}
}
