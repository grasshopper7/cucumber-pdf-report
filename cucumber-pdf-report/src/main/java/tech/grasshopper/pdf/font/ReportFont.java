package tech.grasshopper.pdf.font;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import tech.grasshopper.pdf.exception.PdfReportException;

public class ReportFont {

	public static PDFont REGULAR_FONT;
	public static PDFont BOLD_FONT;
	public static PDFont ITALIC_FONT;
	public static PDFont BOLD_ITALIC_FONT;
	public static final String FONT_FOLDER = "/tech/grasshopper/ttf/";

	public static void loadReportFontFamily(PDDocument document) {

		try {
			REGULAR_FONT = PDType0Font.load(document,
					ReportFont.class.getResourceAsStream(FONT_FOLDER + "LiberationSans-Regular.ttf"));

			BOLD_FONT = PDType0Font.load(document,
					ReportFont.class.getResourceAsStream(FONT_FOLDER + "LiberationSans-Bold.ttf"));

			ITALIC_FONT = PDType0Font.load(document,
					ReportFont.class.getResourceAsStream(FONT_FOLDER + "LiberationSans-Italic.ttf"));

			BOLD_ITALIC_FONT = PDType0Font.load(document,
					ReportFont.class.getResourceAsStream(FONT_FOLDER + "LiberationSans-BoldItalic.ttf"));
		} catch (IOException e) {
			throw new PdfReportException(e);
		}
	}
}
