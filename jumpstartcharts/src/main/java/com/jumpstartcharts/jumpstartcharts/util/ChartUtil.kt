package com.jumpstartcharts.jumpstartcharts.util

import android.content.Context
import androidx.annotation.FontRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import com.jumpstartcharts.jumpstartcharts.R

val CHART_GUIDELINE_PATTERN = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
val CHART_GUIDELINE_STROKE_WIDTH = 4.dp.value
const val DEFAULT_LABEL_SIZE = 12

fun getDefaultAxisLabelPaint(context: Context): NativePaint {
    return getFontPaint(
        context,
        R.font.roboto_regular,
        Color.Black,
        DEFAULT_LABEL_SIZE,
    )
}

fun getFontPaint(
    context: Context,
    @FontRes fontId: Int,
    fontColor: Color,
    textSizeSp: Int,
): NativePaint {
    return getPaintWithFont(context, fontId).apply {
        textSize = with(Density(context)) {
            textSizeSp.sp.toPx()
        }
        color = fontColor.toArgb()
    }
}

fun getPaintWithFont(context: Context, @FontRes fontId: Int): NativePaint {
    val font = ResourcesCompat.getFont(context, fontId)
    return Paint().asFrameworkPaint().apply {
        typeface = font
    }
}
