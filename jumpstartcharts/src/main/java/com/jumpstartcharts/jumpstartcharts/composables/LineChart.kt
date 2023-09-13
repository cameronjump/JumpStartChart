package com.jumpstartcharts.jumpstartcharts.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jumpstartcharts.jumpstartcharts.data.ChartPositionPoint
import com.jumpstartcharts.jumpstartcharts.data.ChartSelectedValue
import com.jumpstartcharts.jumpstartcharts.data.Dataset
import com.jumpstartcharts.jumpstartcharts.data.ScrubbingBehavior
import com.jumpstartcharts.jumpstartcharts.mock.MockGraphData
import com.jumpstartcharts.jumpstartcharts.util.CHART_GUIDELINE_PATTERN
import com.jumpstartcharts.jumpstartcharts.util.CHART_GUIDELINE_STROKE_WIDTH
import com.jumpstartcharts.jumpstartcharts.util.ChartRangeCalculator
import com.jumpstartcharts.jumpstartcharts.util.defaultDrawHorizontalGridLine
import com.jumpstartcharts.jumpstartcharts.util.defaultDrawLineChart
import com.jumpstartcharts.jumpstartcharts.util.defaultXAxis
import com.jumpstartcharts.jumpstartcharts.util.defaultYAxis
import com.jumpstartcharts.jumpstartcharts.util.getDefaultAxisLabelPaint

/**
 * Pass in lambdas to change functionality.
 * Provides reason defaults to make [BaseChart] a Line Chart
 */
@Composable
fun <ChartPoint : com.jumpstartcharts.jumpstartcharts.data.ChartPoint> LineChart(
    datasets: List<Dataset<ChartPoint>>,
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
    drawHighlight: DrawScope.(
        selectedXIndex: Long,
        x: Float,
        yValues: List<Pair<Float, Color>>,
        boundsData: CanvasBoundsData,
    ) -> Unit = { index, x, yValues, boundsData ->
        defaultDrawLineChartHighlight(x, yValues, boundsData.yBottom, boundsData.yTop)
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
    drawPoints: DrawScope.(
        data: List<Pair<List<ChartPositionPoint>, Color>>,
        boundsData: CanvasBoundsData,
    ) -> Unit = { data, _ ->
        defaultDrawLineChart(data)
    },
) {
    val allXValues = remember(datasets) {
        datasets.map { it.points }.flatten().map { it.x }
    }
    val allYValues = remember(datasets) {
        datasets.map { it.points }.flatten().map { it.y }
    }

    val xValueRangeMin = allXValues.minOrNull()
    val xValueRangeMax = allXValues.maxOrNull()

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

    if (xValueRangeMin == null || xValueRangeMax == null) {
        return
    } else {
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
            axisToGraphHorizontalMargin = 0.dp,
            numOfYAxisIntervals = numOfYAxisIntervals,
            xAxisLabelPaint = xAxisLabelPaint,
            yAxisLabelPaint = yAxisLabelPaint,
            drawXAxis = drawXAxis,
            drawYAxis = drawYAxis,
            drawHighlight = drawHighlight,
            drawHorizontalGridLine = drawHorizontalGridLine,
            scrubbingBehavior = scrubbingBehavior,
            drawPoints = drawPoints,
            calculateXPosition = ::calculateXPosition,
            calculateYPosition = ::calculateYPosition,
        )
    }
}

private fun DrawScope.defaultDrawLineChartHighlight(
    x: Float,
    yValues: List<Pair<Float, Color>>,
    yMin: Float,
    yMax: Float,
) {
    drawLine(
        color = Color.DarkGray,
        strokeWidth = CHART_GUIDELINE_STROKE_WIDTH,
        pathEffect = CHART_GUIDELINE_PATTERN,
        start = Offset(x, yMin),
        end = Offset(x, yMax),
    )
    yValues.forEach {
        drawCircle(
            Color.White,
            radius = 7.0.dp.toPx(),
            center = Offset(x, it.first),
        )
    }
    yValues.forEach {
        drawCircle(
            it.second,
            radius = 2.5.dp.toPx(),
            center = Offset(x, it.first),
        )
    }
}

private fun calculateXPosition(
    xValue: Long,
    xValueRangeMin: Long,
    xStep: Float,
    horizontalOffset: Float,
): Float {
    return (xValue - xValueRangeMin) * xStep + horizontalOffset
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
private fun LineChartPreview() {
    Box(modifier = Modifier.padding(8.dp)) {
        LineChart(
            listOf(
                MockGraphData.MOCK_LINE_CHART_DATASET_A,
                MockGraphData.MOCK_LINE_CHART_DATASET_B,
            ),
            contentDescription = "Line chart",
            formatXAxisLabel = { null },
            scrubbingBehavior = ScrubbingBehavior.GraphTouchScrubbingBehavior(),
        )
    }
}
