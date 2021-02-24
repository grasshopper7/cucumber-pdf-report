package tech.grasshopper.pdf.structure;

import java.util.function.Supplier;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import lombok.Builder;
import lombok.Setter;
import lombok.SneakyThrows;
import tech.grasshopper.pdf.structure.header.PageNumber;
import tech.grasshopper.pdf.structure.header.PageTitle;

@Builder
public class PageCreator {

	@Setter
	private PDDocument document;

	public PDPage createPotraitPage() {
		return new PDPage(PDRectangle.A4);
	}

	public PDPage createLandscapePage() {
		return new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
	}

	public PDPage createPotraitPageAndAddToDocument() {
		PDPage page = createPotraitPage();
		document.addPage(page);
		return page;
	}

	public PDPage createLandscapePageAndAddToDocument() {
		PDPage page = createLandscapePage();
		document.addPage(page);
		return page;
	}

	@SneakyThrows
	public PDPage createLandscapePageWithHeaderAndNumber(String title) {

		PDPage page = createLandscapePage();
		PDPageContentStream content = new PDPageContentStream(document, page, AppendMode.APPEND, true);

		PageTitle.builder().content(content).title(title).build().displayTitle();
		PageNumber.builder().content(content).number(document.getNumberOfPages() + 1).build().displayNumber();

		content.close();
		return page;
	}

	public PDPage createLandscapePageWithHeaderAndNumberAndAddToDocument(String title) {
		PDPage page = createLandscapePageWithHeaderAndNumber(title);
		document.addPage(page);
		return page;
	}

	public Supplier<PDPage> potraitPageSupplier() {
		return () -> createPotraitPage();
	}

	public Supplier<PDPage> landscapePageSupplier() {
		return () -> createLandscapePage();
	}

	public Supplier<PDPage> landscapePageWithHeaderAndNumberSupplier(String title) {
		return () -> createLandscapePageWithHeaderAndNumber(title);
	}
}
