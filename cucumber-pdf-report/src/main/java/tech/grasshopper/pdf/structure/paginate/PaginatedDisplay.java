package tech.grasshopper.pdf.structure.paginate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.structure.Display;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public abstract class PaginatedDisplay extends Display {

	protected PaginationData paginationData;

}
