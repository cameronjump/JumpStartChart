package com.jumpstartcharts.jumpstartcharts.data

/**
 * Defines how a graph may be interacted with
 */
sealed class ScrubbingBehavior {

    abstract val supportGraphTouchScrubbing: Boolean
    abstract val alwaysHighlightValue: Boolean
    abstract val shouldStartScrubberOnLeft: Boolean
    abstract val resetPositionAfterDelay: Boolean

    /**
     * The graph will allow highlighting by direct touch input only.
     */
    data class GraphTouchScrubbingBehavior(
        override val alwaysHighlightValue: Boolean = false,
        override val shouldStartScrubberOnLeft: Boolean = true,
        override val resetPositionAfterDelay: Boolean = true,
    ) : ScrubbingBehavior() {
        override val supportGraphTouchScrubbing = true
    }

    object NoScrubbing : ScrubbingBehavior() {

        override val supportGraphTouchScrubbing: Boolean = false
        override val alwaysHighlightValue: Boolean = false
        override val shouldStartScrubberOnLeft: Boolean = true
        override val resetPositionAfterDelay: Boolean = false
    }
}
