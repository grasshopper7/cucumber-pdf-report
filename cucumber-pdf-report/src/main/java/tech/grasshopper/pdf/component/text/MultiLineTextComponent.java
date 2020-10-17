package tech.grasshopper.pdf.component.text;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.component.Component;
import tech.grasshopper.pdf.pojo.report.Text;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class MultiLineTextComponent extends Component {

	private List<Text> texts;

	@Override
	public void display() {
		texts.forEach(t -> TextComponent.builder().content(content).text(t).build().display());
	}
}
