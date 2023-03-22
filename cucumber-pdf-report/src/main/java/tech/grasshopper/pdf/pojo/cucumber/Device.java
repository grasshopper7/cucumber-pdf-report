package tech.grasshopper.pdf.pojo.cucumber;

import java.util.Objects;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class Device extends Attribute {

	public Device(String name) {
		super(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Device other = (Device) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
