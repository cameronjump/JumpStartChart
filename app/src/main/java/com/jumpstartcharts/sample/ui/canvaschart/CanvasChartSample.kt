package com.jumpstartcharts.sample.ui.canvaschart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun CanvasChartStepOne() {
    Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
    }
}
