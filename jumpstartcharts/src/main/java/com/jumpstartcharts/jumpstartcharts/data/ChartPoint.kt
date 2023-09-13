package com.jumpstartcharts.jumpstartcharts.data

import com.jumpstartcharts.jumpstartcharts.composables.BaseChart

/**
 * Point interface required for data points to be used in [BaseChart]
 */
interface ChartPoint {
    val x: Long
    val y: Float
}
