package tech.grasshopper.pdf.data;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import tech.grasshopper.pdf.pojo.cucumber.Tag;

@Getter
@Builder
public class TagData implements DisplayData {

	@Default
	private List<Tag> tags = new ArrayList<>();
}
