package com.jumpstartcharts.sample.ui.canvaschart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun CanvasChartStepOne() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(Color.LightGray),
    ) {
        // ...
    }
}

@Preview(showBackground = true)
@Composable
fun CanvasChartStepTwo() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(Color.LightGray),
    ) {
        val lineWidth = 4.dp.value

        // draw x-axis
        drawLine(
            color = Color.Black,
            start = Offset(x = 0f, y = size.height / 2f),
            end = Offset(x = size.width, y = size.height / 2f),
            strokeWidth = lineWidth,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CanvasChartStepThree() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(Color.LightGray),
    ) {
        val lineWidth = 4.dp.value

        // draw x-axis
        drawLine(
            color = Color.Black,
            start = Offset(x = 0f, y = size.height / 2f),
            end = Offset(x = size.width, y = size.height / 2f),
            strokeWidth = lineWidth,
        )

        // draw y-axis
        drawLine(
            color = Color.Black,
            start = Offset(x = size.width / 2f, y = 0f),
            end = Offset(x = size.width / 2f, y = size.height),
            strokeWidth = lineWidth,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CanvasChartStepFour() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(Color.LightGray),
    ) {
        val lineWidth = 4.dp.value

        // draw x-axis
        drawLine(
            color = Color.Black,
            start = Offset(x = 0f, y = size.height / 2f),
            end = Offset(x = size.width, y = size.height / 2f),
            strokeWidth = lineWidth,
        )

        // draw y-axis
        drawLine(
            color = Color.Black,
            start = Offset(x = size.width / 2f, y = 0f),
            end = Offset(x = size.width / 2f, y = size.height),
            strokeWidth = lineWidth,
        )

        val points = listOf(
            Pair(-10f, 3f),
            Pair(-8f, 5f),
            Pair(-4f, -5f),
            Pair(1f, 2f),
            Pair(2f, 7f),
            Pair(3f, 5f),
            Pair(6f, -3f),
            Pair(8f, 9f),
            Pair(10f, 0f),
        )

        val xRangeMin = -10f
        val xRangeMax = 10f
        val xAxisWidth = size.width
        val xStep = xAxisWidth / (xRangeMax - xRangeMin)

        val yRangeMin = -10f
        val yRangeMax = 10f
        val yAxisHeight = size.height
        val yStep = yAxisHeight / (yRangeMax - yRangeMin)

        val calculateXCoordinate: (x: Float) -> Float = { x ->
            (x - xRangeMin) * xStep
        }

        val calculateYCoordinate: (y: Float) -> Float = { y ->
            (yRangeMax - y) * yStep
        }

        points.forEachIndexed { index, point ->
            val nextPoint = points.getOrNull(index + 1)

            nextPoint?.let {
                drawLine(
                    color = Color.Blue,
                    start = Offset(
                        x = calculateXCoordinate(point.first),
                        y = calculateYCoordinate(point.second),
                    ),
                    end = Offset(
                        x = calculateXCoordinate(nextPoint.first),
                        y = calculateYCoordinate(nextPoint.second),
                    ),
                    strokeWidth = 4.dp.toPx(),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CanvasChartStepFive() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(Color.LightGray),
    ) {
        val lineWidth = 4.dp.value

        // draw x-axis
        drawLine(
            color = Color.Black,
            start = Offset(x = 0f, y = size.height / 2f),
            end = Offset(x = size.width, y = size.height / 2f),
            strokeWidth = lineWidth,
        )

        // draw y-axis
        drawLine(
            color = Color.Black,
            start = Offset(x = size.width / 2f, y = 0f),
            end = Offset(x = size.width / 2f, y = size.height),
            strokeWidth = lineWidth,
        )

        val xRangeMin = -10f
        val xRangeMax = 10f
        val xAxisWidth = size.width
        val xStep = xAxisWidth / (xRangeMax - xRangeMin)

        val yRangeMin = -10f
        val yRangeMax = 10f
        val yAxisHeight = size.height
        val yStep = yAxisHeight / (yRangeMax - yRangeMin)

        val calculateXCoordinate: (x: Float) -> Float = { x ->
            (x - xRangeMin) * xStep
        }

        val calculateYCoordinate: (y: Float) -> Float = { y ->
            (yRangeMax - y) * yStep
        }

        val drawPoints: (
            points: List<Pair<Float, Float>>,
            color: Color,
        ) -> Unit = { points, color ->

            points.forEachIndexed { index, point ->
                val nextPoint = points.getOrNull(index + 1)

                nextPoint?.let {
                    drawLine(
                        color = color,
                        start = Offset(
                            x = calculateXCoordinate(point.first),
                            y = calculateYCoordinate(point.second),
                        ),
                        end = Offset(
                            x = calculateXCoordinate(nextPoint.first),
                            y = calculateYCoordinate(nextPoint.second),
                        ),
                        strokeWidth = lineWidth,
                    )
                }
            }
        }

        val points1 = listOf(
            Pair(-10f, 3f),
            Pair(-8f, 5f),
            Pair(-4f, -5f),
            Pair(1f, 2f),
            Pair(2f, 7f),
            Pair(3f, 5f),
            Pair(6f, -3f),
            Pair(8f, 9f),
            Pair(10f, 0f),
        )

        val points2 = listOf(
            Pair(-10f, 1f),
            Pair(-8f, 8f),
            Pair(-4f, -5f),
            Pair(1f, 3f),
            Pair(2f, 5f),
            Pair(3f, -2f),
            Pair(6f, 8f),
            Pair(8f, 1f),
            Pair(10f, -2f),
        )

        drawPoints(points1, Color.Blue)
        drawPoints(points2, Color.Cyan)
    }
}

@Preview(showBackground = true)
@Composable
fun GradientSample() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(Color.LightGray),
    ) {
        val lineWidth = 4.dp.value

        // draw x-axis
        drawLine(
            color = Color.Black,
            start = Offset(x = 0f, y = size.height / 2f),
            end = Offset(x = size.width, y = size.height / 2f),
            strokeWidth = lineWidth,
        )

        // draw y-axis
        drawLine(
            color = Color.Black,
            start = Offset(x = size.width / 2f, y = 0f),
            end = Offset(x = size.width / 2f, y = size.height),
            strokeWidth = lineWidth,
        )

        val xRangeMin = -10f
        val xRangeMax = 10f
        val xAxisWidth = size.width
        val xStep = xAxisWidth / (xRangeMax - xRangeMin)

        val yRangeMin = -10f
        val yRangeMax = 10f
        val yAxisHeight = size.height
        val yStep = yAxisHeight / (yRangeMax - yRangeMin)

        val calculateXCoordinate: (x: Float) -> Float = { x ->
            (x - xRangeMin) * xStep
        }

        val calculateYCoordinate: (y: Float) -> Float = { y ->
            (yRangeMax - y) * yStep
        }

        val xStart = 0f
        val xEnd = size.width
        val yBot = size.height

        val drawLineGradient: (
            points: List<Pair<Float, Float>>,
            color: Color,
        ) -> Unit = { points, color ->
            val firstPoint = points.first()
            val topY = calculateYCoordinate(points.minBy { calculateYCoordinate(it.second) }.second)

            val gradientPath = Path()

            // initial positions
            gradientPath.moveTo(0f, size.height)

            // move gradient up left corner to first point
            gradientPath.lineTo(
                calculateXCoordinate(firstPoint.first),
                calculateYCoordinate(firstPoint.second),
            )

            points.forEachIndexed { index, _ ->
                val nextPoint = points.getOrNull(index + 1)

                nextPoint?.let {
                    gradientPath.lineTo(
                        calculateXCoordinate(nextPoint.first),
                        calculateYCoordinate(nextPoint.second),
                    )
                }
            }
            gradientPath.lineTo(xEnd, yBot)
            gradientPath.lineTo(xStart, yBot)

            val gradient = Brush.verticalGradient(
                colors = listOf(color, Color.White),
                startY = topY,
                endY = yBot,
            )
            drawPath(gradientPath, gradient, alpha = .5f)

            points.forEachIndexed { index, point ->
                val nextPoint = points.getOrNull(index + 1)

                nextPoint?.let {
                    drawLine(
                        color = color,
                        start = Offset(
                            x = calculateXCoordinate(point.first),
                            y = calculateYCoordinate(point.second),
                        ),
                        end = Offset(
                            x = calculateXCoordinate(nextPoint.first),
                            y = calculateYCoordinate(nextPoint.second),
                        ),
                        strokeWidth = lineWidth,
                    )
                }
            }
        }

        val points1 = listOf(
            Pair(-10f, 3f),
            Pair(-8f, 5f),
            Pair(-4f, -5f),
            Pair(1f, 2f),
            Pair(2f, 7f),
            Pair(3f, 5f),
            Pair(6f, -3f),
            Pair(8f, 9f),
            Pair(10f, 0f),
        )

        val points2 = listOf(
            Pair(-10f, 1f),
            Pair(-8f, 8f),
            Pair(-4f, -5f),
            Pair(1f, 3f),
            Pair(2f, 5f),
            Pair(3f, -2f),
            Pair(6f, 8f),
            Pair(8f, 1f),
            Pair(10f, -2f),
        )

        drawLineGradient(points1, Color.Blue)
        drawLineGradient(points2, Color.Cyan)
    }
}

@Preview(showBackground = true)
@Composable
fun QuadraticSample() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(Color.LightGray),
    ) {
        val lineWidth = 4.dp.value

        // draw x-axis
        drawLine(
            color = Color.Black,
            start = Offset(x = 0f, y = size.height / 2f),
            end = Offset(x = size.width, y = size.height / 2f),
            strokeWidth = lineWidth,
        )

        // draw y-axis
        drawLine(
            color = Color.Black,
            start = Offset(x = size.width / 2f, y = 0f),
            end = Offset(x = size.width / 2f, y = size.height),
            strokeWidth = lineWidth,
        )

        val xRangeMin = -10f
        val xRangeMax = 10f
        val xAxisWidth = size.width
        val xStep = xAxisWidth / (xRangeMax - xRangeMin)

        val yRangeMin = -10f
        val yRangeMax = 10f
        val yAxisHeight = size.height
        val yStep = yAxisHeight / (yRangeMax - yRangeMin)

        val calculateXCoordinate: (x: Float) -> Float = { x ->
            (x - xRangeMin) * xStep
        }

        val calculateYCoordinate: (y: Float) -> Float = { y ->
            (yRangeMax - y) * yStep
        }

        val drawPoints: (
            points: List<Pair<Float, Float>>,
            color: Color,
        ) -> Unit = { points, color ->
            val firstPoint = points.first()
            val linePath = Path()

            linePath.moveTo(
                calculateXCoordinate(firstPoint.first),
                calculateYCoordinate(firstPoint.second),
            )

            points.forEachIndexed { index, point ->
                val nextPoint = points.getOrNull(index + 1)

                nextPoint?.let {
                    linePath.cubicFromTo(
                        point1x = calculateXCoordinate(point.first),
                        point1y = calculateYCoordinate(point.second),
                        point2x = calculateXCoordinate(nextPoint.first),
                        point2y = calculateYCoordinate(nextPoint.second),
                    )
                }
            }
            drawPath(
                linePath,
                color,
                style = Stroke(width = lineWidth),
            )
        }

        val points1 = listOf(
            Pair(-10f, 3f),
            Pair(-8f, 5f),
            Pair(-4f, -5f),
            Pair(1f, 2f),
            Pair(2f, 7f),
            Pair(3f, 5f),
            Pair(6f, -3f),
            Pair(8f, 9f),
            Pair(10f, 0f),
        )

        val points2 = listOf(
            Pair(-10f, 1f),
            Pair(-8f, 8f),
            Pair(-4f, -5f),
            Pair(1f, 3f),
            Pair(2f, 5f),
            Pair(3f, -2f),
            Pair(6f, 8f),
            Pair(8f, 1f),
            Pair(10f, -2f),
        )

        drawPoints(points1, Color.Blue)
        drawPoints(points2, Color.Cyan)
    }
}

