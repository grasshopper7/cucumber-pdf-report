package tech.grasshopper.pdf.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParameterConfig {

	private String title;
	private String titleColor;

	private String passColor;
	private String failColor;
	private String skipColor;

	private String displayFeature;
	private String displayScenario;
	private String displayDetailed;
	private String displayExpanded;
	private String displayAttached;
}
