package com.jumpstartcharts.sample.ui.mock

import com.jumpstartcharts.sample.ui.canvaschart.CanvasChartSampleScreen

val samples = listOf(
    SampleItem("Canvas Chart Sample") {
        CanvasChartSampleScreen()
    },
    SampleItem("Line Chart Sample Screen") {
        LineChartSampleScreen()
    },
)
