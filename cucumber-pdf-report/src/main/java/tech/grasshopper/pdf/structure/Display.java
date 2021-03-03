package tech.grasshopper.pdf.structure;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import lombok.Builder.Default;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.config.ReportConfig;
import tech.grasshopper.pdf.data.DisplayData;
import tech.grasshopper.pdf.destination.Destination.DestinationStore;
import tech.grasshopper.pdf.pojo.cucumber.Status;

@Data
@SuperBuilder
public abstract class Display {

	public static final float CONTENT_START_X = 40f;
	public static final float CONTENT_START_Y = 550f;
	public static final float CONTENT_END_Y = 40f;
	public static final float DETAILED_CONTENT_END_Y = 20f;
	public static final float CONTENT_MARGIN_TOP_Y = 40f;

	public static final float HEADER_START_Y = 570f;
	public static final float HEADER_SECTION_DETAILS_START_X = 50f;
	public static final float HEADER_PAGE_NUMBER_START_X = 750f;

	public static final float TRIMMED_MESSAGE_START_Y = 20f;

	protected PDPageContentStream content;

	protected ReportConfig reportConfig;

	protected PDDocument document;

	protected PDPage page;

	protected DisplayData displayData;

	protected DestinationStore destinations;

	protected float ylocation;
	@Default
	protected float xlocation = CONTENT_START_X;

	public abstract void display();

	public Color statusColor(Status status) {

		Color color = Color.BLACK;
		if (status == Status.PASSED)
			color = reportConfig.passedColor();
		if (status == Status.FAILED)
			color = reportConfig.failedColor();
		if (status == Status.SKIPPED)
			color = reportConfig.skippedColor();
		return color;
	}
}
