package com.jumpstartcharts.jumpstartcharts.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jumpstartcharts.jumpstartcharts.data.ChartDataset
import com.jumpstartcharts.jumpstartcharts.data.ChartPoint
import com.jumpstartcharts.jumpstartcharts.data.ChartPositionPoint
import com.jumpstartcharts.jumpstartcharts.data.ChartSelectedValue
import com.jumpstartcharts.jumpstartcharts.data.ScrubbingBehavior
import com.jumpstartcharts.jumpstartcharts.mock.MockGraphData
import com.jumpstartcharts.jumpstartcharts.util.ChartRangeCalculator
import com.jumpstartcharts.jumpstartcharts.util.defaultDrawHorizontalGridLine
import com.jumpstartcharts.jumpstartcharts.util.defaultXAxis
import com.jumpstartcharts.jumpstartcharts.util.defaultYAxis
import com.jumpstartcharts.jumpstartcharts.util.getDefaultAxisLabelPaint

/**
 * Pass in lambdas to change functionality.
 * Provides reason defaults to make [BaseChart] a Bar Chart
 */
@Composable
fun <Point : ChartPoint> ComposableBarChart(
    datasets: List<ChartDataset<Point>>,
    contentDescription: String,
    yValueRangeMin: Float = ChartRangeCalculator.defaultLowerBound(datasets),
    yValueRangeMax: Float = ChartRangeCalculator.defaultUpperBound(datasets),
    formatXAxisLabel: (value: Long) -> String? = { null },
    formatYAxisLabel: (value: Float) -> String? = { it.toInt().toString() },
    updateHoistedSelectedValue: (ChartSelectedValue?) -> Unit = {},
    hoistIsBeingDragged: (Boolean) -> Unit = {},
    canvasHeight: Dp = 300.dp,
    horizontalChartMargin: Dp = 16.dp,
    numOfYAxisIntervals: Int = 5,
    xAxisLabelPaint: NativePaint = getDefaultAxisLabelPaint(LocalContext.current),
    yAxisLabelPaint: NativePaint = getDefaultAxisLabelPaint(LocalContext.current),
    drawXAxis: DrawScope.(x1: Float, x2: Float, y: Float) -> Unit = { x1, x2, y ->
        defaultXAxis(x1, x2, y)
    },
    drawYAxis: DrawScope.(y1: Float, y2: Float, x: Float) -> Unit = { y1, y2, x ->
        defaultYAxis(y1, y2, x)
    },
    drawHorizontalGridLine: DrawScope.(
        xStart: Float,
        xEnd: Float,
        yPos: Float,
        yValue: Float,
    ) -> Unit = { xStart, xEnd, yPos, _ ->
        defaultDrawHorizontalGridLine(xStart, xEnd, yPos)
    },
    scrubbingBehavior: ScrubbingBehavior = ScrubbingBehavior.NoScrubbing,
    barHorizontalMargin: Dp = DEFAULT_BAR_HORIZONTAL_MARGIN,
    maxBarWidth: Dp = MAX_BAR_WIDTH,
    drawBar: DrawScope.(
        index: Int,
        chartPositionPoint: ChartPositionPoint,
        barColor: Color,
        xAxisYPosition: Float,
        barWidth: Float,
    ) -> Unit = { index, chartPositionPoint, barColor, xAxisYPosition, barWidth ->
        val halfBarWidth = barWidth / 2f
        val topLeftX = chartPositionPoint.xPosition - halfBarWidth
        val topLeftY = chartPositionPoint.yPosition
        drawRect(
            color = barColor,
            topLeft = Offset(topLeftX, topLeftY),
            size = Size(
                width = barWidth,
                height = xAxisYPosition - topLeftY,
            ),
        )
    },
    drawBarHighlight: DrawScope.(
        selectedXIndex: Long,
        x: Float,
        barWidth: Float,
        yStart: Float,
        yEnd: Float,
        topLeftY: Float,
        xAxisYPosition: Float,
        barColor: Color,
    ) -> Unit = { selectedXIndex, x, barWidth, yStart, yEnd, topLeftY, xAxisYPosition, barColor ->
        val halfBarWidth = barWidth / 2f
        val topLeftX = x - halfBarWidth

        drawRect(
            color = barColor,
            topLeft = Offset(topLeftX, topLeftY),
            size = Size(
                width = barWidth,
                height = xAxisYPosition - topLeftY,
            ),
        )
    },
) {
    val allXValues = remember(datasets) {
        datasets.map { it.points }.flatten().map { it.x }
    }
    val allYValues = remember(datasets) {
        datasets.map { it.points }.flatten().map { it.y }
    }

    val xSmallestValue = allXValues.minOrNull()
    val xLargestValue = allXValues.maxOrNull()

    val minYValue = allYValues.minOrNull()
    val maxYValue = allYValues.maxOrNull()

    // safeguards to scale the y value range if values are outside provided range
    val yValueRangeMin = if (minYValue != null && minYValue < yValueRangeMin) {
        minYValue
    } else {
        yValueRangeMin
    }
    val yValueRangeMax = if (maxYValue != null && maxYValue > yValueRangeMax) {
        maxYValue
    } else {
        yValueRangeMax
    }

    if (xSmallestValue == null || xLargestValue == null) {
        return
    } else {
        /**
         * Add 1 to end of range. In the bar chart we draw the bar's horizontal center offset
         * between the value and the next value. So by adding 1 to end we have space for the last
         * bar.
         */
        val xValueRangeMin = xSmallestValue
        val xValueRangeMax = xLargestValue + 1

        BaseChart(
            datasets = datasets,
            contentDescription = contentDescription,
            yValueRangeMin = yValueRangeMin,
            yValueRangeMax = yValueRangeMax,
            xValueRangeMin = xValueRangeMin,
            xValueRangeMax = xValueRangeMax,
            formatXAxisLabel = formatXAxisLabel,
            formatYAxisLabel = formatYAxisLabel,
            updateHoistedSelectedValue = updateHoistedSelectedValue,
            hoistIsBeingDragged = hoistIsBeingDragged,
            canvasHeight = canvasHeight,
            horizontalChartMargin = horizontalChartMargin,
            axisToGraphHorizontalMargin = barHorizontalMargin / 2,
            numOfYAxisIntervals = numOfYAxisIntervals,
            xAxisLabelPaint = xAxisLabelPaint,
            yAxisLabelPaint = yAxisLabelPaint,
            drawXAxis = drawXAxis,
            drawYAxis = drawYAxis,
            drawHighlight = { selectedXIndex, x, yValues, data ->
                drawBarHighlight(
                    selectedXIndex = selectedXIndex,
                    x = x,
                    yValues = yValues,
                    xStart = data.xStart,
                    xEnd = data.xEnd,
                    xAxisYPosition = data.xAxisYPosition,
                    xValueRangeMin = xValueRangeMin,
                    xValueRangeMax = xValueRangeMax,
                    barHorizontalMargin = barHorizontalMargin,
                    maxBarWidth = maxBarWidth,
                    yStart = data.yBottom,
                    yEnd = data.yTop,
                    drawBarHighlight = drawBarHighlight,
                )
            },
            drawHorizontalGridLine = drawHorizontalGridLine,
            scrubbingBehavior = scrubbingBehavior,
            drawPoints = { data, boundsData ->
                drawBarChart(
                    data = data,
                    xStart = boundsData.xStart,
                    xEnd = boundsData.xEnd,
                    xAxisYPosition = boundsData.xAxisYPosition,
                    xValueRangeMin = xValueRangeMin,
                    xValueRangeMax = xValueRangeMax,
                    barHorizontalMargin = barHorizontalMargin,
                    maxBarWidth = maxBarWidth,
                    onDrawBar = drawBar,
                )
            },
            calculateXPosition = ::calculateXPosition,
            calculateYPosition = ::calculateYPosition,
        )
    }
}

