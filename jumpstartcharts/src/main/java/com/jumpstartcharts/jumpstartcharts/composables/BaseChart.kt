package com.jumpstartcharts.jumpstartcharts.composables

import android.graphics.Rect
import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jumpstartcharts.jumpstartcharts.data.ChartPositionPoint
import com.jumpstartcharts.jumpstartcharts.data.ChartSelectedValue
import com.jumpstartcharts.jumpstartcharts.data.ChartValuePoint
import com.jumpstartcharts.jumpstartcharts.data.Dataset
import com.jumpstartcharts.jumpstartcharts.data.ScrubbingBehavior
import com.jumpstartcharts.jumpstartcharts.util.FindClosest
import com.jumpstartcharts.jumpstartcharts.util.drawCenteredText
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Composable chart designed with goal of being dynamic enough to incorporate many different
 * Fundrise charts, but opinionated enough to help with common Fundrise patterns.
 * The idea is that hopefully any difference in behavior can be passed in by lambda and this
 * function will handle connecting thumb sliders, dynamic highlight, bars, state retention, and
 * drawing labels.
 *
 * This function has no default values. Default values will provided one layer up for the chart
 * type. ie [LineChart].
 *
 * [datasets] list of  datasets
 * [contentDescription] describes the content of the chart for accessibility purposes
 * [yValueRangeMin] lower bound of y value range
 * [yValueRangeMax] upper bound of y value range
 * [xValueRangeMin] lower bound of x value range
 * [yValueRangeMax] upper bound of x value range
 * [formatXAxisLabel] function that takes an x value and returns a [String] to be drawn, if null no
 *  label is drawn
 * [formatYAxisLabel] function that takes an y value and returns a [String] to be drawn, if null no
 *  label is drawn
 * [updateHoistedSelectedValue] hoists the currently selected x value and whether it is highlighted
 * [canvasHeight] height of the canvas in Dp
 * [horizontalChartMargin] start and end horizontal offsets in Dp
 * [axisToGraphHorizontalMargin] margin from y axis to first point drawn on canvas, also on end
 * [numOfYAxisIntervals] interval on which we will try to draw y axis labels and gridlines
 * [drawXAxis] lambda that takes points and draws an x axis
 * [drawYAxis] lambda that takes points and draws a y axis
 * [drawHighlight] lambda that takes points and yValues and draws the highlight
 * [drawHorizontalGridLine] lambda that takes points and draws a horizontal grid line
 * [scrubbingBehavior] defines how a graph is interacted with
 * [drawPoints] lambda that draws the points on the canvas
 * [calculateXPosition] lambda that calculates x position given x parameters
 * [calculateYPosition] lambda that calculates y position given y parameters
 */
