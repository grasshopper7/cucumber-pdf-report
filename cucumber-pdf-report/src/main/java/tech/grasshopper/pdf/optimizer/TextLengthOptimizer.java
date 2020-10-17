package tech.grasshopper.pdf.optimizer;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.font.PDFont;

import lombok.Builder;
import lombok.Data;
import tech.grasshopper.pdf.exception.PdfReportException;

@Data
@Builder
public class TextLengthOptimizer {

	private PDFont font;
	private int fontsize;
	private int spaceWidth;

	public int textWidth(String text) {
		int width = 0;
		try {
			width = (int) (font.getStringWidth(text) * fontsize) / 1000;
		} catch (IOException e) {
			throw new PdfReportException(e);
		}
		return width;
	}
	
	public int optimizedTextWidth(String text) {
		int width = 0;
		try {
			width = (int) (font.getStringWidth(optimizeText(text)) * fontsize) / 1000;
		} catch (IOException e) {
			throw new PdfReportException(e);
		}
		return width;
	}

	public boolean doesTextFitInSpace(String text) {
		try {
			if ((font.getStringWidth(text) * fontsize) / 1000 > spaceWidth)
				return false;
		} catch (IOException e) {
			throw new PdfReportException(e);
		}
		return true;
	}

	public String optimizeText(String text) {
		if (doesTextFitInSpace(text))
			return text;
		else
			text = text.substring(0, text.length() - 3);

		while (!doesTextFitInSpace(text + "..."))
			text = text.substring(0, text.length() - 1);

		return text + "...";
	}

	public String optimizeDataCellText(String text) {
		if (doesTextFitInSpace(text)) {
			while (doesTextFitInSpace(text))
				text = text + " ";
			return text.substring(0, text.length() - 2);
		} else
			return optimizeText(text);
	}

	public static String optimizeOutlineText(String text) {
		return optimizeTextLength(text, 50);
	}

	private static String optimizeTextLength(String text, int length) {
		if (text.length() > length)
			return text.substring(0, length - 3) + "...";
		return text;
	}
}
