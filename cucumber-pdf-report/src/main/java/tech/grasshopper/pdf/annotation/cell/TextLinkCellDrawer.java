package tech.grasshopper.pdf.annotation.cell;

import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;
import static org.vandeseer.easytable.settings.HorizontalAlignment.JUSTIFY;
import static org.vandeseer.easytable.settings.HorizontalAlignment.RIGHT;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.drawing.DrawingUtil;
import org.vandeseer.easytable.drawing.PositionedLine;
import org.vandeseer.easytable.drawing.PositionedStyledText;
import org.vandeseer.easytable.drawing.cell.TextCellDrawer;
import org.vandeseer.easytable.settings.BorderStyle;
import org.vandeseer.easytable.util.PdfUtil;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import tech.grasshopper.pdf.annotation.Annotation;

@NoArgsConstructor
public class TextLinkCellDrawer<T extends TextLinkCell> extends TextCellDrawer<TextLinkCell> {

	private Annotation annotation;

	public TextLinkCellDrawer(T cell, Annotation annotation) {
		this.cell = cell;
		this.annotation = annotation;
	}

	@Override
	@SneakyThrows
	public void drawContent(DrawingContext drawingContext) {

		final float startX = drawingContext.getStartingPoint().x;

		final PDFont currentFont = cell.getFont();
		final int currentFontSize = cell.getFontSize();
		final Color currentTextColor = cell.getTextColor();

		float yOffset = drawingContext.getStartingPoint().y + getAdaptionForVerticalAlignment();
		float xOffset = startX + cell.getPaddingLeft();

		final List<String> lines = calculateAndGetLines(currentFont, currentFontSize, cell.getMaxWidth());
		for (int i = 0; i < lines.size(); i++) {
			final String line = lines.get(i);

			yOffset -= calculateYOffset(currentFont, currentFontSize, i);

			final float textWidth = PdfUtil.getStringWidth(line, currentFont, currentFontSize);

			// Handle horizontal alignment by adjusting the xOffset
			if (cell.isHorizontallyAligned(RIGHT)) {
				xOffset = startX + (cell.getWidth() - (textWidth + cell.getPaddingRight()));

			} else if (cell.isHorizontallyAligned(CENTER)) {
				xOffset = startX + (cell.getWidth() - textWidth) / 2;

			} else if (cell.isHorizontallyAligned(JUSTIFY) && (i != lines.size() - 1)) {
				drawingContext.getContentStream().setCharacterSpacing(calculateCharSpacingFor(line));
			}

			drawText(drawingContext, PositionedStyledText.builder().x(xOffset).y(yOffset).text(line).font(currentFont)
					.fontSize(currentFontSize).color(currentTextColor).build());

			// Hack no logic with the denominator factor
			float lineOffset = currentFontSize * currentFont.getFontDescriptor().getAscent() / 3000;

			drawLine(drawingContext, PositionedLine.builder().startX(xOffset).endX(xOffset + textWidth)
					.width(0.01f + currentFontSize * 0.05f).startY(yOffset - lineOffset).endY(yOffset - lineOffset)
					.color(Color.BLACK).resetColor(currentTextColor).borderStyle(BorderStyle.SOLID).build());
		}

		updateAnnotation(drawingContext);
	}

	protected float calculateYOffset(PDFont currentFont, int currentFontSize, int lineIndex) {
		return PdfUtil.getFontHeight(currentFont, currentFontSize)
				+ (lineIndex > 0 ? PdfUtil.getFontHeight(currentFont, currentFontSize) * cell.getLineSpacing() : 0f);
	}

	protected void drawLine(DrawingContext drawingContext, PositionedLine positionedLine) throws IOException {
		DrawingUtil.drawLine(drawingContext.getContentStream(), positionedLine);
	}

	private void updateAnnotation(DrawingContext drawingContext) {

		annotation.setXBottom(drawingContext.getStartingPoint().x);
		annotation.setYBottom(drawingContext.getStartingPoint().y);
		annotation.setWidth(cell.getWidth());
		annotation.setHeight(cell.getRow().getHeight());
		annotation.setPage(drawingContext.getPage());
	}
}
