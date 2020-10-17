package tech.grasshopper.pdf.chapter.page;

import lombok.Builder;
import lombok.Data;
import tech.grasshopper.pdf.chapter.PaginatedChapter;

@Data
@Builder
public class Paginator {

	private int itemsCount;
	private int itemsPerPage;
	private PaginatedChapter chapter;

	public void paginate() {
		
		int pageCount = itemsCount / itemsPerPage;
		if(itemsCount % itemsPerPage > 0) pageCount ++;
	
		for (int i = 0 ; i < pageCount ; i++) {			
			int fromIndex = i * itemsPerPage;
			int toIndex = fromIndex + itemsPerPage;
			
			if(toIndex > itemsCount)
				toIndex = itemsCount;
			
			chapter.generatePage(fromIndex, toIndex, i + 1);
		}
	}	
}
