package tech.grasshopper.pdf.section.attributes;

import java.util.List;
import java.util.function.Consumer;

import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.data.AttributeData.DeviceData;
import tech.grasshopper.pdf.data.DisplayData;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.pojo.cucumber.Device;

@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class DeviceSection extends AttributeSection {

	@Override
	public DisplayData createDisplayData(int fromIndex, int toIndex) {
		List<Device> pageDevices = ((DeviceData) attributeData).getDevices().subList(fromIndex, toIndex);
		return DeviceData.builder().devices(pageDevices).build();
	}

	@Override
	public int maxDataCountPerPage() {
		return reportConfig.getDeviceConfig().dataCount();
	}

	@Override
	public String attributeType() {
		return "DEVICE";
	}

	@Override
	public Consumer<Destination> attributeDestinationConsumer() {
		return destinations::addDeviceDestination;
	}
}
