package tech.grasshopper.pdf.chapter.detailed;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.chapter.Chapter;
import tech.grasshopper.pdf.component.Component;
import tech.grasshopper.pdf.data.FeatureData;
import tech.grasshopper.pdf.pojo.cucumber.Feature;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class DetailedChapter extends Chapter {

	@Default
	private int contentHeight = 0;

	private FeatureData detailedData;

	@Override
	public void createChapter() {
		createAndSendPageData();
	}

	public void createAndSendPageData() {
		List<Component> components = new ArrayList<>();
		List<StepOrHookRow> rows = new ArrayList<>();

		detailedData = (FeatureData) displayData;
		for (Feature feature : detailedData.getFeatures()) {
			if (!checkComponentFitment(DetailedFeatureComponent.CONTENT_HEIGHT)) {
				sendToPage(components);
				components = new ArrayList<>();
				contentHeight = 0;
			}

			components.add(
					DetailedFeatureComponent.builder().document(document).content(content).reportConfig(reportConfig)
							.feature(feature).startHeight(DetailedPage.CONTENT_Y_START - contentHeight).build());
			contentHeight = contentHeight + DetailedFeatureComponent.CONTENT_HEIGHT;

			for (Scenario scenario : feature.getScenarios()) {
				if (!checkComponentFitment(DetailedScenarioComponent.CONTENT_HEIGHT)) {
					sendToPage(components);
					components = new ArrayList<>();
					contentHeight = 0;
				}

				components.add(DetailedScenarioComponent.builder().document(document).content(content)
						.reportConfig(reportConfig).feature(feature).scenario(scenario)
						.startHeight(DetailedPage.CONTENT_Y_START - contentHeight).build());
				contentHeight = contentHeight + DetailedScenarioComponent.CONTENT_HEIGHT;

				rows = new ArrayList<>();
				int stepCompHeight = contentHeight;
				int initialSno = 1;
				int stepCnt = 0;

				for (int i = 0; i < scenario.getAllStepsAndHooks().size(); i++) {
					StepOrHookRow row = scenario.getAllStepsAndHooks().get(i);
					row.setReportConfig(reportConfig);
					
					rows.add(row);
					int rowHeight = row.getRowHeight();

					if (rows.size() == 1)
						rowHeight = rowHeight + StepOrHookRow.HEADER_ROW_HEIGHT;
					stepCnt = row.incrementSerialNumber(stepCnt);

					if (!checkComponentFitment(rowHeight)) {
						rows.remove(rows.size() - 1);
						stepCnt = row.decrementStepCount(stepCnt);
						if (!rows.isEmpty())
							components.add(DetailedRowComponent.builder().content(content).reportConfig(reportConfig)
									.rows(rows).initialSno(initialSno)
									.startHeight(DetailedPage.CONTENT_Y_START - stepCompHeight).build());
						sendToPage(components);

						rows = new ArrayList<>();
						rows.add(row);
						components = new ArrayList<>();
						stepCompHeight = 0;
						contentHeight = rowHeight + StepOrHookRow.HEADER_ROW_HEIGHT;
						initialSno = initialSno + stepCnt;
					} else
						contentHeight = contentHeight + rowHeight;
				}

				if (!rows.isEmpty()) {
					components.add(DetailedRowComponent.builder().content(content).reportConfig(reportConfig).rows(rows)
							.initialSno(initialSno).startHeight(DetailedPage.CONTENT_Y_START - stepCompHeight).build());
					rows = new ArrayList<>();
				}
			}
		}

		if (!components.isEmpty())
			sendToPage(components);
	}

	private boolean checkComponentFitment(int componentHeight) {
		if (contentHeight + componentHeight > DetailedPage.CONTENT_HEIGHT)
			return false;
		return true;
	}

	private void sendToPage(List<Component> components) {
		DetailedPage.builder().components(components).document(document).destinations(destinations).build()
				.createPage();
	}
}