@Composable
@Suppress("LongMethod")
fun <ChartPoint : ChartValuePoint> BaseChart(
    datasets: List<Dataset<ChartPoint>>,
    contentDescription: String,
    yValueRangeMin: Float,
    yValueRangeMax: Float,
    xValueRangeMin: Long,
    xValueRangeMax: Long,
    formatXAxisLabel: (value: Long) -> String?,
    formatYAxisLabel: (value: Float) -> String?,
    updateHoistedSelectedValue: (ChartSelectedValue?) -> Unit,
    hoistIsBeingDragged: (Boolean) -> Unit,
    canvasHeight: Dp,
    horizontalChartMargin: Dp,
    axisToGraphHorizontalMargin: Dp,
    numOfYAxisIntervals: Int,
    xAxisLabelPaint: NativePaint,
    yAxisLabelPaint: NativePaint,
    drawXAxis: DrawScope.(x1: Float, x2: Float, y: Float) -> Unit,
    drawYAxis: DrawScope.(y1: Float, y2: Float, x: Float) -> Unit,
    drawHighlight: DrawScope.(
        selectedXIndex: Long,
        x: Float,
        yValues: List<Pair<Float, Color>>,
        data: CanvasBoundsData,
    ) -> Unit,
    drawHorizontalGridLine: DrawScope.(xStart: Float, xEnd: Float, yPos: Float, yValue: Float) -> Unit,
    scrubbingBehavior: ScrubbingBehavior,
    drawPoints: DrawScope.(
        data: List<Pair<List<ChartPositionPoint>, Color>>,
        boundsData: CanvasBoundsData,
    ) -> Unit,
    calculateXPosition: (xValue: Long, xMin: Long, xStep: Float, xStart: Float) -> Float,
    calculateYPosition: (yValue: Float, yStep: Float, yMax: Float) -> Float,
) {
    if (datasets.isEmpty()) {
        return
    }

    /**
     * To reduce recomposition we retain [ChartSelectedValue] here.
     * We hoist it out when it updates after some debouncing.
     */
    val selectedXValue: MutableState<ChartSelectedValue?> = rememberSaveable {
        mutableStateOf(null)
    }

    var resetHighlightJob: Job? = null

    Column(
        modifier = Modifier
            .testTag("Chart")
            .clickable(
                // Click label for accessibility
                onClickLabel = contentDescription,
                // Disable ripple
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) {
            }
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        val xValuePositionMap = remember {
            mutableStateOf<Map<Long, Float>>(emptyMap())
        }
        val sortedXValues = remember {
            derivedStateOf {
                xValuePositionMap.value.map {
                    it.key
                }.sorted().toLongArray()
            }
        }
        val sortedXPositions = remember {
            derivedStateOf {
                xValuePositionMap.value.map {
                    it.value
                }.sorted().toFloatArray()
            }
        }

        val minXPosition = remember {
            mutableStateOf(0f)
        }
        val maxXPosition = remember {
            mutableStateOf(Float.MAX_VALUE)
        }
        val scope = rememberCoroutineScope()

        /**
         * Center position of scrubber [remember] retains the value on orientation change.
         */
        val scrubberCenterXPosition = remember { Animatable(0f) }

        /**
         * Once x value position map is calculated calculate min and max positions.
         * Move the scrubber to the highlighted value or to the  min position.
         */
        LaunchedEffect(xValuePositionMap.value) {
            // Once xValuePositionMap is calculated ensure scrubber is within that range
            if (xValuePositionMap.value.isNotEmpty()) {
                minXPosition.value = xValuePositionMap.value.values.minOf { it }
                maxXPosition.value = xValuePositionMap.value.values.maxOf { it }
                scrubberCenterXPosition.snapTo(
                    xValuePositionMap.value[selectedXValue.value?.xValue] ?: minXPosition.value,
                )
            }
        }
        /**
         * These points are used in multiple places so we use [remember] to only calculate once
         */
        val allPoints = remember(key1 = datasets) {
            datasets.map { it.points }.flatten()
        }
        val allXValues = remember(key1 = datasets) {
            val mappedXValues = allPoints.map { it.x }

            /**
             * Initialize highlighted position
             */
            val initialXValue = if (scrubbingBehavior.shouldStartScrubberOnLeft) {
                mappedXValues.min()
            } else {
                mappedXValues.max()
            }
            selectedXValue.value = ChartSelectedValue(
                initialXValue,
                scrubbingBehavior.alwaysHighlightValue,
            )
            resetHighlightJob?.cancel()

            mappedXValues
        }

        /**
         * The [BaseChart] handles calculating space for your labels.
         * It does this by calculating all string labels for the axis values and the axis range
         * and finding the longest string for that axis.
         */
        val longestXLabel = remember(key1 = datasets) {
            val possibleYValues = listOf(xValueRangeMin, xValueRangeMax) + allXValues
            possibleYValues.mapNotNull { formatXAxisLabel(it) }
                .maxByOrNull {
                    it.length
                }
        }
        val longestYLabel = remember(key1 = datasets) {
            val possibleYValues = listOf(yValueRangeMin, yValueRangeMax) + allPoints.map { it.y }
            possibleYValues.mapNotNull { formatYAxisLabel(it) }
                .maxByOrNull {
                    it.length
                }
        }

        /**
         * This lambda takes the x coordinate of the user interaction. It uses that to find the
         * x value of the nearest point. Marks that points as highlighted. Moves the scrubber
         * position to that x position of the user interaction.
         */
        val onPressed: (Float) -> Unit = { newXPosition ->
            scope.launch {
                hoistIsBeingDragged(true)

                // calculate and update selected value
                val nearestXValue = FindClosest.findClosestValue(
                    newXPosition,
                    sortedXPositions.value,
                    sortedXValues.value,
                )

                nearestXValue?.let {
                    selectedXValue.value = ChartSelectedValue(nearestXValue, true)

                    // Keep xPosition within range or snap to new position
                    if (newXPosition < minXPosition.value) {
                        scrubberCenterXPosition.snapTo(minXPosition.value)
                    } else if (newXPosition > maxXPosition.value) {
                        scrubberCenterXPosition.snapTo(maxXPosition.value)
                    } else {
                        scrubberCenterXPosition.snapTo(newXPosition)
                    }
                }
            }
        }

        /**
         * This lambda takes marks the selected x value as no longer being highlighted.
         * It then animates the scrubber position to the x coordinate of that selected x value.
         */
        val onRelease: () -> Unit = {
            scope.launch {
                hoistIsBeingDragged(false)

                // on release update isHighlighted and snap to selectedXValue
                selectedXValue.value = selectedXValue.value?.copy(
                    isHighlighted = scrubbingBehavior.alwaysHighlightValue,
                )

                val newXPosition = xValuePositionMap.value[selectedXValue.value?.xValue]

                newXPosition?.let {
                    scrubberCenterXPosition.animateTo(it)
                }

                if (scrubbingBehavior.resetPositionAfterDelay) {
                    resetHighlightJob?.cancel()

                    resetHighlightJob = launch(coroutineContext) {
                        delay(RESET_DELAY)
                        val initialXValue = if (scrubbingBehavior.shouldStartScrubberOnLeft) {
                            allXValues.min()
                        } else {
                            allXValues.max()
                        }
                        selectedXValue.value = ChartSelectedValue(
                            initialXValue,
                            scrubbingBehavior.alwaysHighlightValue,
                        )
                    }
                }
            }
        }

        /**
         * Data needed to have separate Highlight Canvas to reduce recomposition of entire canvas
         */
        val highlightBoundsData: MutableState<CanvasBoundsData?> = remember(key1 = datasets) {
            mutableStateOf(null)
        }
        val highlightYStep: MutableState<Float?> = remember(key1 = datasets) {
            mutableStateOf(null)
        }

        Box {
            ScopedView {
                BaseChartCanvas(
                    modifier = Modifier
                        .testTag("Canvas")
                        .fillMaxWidth()
                        .height(canvasHeight)
                        .conditional(scrubbingBehavior.supportGraphTouchScrubbing) {
                            pointerInput(Unit) {
                                forEachGesture {
                                    awaitPointerEventScope {
                                        val firstDown = awaitFirstDown()

                                        if (firstDown.pressed) {
                                            onPressed(firstDown.position.x)
                                        }

                                        do {
                                            val pointerEvent = awaitPointerEvent()

                                            pointerEvent.changes.forEach { change ->
                                                onPressed(change.position.x)
                                            }
                                        } while (pointerEvent.changes.any { it.pressed })
                                        onRelease()
                                    }
                                }
                            }
                        },
                    datasets = datasets,
                    allPoints = allPoints,
                    calculateXPosition = calculateXPosition,
                    calculateYPosition = calculateYPosition,
                    drawHorizontalGridLine = drawHorizontalGridLine,
                    drawPoints = drawPoints,
                    drawXAxis = drawXAxis,
                    drawYAxis = drawYAxis,
                    formatXAxisLabel = formatXAxisLabel,
                    formatYAxisLabel = formatYAxisLabel,
                    horizontalChartMargin = horizontalChartMargin,
                    axisToGraphHorizontalMargin = axisToGraphHorizontalMargin,
                    longestXLabel = longestXLabel,
                    longestYLabel = longestYLabel,
                    numOfYAxisIntervals = numOfYAxisIntervals,
                    updateHighlightChartBounds = { data, yStep ->
                        highlightBoundsData.value = data
                        highlightYStep.value = yStep
                    },
                    updatePositionMap = {
                        xValuePositionMap.value = it
                    },
                    xAxisLabelPaint = xAxisLabelPaint,
                    yAxisLabelPaint = yAxisLabelPaint,
                    xValueRangeMin = xValueRangeMin,
                    xValueRangeMax = xValueRangeMax,
                    yValueRangeMin = yValueRangeMin,
                    yValueRangeMax = yValueRangeMax,
                )
            }

            val highlightDataBoundsValue = highlightBoundsData.value
            val highlightDataYStepValue = highlightYStep.value

            if (highlightDataBoundsValue != null && highlightDataYStepValue != null) {
                ScopedView {
                    BaseChartHighlightCanvas(
                        modifier = Modifier
                            .testTag("HighlightCanvas")
                            .fillMaxWidth()
                            .height(canvasHeight),
                        datasets = datasets,
                        selectedXValue = selectedXValue.value,
                        xValuePositionMap = xValuePositionMap.value,
                        yValueRangeMax = yValueRangeMax,
                        calculateYPosition = calculateYPosition,
                        canvasBoundsData = highlightDataBoundsValue,
                        yStep = highlightDataYStepValue,
                        drawHighlight = drawHighlight,
                        updateHoistedSelectedValue = updateHoistedSelectedValue,
                    )
                }
            }
        }
    }
}

