package tech.grasshopper.pdf.component.line;

import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.component.Component;
import tech.grasshopper.pdf.exception.PdfReportException;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class HorizontalLineComponent extends Component {
	
	private static final Logger logger = Logger.getLogger(HorizontalLineComponent.class.getName());

	@Default
	private Color color = Color.BLACK;
	private int yCord;
	private int xStartCord;
	private int xEndCord;
	
	@Override
	public void display() {		
		try {
			content.setStrokingColor(color);
			content.moveTo(xStartCord, yCord);
			content.lineTo(xEndCord, yCord);
			content.stroke();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occurred", e);
			throw new PdfReportException(e);
		}
	}
}
