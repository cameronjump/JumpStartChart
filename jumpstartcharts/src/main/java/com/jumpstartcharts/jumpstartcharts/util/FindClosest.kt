package com.jumpstartcharts.jumpstartcharts.util

internal object FindClosest {

    /**
     * Use binary search to find closet value xValue
     */
    fun findClosestValue(
        newXPosition: Float,
        sortedXPositions: FloatArray,
        sortedXValues: LongArray,
    ): Long? {
        if (sortedXPositions.size != sortedXValues.size) {
            return null
        }

        if (sortedXPositions.isEmpty()) {
            return null
        }

        val index = findClosestIndex(sortedXPositions, newXPosition)
        return sortedXValues[index]
    }

    /**
     * Returns element closest to target in arr[]
     * https://www.geeksforgeeks.org/find-closest-number-array/
     */
    private fun findClosestIndex(arr: FloatArray, target: Float): Int {
        val n = arr.size

        // Corner cases
        if (target <= arr[0]) return 0
        if (target >= arr[n - 1]) return n - 1

        // Doing binary search
        var i = 0
        var j = n
        var mid = 0
        while (i < j) {
            mid = (i + j) / 2
            if (arr[mid] == target) return mid

            /* If target is less than array element,
           then search in left */
            if (target < arr[mid]) {
                // If target is greater than previous
                // to mid, return closest of two
                if (mid > 0 && target > arr[mid - 1]) {
                    return getClosestIndex(
                        arr[mid - 1],
                        mid - 1,
                        arr[mid],
                        mid,
                        target,
                    )
                }

                /* Repeat for left half */j = mid
            } else {
                if (mid < n - 1 && target < arr[mid + 1]) {
                    return getClosestIndex(
                        arr[mid],
                        mid,
                        arr[mid + 1],
                        mid + 1,
                        target,
                    )
                }
                i = mid + 1 // update i
            }
        }

        // Only single element left after search
        return mid
    }

    /**
     * Method to compare which one is the more close. We find the closest by taking the difference
     * between the target and both values. It assumes that val2 is greater than val1 and target
     * lies between these two.
     */
    private fun getClosestIndex(
        val1: Float,
        index1: Int,
        val2: Float,
        index2: Int,
        target: Float,
    ): Int {
        return if (target - val1 >= val2 - target) index2 else index1
    }
}
