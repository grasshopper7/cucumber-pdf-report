package tech.grasshopper.pdf.section.details;

import java.awt.Color;

import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.pojo.cucumber.Executable;
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

		int rowSpan = getRowSpan();

		tableBuilder.addRow(Row.builder().add(TextCell.builder().rowSpan(rowSpan).text(getSerialNumber()).build())
				.add(TextCell.builder().text(executableName()).textColor(executableNameColor()).build())
				.add(TextCell.builder().rowSpan(rowSpan).text(executable.getStatus().toString())
						.textColor(statusColor(executable.getStatus())).build())
				.add(TextCell.builder().rowSpan(rowSpan).text(getDuration())
						.textColor(reportConfig.getDetailedStepHookConfig().durationColor()).build())
				.build());

		displaySubTypeDetails();

		displayLogMessage();

		displayStackTrace();

		displayMedia();
	}

	protected abstract int processSNo(int serialNum);

	protected abstract String getSerialNumber();

	protected abstract String getDuration();

	protected abstract String executableName();

	protected abstract Color executableNameColor();

	protected int getSubTypeRowSpanCount() {
		return 0;
	}

	protected void displaySubTypeDetails() {

	}

	protected int getRowSpan() {

		int rowSpan = 1 + getSubTypeRowSpanCount();

		if (!executable.getOutput().isEmpty())
			rowSpan++;

		if (executable.getErrorMessage() != null && !executable.getErrorMessage().isEmpty())
			rowSpan++;

		if (!executable.getMedia().isEmpty())
			rowSpan++;

		return rowSpan;
	}

	protected void displayLogMessage() {

		if (executable.getOutput().isEmpty())
			return;

		tableBuilder
				.addRow(Row.builder()
						.add(LogMessageDisplay.builder().executable(executable)
								.color(reportConfig.getDetailedStepHookConfig().logMsgColor()).build().display())
						.build());
	}

	protected void displayStackTrace() {

		if (executable.getErrorMessage() == null || executable.getErrorMessage().isEmpty())
			return;

		tableBuilder
				.addRow(Row.builder()
						.add(StackTraceDisplay.builder().executable(executable)
								.color(reportConfig.getDetailedStepHookConfig().errorMsgColor()).build().display())
						.build());
	}

	@SneakyThrows
	protected void displayMedia() {

		if (executable.getMedia().isEmpty())
			return;

		tableBuilder.addRow(Row.builder()
				.add(MediaDisplay.builder().executable(executable).document(document).build().display()).build());
	}
}
