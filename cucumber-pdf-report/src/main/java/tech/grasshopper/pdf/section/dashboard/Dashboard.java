package tech.grasshopper.pdf.section.dashboard;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.destination.DestinationAware;
import tech.grasshopper.pdf.outline.Outline;
import tech.grasshopper.pdf.structure.Display;
import tech.grasshopper.pdf.structure.SinglePageSection;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class Dashboard extends SinglePageSection implements DestinationAware {

	static final float DATA_COLUMN_WIDTH = 220;
	static final float SPACE_COLUMN_WIDTH = 20;

	@Override
	@SneakyThrows
	public void createSection() {

		page = createPage();

		try (final PDPageContentStream content = new PDPageContentStream(document, page, AppendMode.APPEND, true)) {

			final TableBuilder tableBuilder = Table.builder()
					.addColumnsOfWidth(DATA_COLUMN_WIDTH, SPACE_COLUMN_WIDTH, DATA_COLUMN_WIDTH, SPACE_COLUMN_WIDTH,
							DATA_COLUMN_WIDTH)
					.borderWidth(0f).horizontalAlignment(HorizontalAlignment.CENTER)
					.verticalAlignment(VerticalAlignment.MIDDLE).padding(5f);

			DashboardDetailsDisplay.builder().tableBuilder(tableBuilder).reportConfig(reportConfig)
					.displayData(displayData).build().display();

			DashboardDonutDisplay.builder().tableBuilder(tableBuilder).reportConfig(reportConfig)
					.displayData(displayData).document(document).build().display();

			DashboardDialDisplay.builder().tableBuilder(tableBuilder).reportConfig(reportConfig)
					.displayData(displayData).document(document).build().display();

			TableDrawer.builder().table(tableBuilder.build()).startX(70f).startY(Display.CONTENT_START_Y)
					.contentStream(content).build().draw();
		}
		createDestination();
	}

	@Override
	public void createDestination() {
		Destination destination = Destination.builder().name(Outline.DASHBOARD_SECTION_TEXT)
				.yCoord((int) page.getMediaBox().getHeight()).page(page).build();
		destinations.setDashboardDestination(destination);
	}
}
