package com.jumpstartcharts.jumpstartcharts.util

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Draws text centered between two x positions and at a y position
 * Returns width of text
 */
fun DrawScope.drawCenteredText(
    xStart: Float,
    xEnd: Float,
    yCenter: Float,
    paint: NativePaint,
    text: String,
) {
    drawCenteredText(
        xCenter = (xEnd + xStart) / 2f,
        yCenter = yCenter,
        paint = paint,
        text = text,
    )
}

/**
 * Draws text centered on an x positions and at a y position
 * Returns width of text
 */
fun DrawScope.drawCenteredText(
    xCenter: Float,
    yCenter: Float,
    paint: NativePaint,
    text: String,
) {
    val rect = android.graphics.Rect()
    paint.textAlign = android.graphics.Paint.Align.LEFT
    paint.getTextBounds(text, 0, text.length, rect)

    val textHeight = paint.descent() - paint.ascent()
    val textOffset = textHeight / 2f - paint.descent()

    val x: Float = xCenter - (rect.width() / 2f)
    val y: Float = yCenter + textOffset

    drawIntoCanvas { canvas ->
        canvas.nativeCanvas.drawText(
            text,
            x,
            y,
            paint,
        )
    }
}

@Preview
@Composable
fun DrawCenteredTextPreview() {
    MaterialTheme {
        val context = LocalContext.current

        Canvas(
            Modifier
                .fillMaxWidth()
                .height(300.dp),
        ) {
            drawCenteredText(
                xStart = 0f,
                xEnd = size.width,
                yCenter = size.height / 2f,
                getDefaultAxisLabelPaint(context),
                "Pizza pizza pizza",
            )
        }
    }
}

@Preview
@Composable
fun DrawCenteredTextXCenterPreview() {
    MaterialTheme {
        val context = LocalContext.current

        Canvas(
            Modifier
                .fillMaxWidth()
                .height(300.dp),
        ) {
            drawCenteredText(
                xCenter = size.width / 2f,
                yCenter = size.height / 2f,
                getDefaultAxisLabelPaint(context),
                "Pizza pizza pizza",
            )
        }
    }
}

@Preview
@Composable
fun DrawCenteredOffsetTextPreview() {
    MaterialTheme {
        val context = LocalContext.current

        Canvas(
            Modifier
                .fillMaxWidth()
                .height(300.dp),
        ) {
            val offset = 300f

            drawRect(
                Color.Red,
                topLeft = Offset(0f, 0f),
                size = Size(offset, size.height),
            )
            drawCenteredText(
                xStart = offset,
                xEnd = size.width,
                yCenter = size.height / 2f,
                getDefaultAxisLabelPaint(context),
                "Pizza pizza pizza",
            )
        }
    }
}

/**
 * Given a [DrawScope] draws an X axis from [x1] to [x2] at [y]
 */
fun DrawScope.defaultXAxis(x1: Float, x2: Float, y: Float) {
    drawLine(
        color = Color.DarkGray,
        strokeWidth = CHART_GUIDELINE_STROKE_WIDTH,
        start = Offset(x1, y),
        end = Offset(x2, y),
    )
}

/**
 * Given a [DrawScope] draws a Y axis from [y1] to [y2] at [x]
 */
fun DrawScope.defaultYAxis(y1: Float, y2: Float, x: Float) {
    drawLine(
        color = Color.DarkGray,
        strokeWidth = CHART_GUIDELINE_STROKE_WIDTH,
        start = Offset(x, y1),
        end = Offset(x, y2),
    )
}

/**
 * Given a [DrawScope] draws a horizontal grid line [xStart] to [xEnd] at [yPos]
 */
fun DrawScope.defaultDrawHorizontalGridLine(
    xStart: Float,
    xEnd: Float,
    yPos: Float,
) {
    drawLine(
        color = Color.DarkGray,
        strokeWidth = 1.dp.toPx(),
        pathEffect = CHART_GUIDELINE_PATTERN,
        start = Offset(xStart, yPos),
        end = Offset(xEnd, yPos),
    )
}
