package tech.grasshopper.pdf.structure;

import java.util.function.Supplier;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

@Data
@Builder
public class PageCreator {

	public static PDPage createPotraitPage() {
		return new PDPage(PDRectangle.A4);
	}

	public static PDPage createLandscapePage() {
		return new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
	}

	@SneakyThrows
	public static PDPage createLandscapePageWithHeaderAndNumber(PDDocument document) {
		PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
		PDPageContentStream content = new PDPageContentStream(document, page, AppendMode.APPEND, true);

		Display.displaySectionTitle(content, "DETAILED SECTION");
		Display.displayPageNumber(content, "-- " + (document.getNumberOfPages() + 1) + " --");
		content.close();
		return page;
	}

	public static PDPage createPotraitPageAndAddToDocument(PDDocument document) {
		PDPage page = createPotraitPage();
		document.addPage(page);
		return page;
	}

	public static PDPage createLandscapePageAndAddToDocument(PDDocument document) {
		PDPage page = createLandscapePage();
		document.addPage(page);
		return page;
	}

	public static PDPage createLandscapePageWithHeaderAndNumberAndAddToDocument(PDDocument document) {
		PDPage page = createLandscapePageWithHeaderAndNumber(document);
		document.addPage(page);
		return page;
	}

	public static Supplier<PDPage> potraitPageSupplier() {
		return () -> createPotraitPage();
	}

	public static Supplier<PDPage> landscapePageSupplier() {
		return () -> createLandscapePage();
	}

	public static Supplier<PDPage> landscapePageWithHeaderAndNumberSupplier(PDDocument document) {
		return () -> createLandscapePageWithHeaderAndNumber(document);
	}
}
