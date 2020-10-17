package tech.grasshopper.pdf.optimizer;

import org.apache.pdfbox.pdmodel.font.PDFont;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TextContentSanitizer {

	private PDFont font;

	public String sanitizeText(String text) {
		StringBuffer strBuf = new StringBuffer();

		try {
			font.encode(text);
			return text;
		} catch (Exception e) {
			char[] chars = text.toCharArray();
			for (Character character : chars) {
				try{
					font.encode(character.toString());	
					strBuf.append(character);
				} catch(Exception ex) {
					strBuf.append("");
				}					
			}
		}
		return strBuf.toString();
	}
}
