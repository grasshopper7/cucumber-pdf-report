package tech.grasshopper.pdf.optimizer;

import org.apache.pdfbox.pdmodel.font.PDFont;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Setter;
import tech.grasshopper.pdf.font.ReportFont;

@Setter
@Builder
public class TextSanitizer {

	@Default
	private PDFont font = ReportFont.REGULAR_FONT;

	@Default
	private String replaceBy = "?";

	public String sanitizeText(String text) {
		StringBuffer strBuf = new StringBuffer();
		try {
			font.encode(text);
			return text;
		} catch (Exception e) {
			char[] chars = text.toCharArray();
			for (Character character : chars) {
				try {
					// Remove tab character completely
					if (character.equals('\t')) {
						continue;
						// Do not sanitize this! Figure why it is getting replaced.
					} else if (character.equals('\r') || character.equals('\n')) {
						strBuf.append(character);
						continue;
					}
					font.encode(character.toString());
					strBuf.append(character);
				} catch (Exception ex) {
					strBuf.append(replaceBy);
				}
			}
		}
		return strBuf.toString();
	}
}
