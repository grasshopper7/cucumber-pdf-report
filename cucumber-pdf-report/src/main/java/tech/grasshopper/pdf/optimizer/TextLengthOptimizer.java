package tech.grasshopper.pdf.optimizer;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.util.PdfUtil;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;

@Builder
public class TextLengthOptimizer {

	@Setter
	private PDFont font;
	@Setter
	private int fontsize;
	@Setter
	private float availableSpace;

	@Setter
	@Default
	private int maxLines = 1;

	@Getter
	@Default
	private boolean textTrimmed = false;

	public boolean doesTextFitInSpace(String text) {

		if (PdfUtil.getStringWidth(text, font, fontsize) > availableSpace)
			return false;
		return true;
	}

	public String optimizeText(String text) {

		if (doesTextFitInSpace(text)) {
			textTrimmed = false;
			return text;
		} else {
			textTrimmed = true;
			text = text.substring(0, text.length() - 2);
		}

		while (!doesTextFitInSpace((new StringBuffer(text).append("*")).toString()))
			text = text.substring(0, text.length() - 1);

		return text + "*";
	}

	public String optimizeTextLines(String text) {

		List<String> lines = PdfUtil.getOptimalTextBreakLines(text, font, fontsize, availableSpace);

		if (lines.size() > maxLines) {
			textTrimmed = true;
			if (PdfUtil.getOptimalTextBreakLines(lines.get(maxLines - 1) + " *", font, fontsize, availableSpace)
					.size() > 1) {
				int length = lines.get(maxLines - 1).length();
				lines.set(maxLines - 1, lines.get(maxLines - 1).substring(0, length - 3) + " *");
			} else {
				lines.set(maxLines - 1, lines.get(maxLines - 1) + " *");
			}
		} else {
			textTrimmed = false;
			return text;
		}

		return lines.subList(0, maxLines).stream().collect(Collectors.joining(" "));
	}

	public static String optimizeOutlineText(String text) {
		return optimizeTextLength(text, 50);
	}

	private static String optimizeTextLength(String text, int length) {
		if (text.length() > length)
			return text.substring(0, length - 3) + " *";
		return text;
	}
}
