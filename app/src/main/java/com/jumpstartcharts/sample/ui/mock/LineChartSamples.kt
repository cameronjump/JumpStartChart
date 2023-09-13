package com.jumpstartcharts.sample.ui.mock

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jumpstartcharts.jumpstartcharts.composables.LineChart
import com.jumpstartcharts.jumpstartcharts.data.ChartDataset
import com.jumpstartcharts.jumpstartcharts.data.ChartPoint
import com.jumpstartcharts.jumpstartcharts.data.ScrubbingBehavior
import com.jumpstartcharts.jumpstartcharts.util.drawCubicGradientChart
import com.jumpstartcharts.jumpstartcharts.util.drawCubicLineChart
import com.jumpstartcharts.jumpstartcharts.util.drawLineGradientChart

@Composable
@Preview(showBackground = true)
fun LineChartSampleSingleDataset() {
    LineChart(
        datasets = listOf(MockGraphData.MOCK_LINE_CHART_DATASET_A),
        contentDescription = "",
    )
}

@Composable
@Preview(showBackground = true)
fun LineChartSampleEmptyDatasets() {
    LineChart(
        datasets = listOf<ChartDataset<ChartPoint>>(),
        contentDescription = "",
        scrubbingBehavior = ScrubbingBehavior.GraphTouchScrubbingBehavior(),
    )
}

@Composable
@Preview(showBackground = true)
fun LineChartSampleMultipleDatasets() {
    LineChart(
        datasets = listOf(
            MockGraphData.MOCK_LINE_CHART_DATASET_A,
            MockGraphData.MOCK_LINE_CHART_DATASET_B,
            MockGraphData.MOCK_LINE_CHART_DATASET_C,
            MockGraphData.MOCK_LINE_CHART_DATASET_D,
        ),
        contentDescription = "",
        scrubbingBehavior = ScrubbingBehavior.GraphTouchScrubbingBehavior(),
    )
}

@Composable
@Preview(showBackground = true)
fun LineChartSampleMultipleGradientDatasets() {
    LineChart(
        datasets = listOf(
            MockGraphData.MOCK_LINE_CHART_DATASET_A,
            MockGraphData.MOCK_LINE_CHART_DATASET_B,
            MockGraphData.MOCK_LINE_CHART_DATASET_C,
            MockGraphData.MOCK_LINE_CHART_DATASET_D,
        ),
        drawPoints = { data, boundsData ->
            drawLineGradientChart(
                data,
                boundsData.xStart,
                boundsData.xEnd,
                boundsData.yBottom,
            )
        },
        scrubbingBehavior = ScrubbingBehavior.GraphTouchScrubbingBehavior(),
        contentDescription = "",
    )
}

@Composable
@Preview(showBackground = true)
fun LineChartSampleMultipleCubicLineDatasets() {
    LineChart(
        datasets = listOf(
            MockGraphData.MOCK_LINE_CHART_DATASET_A,
            MockGraphData.MOCK_LINE_CHART_DATASET_B,
            MockGraphData.MOCK_LINE_CHART_DATASET_C,
            MockGraphData.MOCK_LINE_CHART_DATASET_D,
        ),
        drawPoints = { data, _ ->
            drawCubicLineChart(
                data,
            )
        },
        contentDescription = "",
        scrubbingBehavior = ScrubbingBehavior.GraphTouchScrubbingBehavior(),
    )
}

@Composable
@Preview(showBackground = true)
fun LineChartSampleMultipleCubicGradientDatasets() {
    LineChart(
        datasets = listOf(
            MockGraphData.MOCK_LINE_CHART_DATASET_A,
            MockGraphData.MOCK_LINE_CHART_DATASET_B,
            MockGraphData.MOCK_LINE_CHART_DATASET_C,
            MockGraphData.MOCK_LINE_CHART_DATASET_D,
        ),
        drawPoints = { data, boundsData ->
            drawCubicGradientChart(
                data,
                boundsData.xStart,
                boundsData.xEnd,
                boundsData.yBottom,
            )
        },
        contentDescription = "",
        scrubbingBehavior = ScrubbingBehavior.GraphTouchScrubbingBehavior(),
    )
}

@Composable
@Preview(showBackground = true)
fun LineChartSampleChangeHeight() {
    LineChart(
        datasets = listOf(MockGraphData.MOCK_LINE_CHART_DATASET_A),
        canvasHeight = 100.dp,
        contentDescription = "",
        scrubbingBehavior = ScrubbingBehavior.GraphTouchScrubbingBehavior(),
    )
}

@Composable
@Preview(showBackground = true)
fun LineChartSampleChangeYAxisIntervals() {
    LineChart(
        datasets = listOf(MockGraphData.MOCK_LINE_CHART_DATASET_A),
        scrubbingBehavior = ScrubbingBehavior.GraphTouchScrubbingBehavior(),
        numOfYAxisIntervals = 10,
        contentDescription = "",
    )
}

@Composable
@Preview(showBackground = true)
fun LineChartSampleNoGuidelinesLines() {
    LineChart(
        datasets = listOf(MockGraphData.MOCK_LINE_CHART_DATASET_A),
        scrubbingBehavior = ScrubbingBehavior.GraphTouchScrubbingBehavior(),
        drawXAxis = { _, _, _ -> },
        drawYAxis = { _, _, _ -> },
        drawHorizontalGridLine = { _, _, _, _ -> },
        contentDescription = "",
    )
}

@Composable
@Preview(showBackground = true)
fun LineChartSampleXAxisLabels() {
    LineChart(
        datasets = listOf(MockGraphData.MOCK_LINE_CHART_DATASET_A),
        scrubbingBehavior = ScrubbingBehavior.GraphTouchScrubbingBehavior(),
        formatXAxisLabel = { it.toString() },
        contentDescription = "",
    )
}

val lineChartSamples = listOf(
    SampleItem("Single dataset") {
        LineChartSampleSingleDataset()
    },
    SampleItem("Multiple datasets") {
        LineChartSampleMultipleDatasets()
    },
    SampleItem("Multiple gradients") {
        LineChartSampleMultipleGradientDatasets()
    },
    SampleItem("Multiple cubic gradients") {
        LineChartSampleMultipleCubicGradientDatasets()
    },
    SampleItem("Multiple cubic") {
        LineChartSampleMultipleCubicLineDatasets()
    },
    SampleItem("Change height") {
        LineChartSampleChangeHeight()
    },
    SampleItem("With X axis labels") {
        LineChartSampleXAxisLabels()
    },
    SampleItem("Change Y axis intervals") {
        LineChartSampleChangeYAxisIntervals()
    },
)

@Composable
@Preview(showBackground = true)
fun LineChartSampleScreen() {
    SampleScreen(samples = lineChartSamples)
}
