package tech.grasshopper.pdf.annotation.cell;

import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.structure.cell.AbstractTextCell;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.annotation.Annotation;

@Getter
@SuperBuilder(toBuilder = true)
public class TextLinkCell extends AbstractTextCell {

	@NonNull
	protected String text;
	
	//@NonNull
	protected Annotation annotation;

	@Override
	protected Drawer createDefaultDrawer() {
		return new TextLinkCellDrawer<TextLinkCell>(this, annotation);		
	}

}
