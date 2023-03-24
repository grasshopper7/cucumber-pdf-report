package tech.grasshopper.pdf.section.scenario.nopass;

import java.awt.Color;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.data.ScenarioData;
import tech.grasshopper.pdf.destination.Destination;
import tech.grasshopper.pdf.destination.DestinationAware;
import tech.grasshopper.pdf.optimizer.TextLengthOptimizer;
import tech.grasshopper.pdf.outline.Outline;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;
import tech.grasshopper.pdf.section.scenario.ScenarioStepDetails;
import tech.grasshopper.pdf.structure.Display;
import tech.grasshopper.pdf.structure.PageCreator;
import tech.grasshopper.pdf.structure.TableCreator;
import tech.grasshopper.pdf.util.TextUtil;

@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class NoPassScenarioDisplay extends ScenarioStepDetails implements DestinationAware {

	private static final int TABLE_X_AXIS_START = 40;
	private static final int TABLE_Y_AXIS_START = 530;

	private static final float FEATURE_NAME_COLUMN_WIDTH = 350f;
	private static final float SCENARIO_NAME_COLUMN_WIDTH = 380f;

	public static final float TABLE_SPACE = TABLE_Y_AXIS_START - Display.CONTENT_END_Y;

	public static final TextLengthOptimizer featureNameTextOptimizer = TextLengthOptimizer.builder().font(NAME_FONT)
			.fontsize(NAME_FONT_SIZE).availableSpace(FEATURE_NAME_COLUMN_WIDTH - 2 * DATA_PADDING).maxLines(2).build();

	public static final TextLengthOptimizer scenarioNameTextOptimizer = TextLengthOptimizer.builder().font(NAME_FONT)
			.fontsize(NAME_FONT_SIZE).availableSpace(SCENARIO_NAME_COLUMN_WIDTH - 2 * DATA_PADDING).maxLines(2).build();

	public static final TextUtil headerRowTextUtil = TextUtil.builder().font(HEADER_FONT).fontSize(HEADER_FONT_SIZE)
			.text("Scenario Name").width(SCENARIO_NAME_COLUMN_WIDTH).padding(HEADER_PADDING).build();

	public static final TextUtil featureNameTextUtil = TextUtil.builder().font(NAME_FONT).fontSize(NAME_FONT_SIZE)
			.text("").width(FEATURE_NAME_COLUMN_WIDTH).padding(DATA_PADDING).build();

	public static final TextUtil scenarioNameTextUtil = TextUtil.builder().font(NAME_FONT).fontSize(NAME_FONT_SIZE)
			.text("").width(SCENARIO_NAME_COLUMN_WIDTH).padding(DATA_PADDING).build();

	protected TextLengthOptimizer featureNameTextOptimizer() {
		return featureNameTextOptimizer;
	}

	protected TextLengthOptimizer scenarioNameTextOptimizer() {
		return scenarioNameTextOptimizer;
	}

	@Override
	@SneakyThrows
	public void display() {

		page = PageCreator.builder().document(document).build()
				.createLandscapePageWithHeaderAndNumberAndAddToDocument("FAIL & SKIP SCENARIOS");

		content = new PDPageContentStream(document, page, AppendMode.APPEND, true);

		createTableBuilder();
		createHeaderRow();
		createDataRows();
		drawTable();
		croppedMessageDisplay();
		createDestination();

		content.close();
	}

	private void createTableBuilder() {

		tableBuilder = Table.builder().addColumnsOfWidth(25f, FEATURE_NAME_COLUMN_WIDTH, SCENARIO_NAME_COLUMN_WIDTH)
				.borderColor(Color.LIGHT_GRAY).borderWidth(1);
	}

	private void createHeaderRow() {

		tableBuilder.addRow(Row.builder().padding(HEADER_PADDING).horizontalAlignment(HorizontalAlignment.CENTER)
				.verticalAlignment(VerticalAlignment.MIDDLE).font(HEADER_FONT).fontSize(HEADER_FONT_SIZE)
				.add(TextCell.builder().text("#").build())
				.add(TextCell.builder().text("Feature Name").horizontalAlignment(HorizontalAlignment.LEFT).build())
				.add(TextCell.builder().text("Scenario Name").horizontalAlignment(HorizontalAlignment.LEFT).build())
				.build());
	}

	private void createDataRows() {

		int sNo = paginationData.getItemFromIndex() + 1;
		ScenarioData scenarioData = (ScenarioData) displayData;
		List<Scenario> scenarios = scenarioData.getScenarios();
		int featureRowSpanSize = featureNameRowSpans.size();

		for (int i = 0; i < scenarios.size(); i++) {
			Scenario scenario = scenarios.get(i);

			if (featureNameTextOptimizer.isTextTrimmed() || scenarioNameTextOptimizer.isTextTrimmed())
				nameCropped = true;

			tableBuilder.addRow(Row.builder().padding(DATA_PADDING).font(NAME_FONT).fontSize(NAME_FONT_SIZE)
					.horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.TOP)

					.add(TextCell.builder().text(String.valueOf(sNo)).fontSize(8).build())

					.add(createFeatureNameCell(scenario.getFeature(), featureNameRowSpans.get(i),
							i == featureRowSpanSize - 1))
					.add(createScenarioNameCell(scenario)).build());
			sNo++;
		}
	}

	private void drawTable() {

		TableCreator tableDrawer = TableCreator.builder().tableBuilder(tableBuilder).document(document)
				.startX(TABLE_X_AXIS_START).startY(TABLE_Y_AXIS_START)
				.pageSupplier(PageCreator.builder().document(document).build().landscapePageSupplier()).build();
		tableDrawer.displayTable();
	}

	protected boolean annotationFilter() {
		return reportConfig.isDisplayDetailed();
	}

	@Override
	public void createDestination() {
		Destination destination = Destination.builder()
				.name(Outline.FAIL_SCENARIOS_SECTION_TEXT + " - " + (paginationData.getItemFromIndex() + 1)
						+ " to " + paginationData.getItemToIndex())
				.yCoord((int) page.getMediaBox().getHeight()).page(page).build();
		destinations.addFailSkipScenarioDestination(destination);
	}
}
