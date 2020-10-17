package tech.grasshopper.pdf.pojo.cucumber;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Row {

	@Builder.Default
	private List<String> cells = new ArrayList<>();
}
