package com.jumpstartcharts.sample.ui.mock

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jumpstartcharts.jumpstartcharts.composables.BarChart
import com.jumpstartcharts.jumpstartcharts.data.ChartDataset
import com.jumpstartcharts.jumpstartcharts.data.ChartPoint
import com.jumpstartcharts.jumpstartcharts.data.ScrubbingBehavior

@Composable
@Preview(showBackground = true)
fun BarChartSampleSingleDataset() {
    BarChart(
        datasets = listOf(MockGraphData.MOCK_BAR_CHART_DATASET_A),
        contentDescription = "",
    )
}

@Composable
@Preview(showBackground = true)
fun BarChartSampleEmptyDatasets() {
    BarChart(
        datasets = listOf<ChartDataset<ChartPoint>>(),
        contentDescription = "",
        scrubbingBehavior = ScrubbingBehavior.GraphTouchScrubbingBehavior(),
    )
}

@Composable
@Preview(showBackground = true)
fun BarChartSampleMultipleDatasets() {
    BarChart(
        datasets = listOf(
            MockGraphData.MOCK_BAR_CHART_DATASET_A,
            MockGraphData.MOCK_BAR_CHART_DATASET_B,
        ),
        contentDescription = "",
        scrubbingBehavior = ScrubbingBehavior.GraphTouchScrubbingBehavior(),
    )
}

@Composable
@Preview(showBackground = true)
fun BarChartSampleOneItemMultipleDatasets() {
    BarChart(
        datasets = listOf(

            MockGraphData.MOCK_BAR_CHART_DATASET_C,
            MockGraphData.MOCK_BAR_CHART_DATASET_D,
        ),
        contentDescription = "",
        scrubbingBehavior = ScrubbingBehavior.GraphTouchScrubbingBehavior(),
    )
}

@Composable
@Preview(showBackground = true)
fun BarChartSampleChangeHeight() {
    BarChart(
        datasets = listOf(MockGraphData.MOCK_BAR_CHART_DATASET_A),
        canvasHeight = 100.dp,
        contentDescription = "",
        scrubbingBehavior = ScrubbingBehavior.GraphTouchScrubbingBehavior(),
    )
}

@Composable
@Preview(showBackground = true)
fun BarChartSampleChangeYAxisIntervals() {
    BarChart(
        datasets = listOf(MockGraphData.MOCK_BAR_CHART_DATASET_A),
        scrubbingBehavior = ScrubbingBehavior.GraphTouchScrubbingBehavior(),
        numOfYAxisIntervals = 10,
        contentDescription = "",
    )
}

@Composable
@Preview(showBackground = true)
fun BarChartSampleNoGuidelinesBars() {
    BarChart(
        datasets = listOf(MockGraphData.MOCK_BAR_CHART_DATASET_A),
        scrubbingBehavior = ScrubbingBehavior.GraphTouchScrubbingBehavior(),
        drawXAxis = { _, _, _ -> },
        drawYAxis = { _, _, _ -> },
        drawHorizontalGridLine = { _, _, _, _ -> },
        contentDescription = "",
    )
}

@Composable
@Preview(showBackground = true)
fun BarChartSampleXAxisLabels() {
    BarChart(
        datasets = listOf(MockGraphData.MOCK_BAR_CHART_DATASET_A),
        scrubbingBehavior = ScrubbingBehavior.GraphTouchScrubbingBehavior(),
        formatXAxisLabel = { it.toString() },
        contentDescription = "",
    )
}

val barChartSamples = listOf(
    SampleItem("Single dataset") {
        BarChartSampleSingleDataset()
    },
    SampleItem("Multiple datasets") {
        BarChartSampleMultipleDatasets()
    },
    SampleItem("One item multiple datasets") {
        BarChartSampleOneItemMultipleDatasets()
    },
    SampleItem("Change height") {
        BarChartSampleChangeHeight()
    },
    SampleItem("With X axis labels") {
        BarChartSampleXAxisLabels()
    },
    SampleItem("Change Y axis intervals") {
        BarChartSampleChangeYAxisIntervals()
    },
)

@Composable
@Preview(showBackground = true)
fun BarChartSampleScreen() {
    SampleScreen(samples = barChartSamples)
}
