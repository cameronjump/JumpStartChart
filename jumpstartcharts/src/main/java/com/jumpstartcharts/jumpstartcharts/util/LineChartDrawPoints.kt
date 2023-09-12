package com.jumpstartcharts.jumpstartcharts.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import com.jumpstartcharts.jumpstartcharts.data.ChartPositionPoint

/**
 * Draws a line chart for given datasets.
 */
fun DrawScope.defaultDrawLineChart(
    data: List<Pair<List<ChartPositionPoint>, Color>>,
    strokeWidth: Float = CHART_GUIDELINE_STROKE_WIDTH,
) {
    data.forEach {
        drawLine(it.first, it.second, strokeWidth)
    }
}

/**
 * Draws a cubic line chart for given datasets.
 */
fun DrawScope.drawCubicLineChart(
    data: List<Pair<List<ChartPositionPoint>, Color>>,
    strokeWidth: Float = CHART_GUIDELINE_STROKE_WIDTH,
) {
    data.forEach {
        drawCubicLine(
            it.first,
            it.second,
            strokeWidth,
        )
    }
}

/**
 * Draws a gradient line chart for given datasets.
 * First draws the gradients then draws the lines.
 * Currently assumes the first point is at [xStart] (potentially an issue?)
 */
fun DrawScope.drawLineGradientChart(
    data: List<Pair<List<ChartPositionPoint>, Color>>,
    xStart: Float,
    xEnd: Float,
    yBot: Float,
    strokeWidth: Float = CHART_GUIDELINE_STROKE_WIDTH,
    gradientAlpha: Float = 1f,
    gradientStartColors: List<Color> = emptyList(),
    endGradientColor: Color = Color.White,
) {
    data.forEachIndexed { index, item ->
        val gradientStartColor =
            gradientStartColors.getOrNull(index) ?: lerp(
                item.second,
                endGradientColor,
                GRADIENT_START_FRACTION,
            )

        drawLineGradient(
            item.first,
            xStart,
            xEnd,
            yBot,
            gradientAlpha,
            gradientStartColor,
            endGradientColor,
        )
    }
    data.forEach {
        drawLine(
            it.first,
            it.second,
            strokeWidth,
        )
    }
}

/**
 * Draws a cubic gradient line chart for given datasets.
 * First draws the cubic gradients then draws the cubic lines.
 * Currently assumes the first point is at [xStart] (potentially an issue?)
 */
fun DrawScope.drawCubicGradientChart(
    data: List<Pair<List<ChartPositionPoint>, Color>>,
    xStart: Float,
    xEnd: Float,
    yBot: Float,
    strokeWidth: Float = CHART_GUIDELINE_STROKE_WIDTH,
    gradientAlpha: Float = 1f,
    gradientStartColors: List<Color> = emptyList(),
    endGradientColor: Color = Color.White,
) {
    data.forEachIndexed { index, item ->
        val gradientStartColor =
            gradientStartColors.getOrNull(index) ?: lerp(
                item.second,
                endGradientColor,
                GRADIENT_START_FRACTION,
            )

        drawCubicGradient(
            item.first,
            xStart,
            xEnd,
            yBot,
            gradientAlpha,
            gradientStartColor,
            endGradientColor,
        )
    }
    data.forEach {
        drawCubicLine(
            it.first,
            it.second,
            strokeWidth,
        )
    }
}

/**
 * Draws a line for given [points]
 */
private fun DrawScope.drawLine(
    points: List<ChartPositionPoint>,
    color: Color,
    strokeWidth: Float,
) {
    points.forEachIndexed { index, graphPoint ->
        val nextPoint = points.getOrNull(index + 1)

        nextPoint?.let {
            drawLine(
                color = color,
                start = Offset(graphPoint.xPosition, graphPoint.yPosition),
                end = Offset(nextPoint.xPosition, nextPoint.yPosition),
                strokeWidth = strokeWidth,
            )
        }
    }
}

/**
 * Draws a cubic line for given [points]
 */
