package tech.grasshopper.pdf.annotation.cell;

import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.annotation.Annotation;

@Getter
@SuperBuilder(toBuilder = true)
public class TextLinkCell extends TextCell {

	@NonNull
	protected Annotation annotation;

	@Default
	protected boolean showLine = true;

	@Override
	protected Drawer createDefaultDrawer() {
		return new TextLinkCellDrawer<TextLinkCell>(this, annotation, showLine);
	}
}
