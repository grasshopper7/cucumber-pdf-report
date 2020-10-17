package tech.grasshopper.pdf.component.chart;

import org.apache.pdfbox.pdmodel.PDDocument;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.component.Component;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public abstract class ChartComponent extends Component {

	protected PDDocument document;

}
