package tech.grasshopper.pdf.pojo.report;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.font.PDFont;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import tech.grasshopper.pdf.font.ReportFont;

@Data
@Builder
public class Text {

	@Default
	private Color textColor = Color.BLACK;
	@Default
	private PDFont font = ReportFont.REGULAR_FONT;
	private float fontSize;
	private float xoffset;
	private float yoffset;
	private String text;
}
