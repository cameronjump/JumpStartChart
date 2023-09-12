package com.jumpstartcharts.jumpstartcharts.data

import androidx.compose.ui.graphics.Color
import com.jumpstartcharts.jumpstartcharts.composables.BaseComposableChart

/**
 * DataSet interface required for lists of points to be used in [BaseComposableChart]
 */
interface Dataset<T : ChartValuePoint> {

    val color: Color

    val highlightColor: Color

    val points: List<T>
}
