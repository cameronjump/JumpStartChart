package com.jumpstartcharts.jumpstartcharts.data

import com.jumpstartcharts.jumpstartcharts.composables.BaseComposableChart

/**
 * Point implementation to be used in [BaseComposableChart]
 */
data class ValuePoint(
    override val x: Long,
    override val y: Float
) : ChartValuePoint