/**
 * Adds a cubic line to path from [point1] to [point2]
 */
fun Path.cubicFromTo(point1x: Float, point1y: Float, point2x: Float, point2y: Float) {
    /**
     * https://medium.com/@pranjalg2308/understanding-bezier-curve-in-android-and-moddinggraphview-library-a9b1f0f95cd0
     */
    val conX1 = (point1x + point2x) / 2f
    val conY1 = point1y
    val conX2 = (point1x + point2x) / 2f
    val conY2 = point2y

    cubicTo(
        conX1,
        conY1,
        conX2,
        conY2,
        point2x,
        point2y,
    )
}

private data class SampleItem(
    val title: String,
    val show: @Composable () -> Unit,
)

private val sampleItems = listOf(
    SampleItem("Step one") {
        CanvasChartStepOne()
    },
    SampleItem("Step two") {
        CanvasChartStepTwo()
    },
    SampleItem("Step three") {
        CanvasChartStepThree()
    },
    SampleItem("Step four") {
        CanvasChartStepFour()
    },
    SampleItem("Step five") {
        CanvasChartStepFive()
    },
    SampleItem("Gradient example") {
        GradientSample()
    },

    SampleItem("Quadratic example") {
        QuadraticSample()
    },
)

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun CanvasChartSampleScreen() {
    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = "These samples are all built directly on Canvas.",
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.padding(24.dp))

        val pageCount = sampleItems.size
        val state = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f,
        ) {
            pageCount
        }

        val scope = rememberCoroutineScope()

        HorizontalPager(
            state = state,
        ) {
            Column {
                Text(
                    text = sampleItems[it].title,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Spacer(modifier = Modifier.padding(16.dp))

                sampleItems[it].show()
            }
        }

        Spacer(modifier = Modifier.padding(24.dp))

        Row {
            if (state.currentPage > 0) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { scope.launch { state.animateScrollToPage(state.currentPage - 1) } },
                ) {
                    Text(text = "Previous page")
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            Modifier.padding(16.dp)

            if (state.currentPage < pageCount - 1) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { scope.launch { state.animateScrollToPage(state.currentPage + 1) } },
                ) {
                    Text(text = "Next page")
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
