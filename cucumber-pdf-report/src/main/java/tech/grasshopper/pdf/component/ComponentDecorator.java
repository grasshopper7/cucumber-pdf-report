package tech.grasshopper.pdf.component;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public abstract class ComponentDecorator extends Component {

	@Override
	public abstract void display();
}
