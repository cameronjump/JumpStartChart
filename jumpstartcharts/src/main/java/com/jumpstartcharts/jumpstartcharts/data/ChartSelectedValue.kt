package com.jumpstartcharts.jumpstartcharts.data

/**
 * Parcelable data class to hold selected [xValue] and if that value [isHighlighted]
 */
data class ChartSelectedValue(
    val xValue: Long,
    val isHighlighted: Boolean,
) : java.io.Serializable
