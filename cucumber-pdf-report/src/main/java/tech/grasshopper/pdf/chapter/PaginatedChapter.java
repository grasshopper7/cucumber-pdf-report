package tech.grasshopper.pdf.chapter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.data.DisplayData;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public abstract class PaginatedChapter extends Chapter {
	
	public abstract void generatePage(int fromIndex, int toIndex, int pageNum) ;
	
	public abstract DisplayData createPageData(int fromIndex, int toIndex) ;
}
