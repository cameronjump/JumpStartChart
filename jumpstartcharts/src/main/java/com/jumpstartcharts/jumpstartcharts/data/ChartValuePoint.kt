package com.jumpstartcharts.jumpstartcharts.data

import com.jumpstartcharts.jumpstartcharts.composables.BaseComposableChart

/**
 * Point interface required for data points to be used in [BaseComposableChart]
 */
interface ChartValuePoint {
    val x: Long
    val y: Float
}