package tech.grasshopper.pdf.chapter.detailed;

import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.pojo.cucumber.Hook;
import tech.grasshopper.pdf.pojo.cucumber.Status;
import tech.grasshopper.pdf.util.DateUtil;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class HookRow extends StepOrHookRow {

	private Hook hook;

	@Override
	public ParagraphCell generateTextOutput() {
		ParagraphBuilder paragraphBuilder = Paragraph.builder();

		generateName(paragraphBuilder, hook.getHookType().toString().toLowerCase() + " - " + hook.getLocation(),
				reportConfig.getDetailedStepHookConfig().hookTextColor());
		ErrorMessageComponent.builder().stackTrace(hook.getErrorMessage())
				.textColor(reportConfig.getDetailedStepHookConfig().errorMsgColor()).build()
				.componentText(paragraphBuilder);
		LogMessageComponent.builder().logMessages(hook.getOutput())
				.textColor(reportConfig.getDetailedStepHookConfig().logMsgColor())
				.hasRowOrStringOrError(dividerRequired()).build().componentText(paragraphBuilder);
		MediaMessageComponent.builder().mediaMessages(hook.getMedia())
				.textColor(reportConfig.getDetailedStepHookConfig().mediaMsgColor())
				.hasRowOrStringOrError(dividerRequired()).build().componentText(paragraphBuilder);

		return generateParagraphCell(paragraphBuilder);
	}

	@Override
	public boolean dividerRequired() {
		return !hook.getOutput().isEmpty() || !hook.getMedia().isEmpty();
	}

	@Override
	public int getRowHeight() {
		int height = NAME_ROW_HEIGHT;
		height = height + ErrorMessageComponent.builder().stackTrace(hook.getErrorMessage()).build().componentHeight();
		height = height + LogMessageComponent.builder().logMessages(hook.getOutput())
				.hasRowOrStringOrError(dividerRequired()).build().componentHeight();
		height = height + MediaMessageComponent.builder().mediaMessages(hook.getMedia())
				.hasRowOrStringOrError(dividerRequired()).build().componentHeight();
		return height;
	}

	@Override
	public Status getStatus() {
		return hook.getStatus();
	}

	@Override
	public String getDuration() {
		return DateUtil.durationValue(hook.getDuration());
	}

	@Override
	protected String getSerialNo() {
		return "";
	}
}
