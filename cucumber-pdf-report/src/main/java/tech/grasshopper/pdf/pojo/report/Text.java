package tech.grasshopper.pdf.pojo.report;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class Text {

	@Default
	private Color textColor = Color.BLACK;
	@Default
	private PDFont font = PDType1Font.HELVETICA;
	private float fontSize;
	private float xoffset;
	private float yoffset;
	private String text;
}
