package tech.grasshopper.pdf.section.dashboard;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.knowm.xchart.style.PieStyler;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.ImageCell;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.chart.ReportDonutChart;
import tech.grasshopper.pdf.data.SummaryData;
import tech.grasshopper.pdf.image.ImageCreator;
import tech.grasshopper.pdf.structure.Display;

@SuperBuilder
public class DashboardDonutDisplay extends Display {

	@Setter(value = AccessLevel.PACKAGE)
	private TableBuilder tableBuilder;

	private final SummaryData summaryData = (SummaryData) displayData;

	@Override
	public void display() {

		final PDImageXObject featuresDonut = createDonutChart(summaryData.getPassedFeatures(),
				summaryData.getFailedFeatures(), summaryData.getSkippedFeatures());

		final PDImageXObject scenariosDonut = createDonutChart(summaryData.getPassedScenarios(),
				summaryData.getFailedScenarios(), summaryData.getSkippedScenarios());

		final PDImageXObject stepsDonut = createDonutChart(summaryData.getPassedSteps(), summaryData.getFailedSteps(),
				summaryData.getSkippedSteps());

		tableBuilder.addRow(Row.builder().add(ImageCell.builder().image(featuresDonut).build())
				.add(DashboardDisplayUtil.spacerCell()).add(ImageCell.builder().image(scenariosDonut).build())
				.add(DashboardDisplayUtil.spacerCell()).add(ImageCell.builder().image(stepsDonut).build()).build())
				.addRow(DashboardDisplayUtil.spacerRow());
	}

	private PDImageXObject createDonutChart(Number passed, Number failed, Number skipped) {

		final ReportDonutChart chart = new ReportDonutChart(150, 150);
		updateDonutChartStyler(chart.getStyler());

		Map<String, Number> data = new HashMap<>();
		data.put("Passed", passed);
		data.put("Failed", failed);
		data.put("Skipped", skipped);
		chart.updateData(data);

		return ImageCreator.builder().chart(chart).document(document).build().generateChartImageXObject();
	}

	private void updateDonutChartStyler(PieStyler styler) {

		styler.setSumFontSize(20f);
		styler.setDonutThickness(0.35);
		styler.setSeriesColors(
				new Color[] { reportConfig.passedColor(), reportConfig.failedColor(), reportConfig.skippedColor() });
	}
}
