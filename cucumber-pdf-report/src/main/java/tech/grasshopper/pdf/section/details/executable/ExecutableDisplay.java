package tech.grasshopper.pdf.section.details.executable;

import static tech.grasshopper.pdf.section.details.executable.DummyCellDisplay.dummyCellLeftBorder;
import static tech.grasshopper.pdf.section.details.executable.DummyCellDisplay.dummyCellRightBorder;

import java.awt.Color;

import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.optimizer.TextSanitizer;
import tech.grasshopper.pdf.pojo.cucumber.Executable;
import tech.grasshopper.pdf.pojo.cucumber.Status;
import tech.grasshopper.pdf.structure.Display;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public abstract class ExecutableDisplay extends Display {

	protected Executable executable;

	protected TableBuilder tableBuilder;

	protected int sNo;

	@Override
	public void display() {

		TextSanitizer sanitizer = TextSanitizer.builder().build();

		tableBuilder.addRow(Row.builder()
				.add(TextCell.builder().text(getSerialNumber()).borderColor(Color.GRAY).borderWidthLeft(1f)
						.borderWidthTop(1f).build())
				.add(TextCell.builder().text(sanitizer.sanitizeText(executableName())).textColor(executableNameColor())
						.backgroundColor(executableBackgroundColor()).borderColor(Color.GRAY).borderWidth(1).build())
				.add(TextCell.builder().text(executable.getStatus().toString())
						.textColor(statusColor(executable.getStatus())).borderColor(Color.GRAY).borderWidthRight(1f)
						.borderWidthTop(1f).build())
				.add(TextCell.builder().text(getDuration())
						.textColor(reportConfig.getDetailedStepHookConfig().durationColor()).borderColor(Color.GRAY)
						.borderWidthRight(1f).borderWidthTop(1f).build())
				.build());

		displaySubTypeDetails();

		displayLogMessage();

		displayStackTrace();

		displayMedia();
	}

	public abstract int processSNo(int serialNum);

	protected abstract String getSerialNumber();

	protected abstract String getDuration();

	public abstract String executableName();

	protected abstract Color executableNameColor();

	protected abstract Color executableBackgroundColor();

	protected int getSubTypeRowSpanCount() {
		return 0;
	}

	protected void displaySubTypeDetails() {

	}

	protected void displayLogMessage() {

		if (executable.getOutput().isEmpty())
			return;

		LogMessageDisplay.builder().executable(executable).tableBuilder(tableBuilder)
				.color(reportConfig.getDetailedStepHookConfig().logMsgColor()).build().display();
	}

	protected void displayStackTrace() {

		if (executable.getErrorMessage() == null || executable.getErrorMessage().isEmpty())
			return;

		// Removing standard message from adapters. LAME HACK!!
		if (executable.getStatus() == Status.SKIPPED && executable.getErrorMessage().equalsIgnoreCase("Step Skipped"))
			return;

		tableBuilder.addRow(Row.builder().add(dummyCellLeftBorder())
				.add(StackTraceDisplay.builder().executable(executable)
						.color(reportConfig.getDetailedStepHookConfig().errorMsgColor()).build().display())
				.add(dummyCellRightBorder()).add(dummyCellRightBorder()).build());
	}

	@SneakyThrows
	protected void displayMedia() {
		if (executable.getMedia().isEmpty())
			return;

		tableBuilder.addRow(Row.builder().add(dummyCellLeftBorder())
				.add(MediaDisplay.builder().executable(executable).document(document)
						.attachView(reportConfig.isDisplayAttached()).expandView(reportConfig.isDisplayExpanded())
						.build().display())
				.add(dummyCellRightBorder()).add(dummyCellRightBorder()).build());
	}
}