@Composable
private fun <ChartPoint : ChartValuePoint> BaseChartCanvas(
    datasets: List<Dataset<ChartPoint>>,
    modifier: Modifier,
    allPoints: List<ChartPoint>,
    longestYLabel: String?,
    yAxisLabelPaint: NativePaint,
    longestXLabel: String?,
    xAxisLabelPaint: NativePaint,
    horizontalChartMargin: Dp,
    axisToGraphHorizontalMargin: Dp,
    xValueRangeMin: Long,
    xValueRangeMax: Long,
    yValueRangeMin: Float,
    yValueRangeMax: Float,
    updateHighlightChartBounds: (data: CanvasBoundsData, yStep: Float) -> Unit,
    updatePositionMap: (Map<Long, Float>) -> Unit,
    drawXAxis: DrawScope.(x1: Float, x2: Float, y: Float) -> Unit,
    drawYAxis: DrawScope.(y1: Float, y2: Float, x: Float) -> Unit,
    formatXAxisLabel: (value: Long) -> String?,
    formatYAxisLabel: (value: Float) -> String?,
    numOfYAxisIntervals: Int,
    drawHorizontalGridLine: DrawScope.(xStart: Float, xEnd: Float, yPos: Float, yValue: Float) -> Unit,
    drawPoints: DrawScope.(
        data: List<Pair<List<ChartPositionPoint>, Color>>,
        boundsData: CanvasBoundsData,
    ) -> Unit,
    calculateXPosition: (xValue: Long, xMin: Long, xStep: Float, horizontalOffset: Float) -> Float,
    calculateYPosition: (yValue: Float, yStep: Float, yMax: Float) -> Float,
) {
    Canvas(modifier = modifier) {
        /**
         * The [BaseChart] handles calculating space for your labels.
         * At this step it takes the longest labels we calculated earlier and measures them
         * with the provided paint. If no labels are found default to 0.
         */
        val rect = Rect()

        val yAxisLabelToChartPadding = 8.dp.toPx()
        val xAxisLabelToChartPadding = 16.dp.toPx()

        val yAxisLabelSpace = longestYLabel?.let {
            // Calculate yAxisLabelSpace by using naive longest length
            yAxisLabelPaint.getTextBounds(longestYLabel, 0, longestYLabel.length, rect)
            rect.width().toFloat() + yAxisLabelToChartPadding
        } ?: 0f

        val xAxisLabelSpace = longestXLabel?.let {
            // Calculate xAxisLabelSpace by using naive longest length
            xAxisLabelPaint.getTextBounds(longestXLabel, 0, longestXLabel.length, rect)
            rect.height().toFloat() + xAxisLabelToChartPadding
        } ?: 0f

        /**
         * Calculates the bounds of the chart in the canvas
         * Reminder: for canvas the 0 coordinate is the top
         */
        val xAxisStart = horizontalChartMargin.toPx() + yAxisLabelSpace
        val xStart = xAxisStart + axisToGraphHorizontalMargin.toPx()
        val xEnd = size.width - horizontalChartMargin.toPx() - axisToGraphHorizontalMargin.toPx()
        val yTop = 0f
        val yBottom = size.height - xAxisLabelSpace

        val xAxisSpace = xEnd - xStart
        val yAxisSpace = yBottom - yTop

        /**
         * Step is the amount of pixels per value in the graph
         * Example: a range of 20 across a canvas of 100 pixels would be a step of 5
         */
        val xStep = xAxisSpace / (xValueRangeMax - xValueRangeMin)
        val yStep = yAxisSpace / (yValueRangeMax - yValueRangeMin)

        val xAxisYPosition = if (yValueRangeMin <= 0 && xValueRangeMax >= 0) {
            val yValue = 0f
            calculateYPosition(
                yValue,
                yStep,
                yValueRangeMax,
            )
        } else {
            null
        }

        /**
         * The start and end of the allocated space for y axis labels
         */
        val xPosYAxisLabelSpaceStart = horizontalChartMargin.toPx()
        val xPosYAxisLabelSpaceEnd = xAxisStart - xAxisLabelToChartPadding

        val canvasBounds = CanvasBoundsData(
            xStart = xStart,
            xEnd = xEnd,
            yTop = yTop,
            yBottom = yBottom,
            xAxisYPosition = xAxisYPosition ?: yBottom,
        )

        /**
         * Hoist out window bounds for highlighting
         */
        updateHighlightChartBounds(
            canvasBounds,
            yStep,
        )

        /**
         * Only [drawXAxis] if the graph includes 0
         */
        xAxisYPosition?.let {
            drawXAxis(xAxisStart, xEnd, it)
        }

        /**
         * Always call lambdas to [drawYAxis] and [drawYAxisLabelAndGridlines]
         */
        drawYAxis(yTop, yBottom, xAxisStart)
        drawYAxisLabelAndGridlines(
            numOfYAxisIntervals,
            yAxisLabelPaint,
            yValueRangeMin,
            yValueRangeMax,
            xPosYAxisLabelSpaceStart,
            xPosYAxisLabelSpaceEnd,
            yBottom,
            yTop,
            formatYAxisLabel,
        ) { yPos, yValue ->
            drawHorizontalGridLine(xAxisStart, xEnd, yPos, yValue)
        }

        /**
         * Store and associate x values with x coordinates
         */
        val xValuePositionMap = allPoints.associate {
            it.x to calculateXPosition(
                it.x,
                xValueRangeMin,
                xStep,
                xStart,
            )
        }
        updatePositionMap(xValuePositionMap)

        /**
         * Always call [drawXAxisLabels]
         */
        drawXAxisLabels(
            xAxisLabelPaint,
            yPosXAxisLabelSpaceTop = yBottom + xAxisLabelToChartPadding,
            yPosYAxisLabelSpaceBottom = size.height,
            xValuePositionMap = xValuePositionMap,
            formatXAxisLabel,
        )

        /**
         * Calculate all points to graph, then [drawPoints].
         * The points are then used for [drawHighlight].
         */
        val pointData = datasets.map { dataSet ->
            val graphPoints = dataSet.points.map {
                ChartPositionPoint(
                    calculateXPosition(
                        it.x,
                        xValueRangeMin,
                        xStep,
                        xStart,
                    ),
                    calculateYPosition(
                        it.y,
                        yStep,
                        yValueRangeMax,
                    ),
                )
            }
            Pair(graphPoints, dataSet.color)
        }
        drawPoints(pointData, canvasBounds)
    }
}

