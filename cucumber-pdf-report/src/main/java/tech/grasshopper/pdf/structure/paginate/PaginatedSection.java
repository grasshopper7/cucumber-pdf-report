package tech.grasshopper.pdf.structure.paginate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.data.DisplayData;
import tech.grasshopper.pdf.structure.Section;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public abstract class PaginatedSection extends Section {

	public abstract void generateDisplay(int fromIndex, int toIndex);

	public abstract DisplayData createDisplayData(int fromIndex, int toIndex);
}
