package com.jumpstartcharts.jumpstartcharts.composables

import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.jumpstartcharts.jumpstartcharts.data.ChartDataset
import com.jumpstartcharts.jumpstartcharts.data.ChartPoint
import com.jumpstartcharts.jumpstartcharts.mock.MockGraphData
import com.jumpstartcharts.jumpstartcharts.util.drawCubicGradientChart
import com.jumpstartcharts.jumpstartcharts.util.drawCubicLineChart
import com.jumpstartcharts.jumpstartcharts.util.drawLineGradientChart
import org.junit.Rule
import org.junit.Test

class LineChartKtTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
        // ...see docs for more options
    )

    @Test
    fun testSingleDataset() {
        paparazzi.snapshot {
            LineChart(
                datasets = listOf(MockGraphData.MOCK_LINE_CHART_DATASET_A),
                contentDescription = "",
            )
        }
    }

    @Test
    fun testEmptyDatasets() {
        paparazzi.snapshot {
            LineChart(
                datasets = listOf<ChartDataset<ChartPoint>>(),
                contentDescription = "",
            )
        }
    }

    @Test
    fun testMultipleDatasets() {
        paparazzi.snapshot {
            LineChart(
                datasets = listOf(
                    MockGraphData.MOCK_LINE_CHART_DATASET_A,
                    MockGraphData.MOCK_LINE_CHART_DATASET_B,
                    MockGraphData.MOCK_LINE_CHART_DATASET_C,
                    MockGraphData.MOCK_LINE_CHART_DATASET_D,
                ),
                contentDescription = "",
            )
        }
    }

    @Test
    fun testMultipleGradientDatasets() {
        paparazzi.snapshot {
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
                contentDescription = "",
            )
        }
    }

    @Test
    fun testMultipleCubicLineDatasets() {
        paparazzi.snapshot {
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
            )
        }
    }

    @Test
    fun testMultipleCubicGradientDatasets() {
        paparazzi.snapshot {
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
            )
        }
    }

    @Test
    fun testChangeHeight() {
        paparazzi.snapshot {
            LineChart(
                datasets = listOf(MockGraphData.MOCK_LINE_CHART_DATASET_A),
                canvasHeight = 100.dp,
                contentDescription = "",
            )
        }
    }

    @Test
    fun testChangeYAxisIntervals() {
        paparazzi.snapshot {
            LineChart(
                datasets = listOf(MockGraphData.MOCK_LINE_CHART_DATASET_A),
                numOfYAxisIntervals = 10,
                contentDescription = "",
            )
        }
    }

    @Test
    fun testDrawNoLines() {
        paparazzi.snapshot {
            LineChart(
                datasets = listOf(MockGraphData.MOCK_LINE_CHART_DATASET_A),
                drawXAxis = { _, _, _ -> },
                drawYAxis = { _, _, _ -> },
                drawHorizontalGridLine = { _, _, _, _ -> },
                contentDescription = "",
            )
        }
    }
}
