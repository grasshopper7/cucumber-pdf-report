package tech.grasshopper.pdf.structure;

import org.apache.pdfbox.pdmodel.PDPage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public abstract class SinglePageSection extends Section {

	protected PDPage page;
}
