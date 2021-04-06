package tech.grasshopper.pdf.optimizer;

import org.apache.pdfbox.pdmodel.font.PDFont;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import tech.grasshopper.pdf.font.ReportFont;

@Setter
@Builder
public class TextSanitizer {

	@Default
	private PDFont font = ReportFont.REGULAR_FONT;

	@Default
	private String replaceBy = "?";

	@Default
	private String replaceTabBy = "    ";

	@Getter
	@Default
	private boolean tabStripped = false;

	@Getter
	@Default
	private boolean glyphAbsentStripped = false;

	public String tabStripMessage() {
		return "TAB character is replaced by '" + replaceTabBy + "'.";
	}

	public String glyphAbsentStripMessage() {
		return "Not displayable characters are replaced by '" + replaceBy + "'.";
	}

	public String getStripMessage() {
		String message = "";

		if (tabStripped)
			message = "* " + tabStripMessage();

		if (glyphAbsentStripped) {
			message = message.isEmpty() ? "* " : " ";
			message += glyphAbsentStripMessage();
		}

		return message;
	}

	public String sanitizeText(String text) {
		StringBuffer strBuf = new StringBuffer();
		try {
			font.encode(text);
			return text;
		} catch (Exception e) {
			char[] chars = text.toCharArray();
			for (Character character : chars) {
				try {

					// Replace tab character
					if (character.equals('\t')) {
						// tabStripped = true;
						strBuf.append(replaceTabBy);
						continue;

						// Do not sanitize this! Figure why it is getting replaced!!
					} else if (character.equals('\r') || character.equals('\n')) {
						strBuf.append(character);
						continue;
					}

					font.encode(character.toString());
					strBuf.append(character);
				} catch (Exception ex) {

					// Remove character if glyph missing in font family
					glyphAbsentStripped = true;
					strBuf.append(replaceBy);
				}
			}
		}
		return strBuf.toString();
	}
}