@Composable
private fun <ChartPoint : ChartValuePoint> BaseChartHighlightCanvas(
    datasets: List<Dataset<ChartPoint>>,
    modifier: Modifier,
    selectedXValue: ChartSelectedValue?,
    xValuePositionMap: Map<Long, Float>,
    yValueRangeMax: Float,
    calculateYPosition: (yValue: Float, yStep: Float, yMax: Float) -> Float,
    canvasBoundsData: CanvasBoundsData,
    yStep: Float,
    drawHighlight: DrawScope.(
        xValueIndex: Long,
        x: Float,
        yValues: List<Pair<Float, Color>>,
        data: CanvasBoundsData,
    ) -> Unit,
    updateHoistedSelectedValue: (ChartSelectedValue?) -> Unit,
) {
    updateHoistedSelectedValue(selectedXValue)

    // On scrubber move create haptic feedback
    val view = LocalView.current
    val shouldPerformFeedback = remember(datasets) {
        mutableStateOf(false)
    }
    LaunchedEffect(selectedXValue) {
        if (shouldPerformFeedback.value) {
            view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
        } else {
            shouldPerformFeedback.value = true
        }
    }

    Canvas(modifier = modifier.testTag("Highlight Canvas")) {
        val highlightedXPosition = xValuePositionMap[selectedXValue?.xValue]
        val highlightedYPositions = datasets.mapNotNull { dataSet ->
            val point = dataSet.points.find { it.x == selectedXValue?.xValue }
            if (point != null) {
                Pair(point, dataSet.highlightColor)
            } else {
                null
            }
        }.map {
            Pair(
                calculateYPosition(
                    it.first.y,
                    yStep,
                    yValueRangeMax,
                ),
                it.second,
            )
        }

        if (highlightedXPosition != null && selectedXValue?.isHighlighted == true) {
            drawHighlight(
                selectedXValue.xValue,
                highlightedXPosition,
                highlightedYPositions,
                canvasBoundsData,
            )
        }
    }
}