private fun DrawScope.calculateBarWidth(
    xEnd: Float,
    xStart: Float,
    xValueRangeMax: Long,
    xValueRangeMin: Long,
    barHorizontalMargin: Dp,
    maxBarWidth: Dp,
): Float {
    val xWindow = xEnd - xStart
    val xRangeWindow = xValueRangeMax - xValueRangeMin

    /**
     * Bar width calculated using bar horizontal margin
     */
    val barWidth = (xWindow / xRangeWindow) - barHorizontalMargin.toPx()

    /**
     * If bar width calculated using [barHorizontalMargin] is smaller than [MIN_BAR_WIDTH],
     * fallback to calculating bar width as a percent ratio of space.
     * Compare to [maxBarWidth] and use whichever is smaller.
     * This allows us to try to match the margin between bars asked for by design, but gives us a
     * fallback if we don't have space for the margin.
     */
    return if (barWidth < MIN_BAR_WIDTH.toPx()) {
        val fallbackBarWidth = (xWindow / xRangeWindow) * TWO_THIRDS
        fallbackBarWidth
    } else {
        minOf(barWidth, maxBarWidth.toPx())
    }
}

private fun calculateXPosition(
    xValue: Long,
    xValueRangeMin: Long,
    xStep: Float,
    xStart: Float,
): Float {
    /**
     * Offset the center of the bar by [xValue] + [BAR_CENTER_OFFSET]
     * This means we are drawing the bars between the actual value and the next integer.
     * We account for this by adding 1 to the end of the range.
     */
    return (xValue + BAR_CENTER_OFFSET - xValueRangeMin) * xStep + xStart
}

