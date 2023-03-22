package tech.grasshopper.pdf.data;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import tech.grasshopper.pdf.pojo.cucumber.Attribute;
import tech.grasshopper.pdf.pojo.cucumber.Author;
import tech.grasshopper.pdf.pojo.cucumber.Device;
import tech.grasshopper.pdf.pojo.cucumber.Tag;

public abstract class AttributeData implements DisplayData {

	public abstract List<? extends Attribute> getAttributes();

	@Getter
	@Builder
	public static class AuthorData extends AttributeData {

		@Default
		private List<Author> authors = new ArrayList<>();

		@Override
		public List<? extends Attribute> getAttributes() {
			return authors;
		}
	}

	@Getter
	@Builder
	public static class DeviceData extends AttributeData {

		@Default
		private List<Device> devices = new ArrayList<>();

		@Override
		public List<? extends Attribute> getAttributes() {
			return devices;
		}
	}

	@Getter
	@Builder
	public static class TagData extends AttributeData {

		@Default
		private List<Tag> tags = new ArrayList<>();

		@Override
		public List<? extends Attribute> getAttributes() {
			return tags;
		}
	}

}