private fun DrawScope.drawYAxisLabelAndGridlines(
    numOfYAxisIntervals: Int,
    paint: NativePaint,
    yValueMin: Float,
    yValueMax: Float,
    xPosYAxisLabelSpaceStart: Float,
    xPosYAxisLabelSpaceEnd: Float,
    yBottom: Float,
    yTop: Float,
    formatYAxisLabel: (value: Float) -> String?,
    drawHorizontalGridLine: DrawScope.(yPos: Float, yValue: Float) -> Unit,
) {
    val spacing = (yBottom - yTop) / numOfYAxisIntervals
    val numberSpacing = (yValueMax - yValueMin) / numOfYAxisIntervals

    /**
     * Make sure labels do not overlap
     */
    var previousEnd = 0f
    val rect = Rect()

    val indexes = 0..numOfYAxisIntervals
    indexes.forEach {
        val yPosition = yTop + spacing * it
        val numberLabel = (yValueMax - (numberSpacing * it))
        val formattedLabel = formatYAxisLabel(numberLabel)

        drawHorizontalGridLine(yPosition, numberLabel)

        formattedLabel?.let { label ->
            paint.getTextBounds(label, 0, label.length, rect)
            val height = rect.height()
            val newStart = if (it == indexes.first) {
                yPosition
            } else {
                yPosition - AXIS_LABEL_PADDING.toPx() - (height / 2)
            }
            if (newStart >= previousEnd) {
                drawCenteredText(
                    xPosYAxisLabelSpaceStart,
                    xPosYAxisLabelSpaceEnd,
                    yPosition,
                    paint,
                    label,
                )

                previousEnd = yPosition + (height / 2)
            }
        }
    }
}