private fun DrawScope.drawBarChart(
    data: List<Pair<List<ChartPositionPoint>, Color>>,
    xStart: Float,
    xEnd: Float,
    xAxisYPosition: Float,
    xValueRangeMin: Long,
    xValueRangeMax: Long,
    barHorizontalMargin: Dp,
    maxBarWidth: Dp,
    onDrawBar: DrawScope.(
        index: Int,
        chartPositionPoint: ChartPositionPoint,
        color: Color,
        xAxisYPosition: Float,
        barWidth: Float,
    ) -> Unit,
) {
    val barWidth = calculateBarWidth(
        xEnd = xEnd,
        xStart = xStart,
        xValueRangeMax = xValueRangeMax,
        xValueRangeMin = xValueRangeMin,
        barHorizontalMargin = barHorizontalMargin,
        maxBarWidth = maxBarWidth,
    )

    data.forEach {
        drawBars(
            points = it.first,
            color = it.second,
            xAxisYPosition = xAxisYPosition,
            barWidth = barWidth,
            onDrawBar = onDrawBar,
        )
    }
}

private fun DrawScope.drawBars(
    points: List<ChartPositionPoint>,
    color: Color,
    xAxisYPosition: Float,
    barWidth: Float,
    onDrawBar: DrawScope.(
        index: Int,
        chartPositionPoint: ChartPositionPoint,
        color: Color,
        xAxisYPosition: Float,
        barWidth: Float,
    ) -> Unit,
) {
    points.forEach {
        onDrawBar(
            points.indexOf(it),
            it,
            color,
            xAxisYPosition,
            barWidth,
        )
    }
}

private fun DrawScope.drawBarHighlight(
    selectedXIndex: Long,
    x: Float,
    yValues: List<Pair<Float, Color>>,
    xStart: Float,
    xEnd: Float,
    xAxisYPosition: Float,
    xValueRangeMin: Long,
    xValueRangeMax: Long,
    barHorizontalMargin: Dp,
    maxBarWidth: Dp,
    yStart: Float,
    yEnd: Float,
    drawBarHighlight: DrawScope.(
        selectedXIndex: Long,
        x: Float,
        barWidth: Float,
        yStart: Float,
        yEnd: Float,
        topLeftY: Float,
        xAxisYPosition: Float,
        color: Color,
    ) -> Unit,
) {
    val barWidth = calculateBarWidth(
        xEnd = xEnd,
        xStart = xStart,
        xValueRangeMax = xValueRangeMax,
        xValueRangeMin = xValueRangeMin,
        barHorizontalMargin = barHorizontalMargin,
        maxBarWidth = maxBarWidth,
    )

    yValues.forEach {
        drawBarHighlight(
            selectedXIndex,
            x,
            barWidth,
            yStart,
            yEnd,
            it.first,
            xAxisYPosition,
            it.second,
        )
    }
}

private fun calculateYPosition(
    yValue: Float,
    yStep: Float,
    yMax: Float,
): Float {
    return (yMax - yValue) * yStep
}

@Preview(showBackground = true)
@Composable
internal fun BarChartPreview() {
    Box(modifier = Modifier.padding(8.dp)) {
        ComposableBarChart(
            listOf(
                MockGraphData.MOCK_BAR_CHART_DATASET_A,
                MockGraphData.MOCK_BAR_CHART_DATASET_B,
            ),
            contentDescription = "Line chart",
            formatXAxisLabel = { null },
            scrubbingBehavior = ScrubbingBehavior.GraphTouchScrubbingBehavior(),
        )
    }
}

private const val TWO_THIRDS = .66f
private const val BAR_CENTER_OFFSET = .5f
private val DEFAULT_BAR_HORIZONTAL_MARGIN = 8.dp
private val MIN_BAR_WIDTH = 6.dp
private val MAX_BAR_WIDTH = 90.dp
