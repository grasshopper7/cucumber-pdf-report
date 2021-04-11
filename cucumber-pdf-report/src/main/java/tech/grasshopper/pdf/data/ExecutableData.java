package tech.grasshopper.pdf.data;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import tech.grasshopper.pdf.pojo.cucumber.Executable;

@Getter
@Builder
public class ExecutableData implements DisplayData {

	@Default
	private List<Executable> executables = new ArrayList<>();
}