/**
 * Data class used to hold bounds of canvas for use in drawing/highlighting points
 */
data class CanvasBoundsData(
    val xStart: Float,
    val xEnd: Float,
    val yBottom: Float,
    val yTop: Float,
    val xAxisYPosition: Float,
)

private fun DrawScope.drawXAxisLabels(
    paint: NativePaint,
    yPosXAxisLabelSpaceTop: Float,
    yPosYAxisLabelSpaceBottom: Float,
    xValuePositionMap: Map<Long, Float>,
    formatXAxisLabel: (value: Long) -> String?,
) {
    val yCenter = (yPosXAxisLabelSpaceTop + yPosYAxisLabelSpaceBottom) / 2f
    val rect = Rect()

    /**
     * Make sure labels do not overlap
     */
    var previousEnd = 0f

    xValuePositionMap.keys.sorted().forEach { value ->
        val label = formatXAxisLabel(value)
        val xPosition = xValuePositionMap[value]

        if (label != null && xPosition != null) {
            paint.getTextBounds(label, 0, label.length, rect)
            val width = rect.width()
            val newStart = xPosition - AXIS_LABEL_PADDING.toPx() - (width / 2)

            if (newStart > previousEnd) {
                drawCenteredText(
                    xCenter = xPosition,
                    yCenter = yCenter,
                    paint = paint,
                    text = label,
                )

                previousEnd = xPosition + (width / 2)
            }
        }
    }
}

/**
 * Scopes a composable, for use in recomposition optimizations
 * Column and Row are inline functions. Any direct children of inline function composable
 * recompose if a sibling recomposes.
 * [ScopedView] brings the composable a layer down, now its only siblings are other composable
 * functions in the [ScopedView]. The composable wrapped in [ScopedView] can recompose without
 * causing recompilation in the [ScopedView] siblings.
 *
 * Helpful reference (https://stackoverflow.com/questions/71099730/jetpack-compose-add-scopes-for-smart-recomposition)
 */
@Composable
fun ScopedView(content: @Composable () -> Unit) {
    content()
}

/**
 * Add a modifier based on a boolean
 */
fun Modifier.conditional(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}

private const val RESET_DELAY = 2000L
private val AXIS_LABEL_PADDING = 2.dp
