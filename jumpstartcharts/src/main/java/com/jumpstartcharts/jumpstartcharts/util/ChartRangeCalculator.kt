package com.jumpstartcharts.jumpstartcharts.util

import com.jumpstartcharts.jumpstartcharts.data.ChartValuePoint
import com.jumpstartcharts.jumpstartcharts.data.Dataset

object ChartRangeCalculator {

    /**
     * Given [dataSets] finds the max value, then calculates the upper bound based on the
     * [roundValue] lambda
     */
    fun <ChartPoint : ChartValuePoint> upperBound(
        dataSets: List<Dataset<ChartPoint>>,
        roundValue: (value: Float) -> Float,
    ): Float {
        val maxYValue = dataSets.flatMap { it.points }.maxOfOrNull { it.y } ?: run {
            return Float.MAX_VALUE
        }

        return roundValue(maxYValue)
    }

    /**
     * Given [dataSets] finds the min value, then calculates the lower bound based on the
     * [roundValue] lambda
     */
    fun <ChartPoint : ChartValuePoint> lowerBound(
        dataSets: List<Dataset<ChartPoint>>,
        roundValue: (value: Float) -> Float,
    ): Float {
        val minYValue = dataSets.flatMap { it.points }.minOfOrNull { it.y } ?: run {
            return 0f
        }

        return roundValue(minYValue)
    }

    /**
     * Given [dataSets] finds the upper bound based on [defaultUpperBound]
     */
    fun <ChartPoint : ChartValuePoint> defaultUpperBound(
        dataSets: List<Dataset<ChartPoint>>,
    ): Float {
        return upperBound(
            dataSets,
            roundValue = ::defaultUpperBound,
        )
    }

    /**
     * Given [dataSets] finds the lower bound based on [defaultLowerBound]
     */
    fun <ChartPoint : ChartValuePoint> defaultLowerBound(
        dataSets: List<Dataset<ChartPoint>>,
    ): Float {
        return lowerBound(
            dataSets,
            roundValue = ::defaultLowerBound,
        )
    }

    /**
     * Calculates default upper bound,  if [value] is negative set min to 0, otherwise
     * rounds up to nearest hundred
     */
    fun defaultUpperBound(value: Float): Float {
        return if (value <= 0f) {
            0f
        } else {
            roundUpToNearestX(value = value, x = HUNDRED)
        }
    }

    /**
     * Calculates default lower bound, if [value] is positive set min to 0, otherwise
     * rounds down to nearest hundred
     */
    fun defaultLowerBound(value: Float): Float {
        return if (value >= 0f) {
            0f
        } else {
            roundDownToNearestX(value = value, x = HUNDRED)
        }
    }

    /**
     * Rounds [value] up to nearest [x] interval
     */
    fun roundUpToNearestX(value: Float, x: Int): Float {
        val rounded: Int = if (value >= 0) {
            ((value.toInt() + (x - 1)) / x) * x
        } else {
            (value.toInt() / x) * x
        }

        return rounded.toFloat()
    }

    /**
     * Rounds [value] down to nearest [x] interval
     */
    fun roundDownToNearestX(value: Float, x: Int): Float {
        val rounded: Int = if (value >= 0) {
            (value.toInt() / x) * x
        } else {
            ((value.toInt() - (x - 1)) / x) * x
        }

        return rounded.toFloat()
    }

    const val HUNDRED = 100
    const val THOUSAND = 1000
}
