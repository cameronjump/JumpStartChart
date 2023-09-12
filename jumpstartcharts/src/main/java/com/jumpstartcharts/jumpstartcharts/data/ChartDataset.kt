package com.jumpstartcharts.jumpstartcharts.data

import androidx.compose.ui.graphics.Color
import com.jumpstartcharts.jumpstartcharts.composables.BaseComposableChart

/**
 * Dataset implementation for [BaseComposableChart]
 */
data class ChartDataset<T : ChartValuePoint>(
    override val color: Color,
    override val highlightColor: Color,
    override val points: List<T>
) : Dataset<T>