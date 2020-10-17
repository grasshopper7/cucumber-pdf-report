package tech.grasshopper.pdf.chapter.page;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public abstract class PaginatedPage extends Page {

	protected PaginationData paginationData;

}