private fun DrawScope.drawCubicLine(
    points: List<ChartPositionPoint>,
    color: Color,
    strokeWidth: Float,
) {
    val firstPoint = points.firstOrNull() ?: return

    val linePath = Path()

    linePath.moveTo(firstPoint.xPosition, firstPoint.yPosition)

    points.forEachIndexed { index, graphPoint ->
        val nextPoint = points.getOrNull(index + 1)

        nextPoint?.let {
            linePath.cubicFromTo(
                graphPoint,
                nextPoint,
            )
        }
    }
    drawPath(linePath, color, style = Stroke(width = strokeWidth))
}

/**
 * Draws a vertical line gradient from [points] to [yBot] of the graph.
 * Currently assumes the first point is at [xStart] (potentially an issue?)
 */
private fun DrawScope.drawLineGradient(
    points: List<ChartPositionPoint>,
    xStart: Float,
    xEnd: Float,
    yBot: Float,
    gradientAlpha: Float,
    gradientStartColor: Color,
    endGradientColor: Color,
) {
    val firstPoint = points.firstOrNull() ?: return
    val topY = points.minByOrNull { it.yPosition }?.yPosition ?: return

    val gradientPath = Path()

    // initial positions
    gradientPath.moveTo(xStart, yBot)

    // move gradient up left corner to first point
    gradientPath.lineTo(firstPoint.xPosition, firstPoint.yPosition)

    points.forEachIndexed { index, _ ->
        val nextPoint = points.getOrNull(index + 1)

        nextPoint?.let {
            gradientPath.lineTo(nextPoint.xPosition, nextPoint.yPosition)
        }
    }
    gradientPath.lineTo(xEnd, yBot)
    gradientPath.lineTo(xStart, yBot)

    val gradient = Brush.verticalGradient(
        colors = listOf(gradientStartColor, endGradientColor),
        startY = topY,
        endY = yBot,
    )
    drawPath(gradientPath, gradient, alpha = gradientAlpha)
}

/**
 * Draws a vertical cubic gradient from [points] to [yBot] of the graph.
 * Currently assumes the first point is at [xStart] (potentially an issue?)
 */
private fun DrawScope.drawCubicGradient(
    points: List<ChartPositionPoint>,
    xStart: Float,
    xEnd: Float,
    yBot: Float,
    gradientAlpha: Float,
    gradientStartColor: Color,
    endGradientColor: Color,
) {
    val firstPoint = points.firstOrNull() ?: return
    val topY = points.minByOrNull { it.yPosition }?.yPosition ?: return

    val gradientPath = Path()

    // initial positions
    gradientPath.moveTo(xStart, yBot)

    // move gradient up left corner to first point
    gradientPath.lineTo(firstPoint.xPosition, firstPoint.yPosition)

    points.forEachIndexed { index, graphPoint ->
        val nextPoint = points.getOrNull(index + 1)

        nextPoint?.let {
            gradientPath.cubicFromTo(
                graphPoint,
                nextPoint,
            )
        }
    }
    gradientPath.lineTo(xEnd, yBot)
    gradientPath.lineTo(xStart, yBot)

    val gradient = Brush.verticalGradient(
        colors = listOf(gradientStartColor, endGradientColor),
        startY = topY,
        endY = yBot,
    )
    drawPath(gradientPath, gradient, alpha = gradientAlpha)
}

/**
 * Adds a cubic line to path from [point1] to [point2]
 */
private fun Path.cubicFromTo(point1: ChartPositionPoint, point2: ChartPositionPoint) {
    /**
     * I don't really understand what this function with these params does...
     * But I found it in this article ...
     * https://medium.com/@pranjalg2308/understanding-bezier-curve-in-android-and-moddinggraphview-library-a9b1f0f95cd0
     * ... and it works very nicely.
     */
    val conX1 = (point1.xPosition + point2.xPosition) / 2f
    val conY1 = point1.yPosition
    val conX2 = (point1.xPosition + point2.xPosition) / 2f
    val conY2 = point2.yPosition

    cubicTo(
        conX1,
        conY1,
        conX2,
        conY2,
        point2.xPosition,
        point2.yPosition,
    )
}

private const val GRADIENT_START_FRACTION = .8f
