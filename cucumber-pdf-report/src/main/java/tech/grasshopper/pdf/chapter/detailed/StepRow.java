package tech.grasshopper.pdf.chapter.detailed;

import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.pojo.cucumber.Status;
import tech.grasshopper.pdf.pojo.cucumber.Step;
import tech.grasshopper.pdf.util.DateUtil;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class StepRow extends StepOrHookRow {

	private Step step;

	@Override
	public ParagraphCell generateTextOutput() {
		ParagraphBuilder paragraphBuilder = Paragraph.builder();

		generateName(paragraphBuilder, step.getName(), reportConfig.getDetailedStepHookConfig().stepTextColor());
		DataTableComponent.builder().rows(step.getRows()).build().componentText(paragraphBuilder);
		DocStringComponent.builder().docString(step.getDocString()).build().componentText(paragraphBuilder);
		ErrorMessageComponent.builder().stackTrace(step.getErrorMessage())
				.textColor(reportConfig.getDetailedStepHookConfig().errorMsgColor()).build()
				.componentText(paragraphBuilder);
		LogMessageComponent.builder().logMessages(step.getOutput())
				.textColor(reportConfig.getDetailedStepHookConfig().logMsgColor())
				.hasRowOrStringOrError(dividerRequired()).build().componentText(paragraphBuilder);
		MediaMessageComponent.builder().mediaMessages(step.getMedia())
				.textColor(reportConfig.getDetailedStepHookConfig().mediaMsgColor())
				.hasRowOrStringOrError(dividerRequired()).build().componentText(paragraphBuilder);

		return generateParagraphCell(paragraphBuilder);
	}

	@Override
	public int getRowHeight() {
		int height = NAME_ROW_HEIGHT;
		height = height + ErrorMessageComponent.builder().stackTrace(step.getErrorMessage()).build().componentHeight();
		height = height + DataTableComponent.builder().rows(step.getRows()).build().componentHeight();
		height = height + DocStringComponent.builder().docString(step.getDocString()).build().componentHeight();
		height = height + LogMessageComponent.builder().logMessages(step.getOutput())
				.hasRowOrStringOrError(dividerRequired()).build().componentHeight();
		height = height + MediaMessageComponent.builder().mediaMessages(step.getMedia())
				.hasRowOrStringOrError(dividerRequired()).build().componentHeight();
		return height;
	}

	@Override
	public boolean dividerRequired() {
		return !step.getRows().isEmpty() || step.getDocString() != null || !step.getOutput().isEmpty()
				|| !step.getMedia().isEmpty();
	}

	@Override
	public Status getStatus() {
		return step.getStatus();
	}

	@Override
	public String getDuration() {
		return DateUtil.durationValue(step.getDuration());
	}

	@Override
	protected int incrementSerialNumber(int sno) {
		return sno + 1;
	}

	@Override
	public int decrementStepCount(int cnt) {
		return cnt - 1;
	}
}
