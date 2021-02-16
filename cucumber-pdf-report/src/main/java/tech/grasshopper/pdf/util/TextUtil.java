package tech.grasshopper.pdf.util;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.util.PdfUtil;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Setter;

@Setter
@Builder
public class TextUtil {

	private PDFont font;
	private int fontSize;
	private String text;
	private float width;
	@Default
	private float lineSpacing = 1f;
	@Default
	private float padding = 0f;

	public float tableRowHeight() {

		float textHeight = PdfUtil.getFontHeight(font, fontSize);

		final int size = PdfUtil.getOptimalTextBreakLines(text, font, fontSize, (width - 2 * padding)).size();

		final float heightOfTextLines = size * textHeight;
		final float heightOfLineSpacing = (size - 1) * textHeight * lineSpacing;

		return heightOfTextLines + heightOfLineSpacing + (2 * padding);
	}

}
