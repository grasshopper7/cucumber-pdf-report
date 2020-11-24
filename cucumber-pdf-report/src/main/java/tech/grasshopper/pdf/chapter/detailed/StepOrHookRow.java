package tech.grasshopper.pdf.chapter.detailed;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.cell.TextCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.config.ReportConfig;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.pojo.cucumber.Hook;
import tech.grasshopper.pdf.pojo.cucumber.Status;
import tech.grasshopper.pdf.pojo.cucumber.Step;

@Data
@SuperBuilder
public abstract class StepOrHookRow {

	protected static final PDFont FONT = ReportFont.ITALIC_FONT;
	protected static final int FONT_SIZE = 10;
	protected static final int COLUMN_WIDTH = 315;
	protected static final int PADDING = 5;

	public static final int HEADER_ROW_HEIGHT = 22;
	public static final int NAME_ROW_HEIGHT = 20;

	protected String sNo;
	protected ReportConfig reportConfig;

	@Builder.Default
	protected final TextLengthOptimizer nameOptimizer = TextLengthOptimizer.builder().font(FONT).fontsize(FONT_SIZE)
			.spaceWidth(COLUMN_WIDTH - 2 * PADDING).build();

	protected Row generateRow() {
		return Row.builder().font(FONT).fontSize(FONT_SIZE).height((float) getRowHeight())
				.horizontalAlignment(HorizontalAlignment.LEFT)
				.add(TextCell.builder().text(getSerialNo()).fontSize(9).build()).add(generateTextOutput())
				.add(TextCell.builder().text(getStatus().toString()).textColor(getStatusColor(reportConfig)).fontSize(9)
						.horizontalAlignment(HorizontalAlignment.CENTER).build())
				.add(TextCell.builder().text(getDuration())
						.textColor(reportConfig.getDetailedStepHookConfig().durationColor()).build())
				.build();
	}

	protected String getSerialNo() {
		return sNo;
	}

	protected abstract int getRowHeight();

	protected abstract ParagraphCell generateTextOutput();

	protected abstract Status getStatus();

	protected Color getStatusColor(ReportConfig reportConfig) {
		Color color = Color.BLACK;
		Status status = getStatus();
		if (status == Status.PASSED)
			color = reportConfig.passedColor();
		else if (status == Status.FAILED)
			color = reportConfig.failedColor();
		else if (status == Status.SKIPPED)
			color = reportConfig.skippedColor();
		return color;
	}

	protected abstract String getDuration();

	protected abstract boolean dividerRequired();

	protected void generateName(ParagraphBuilder paragraphBuilder, String name, Color textColor) {
		StyledText stepTextStyler = StyledText.builder().text(nameOptimizer.optimizeText(name)).color(textColor)
				.build();
		paragraphBuilder.append(stepTextStyler).appendNewLine(1);
	}

	protected ParagraphCell generateParagraphCell(ParagraphBuilder paraBuilder) {
		return ParagraphCell.builder().paddingLeft(5).paddingRight(5).lineSpacing(1.45f).paragraph(paraBuilder.build())
				.build();
	}

	protected int incrementSerialNumber(int sno) {
		return sno;
	}

	public int decrementStepCount(int cnt) {
		return cnt;
	}

	public static StepOrHookRow createStepOrHook(Step step) {
		return StepRow.builder().step(step).build();
	}

	public static StepOrHookRow createStepOrHook(Hook hook) {
		return HookRow.builder().hook(hook).build();
	}
}
