package com.jumpstartcharts.sample.ui.mock

import androidx.compose.runtime.Composable

data class SampleItem(
    val title: String,
    val show: @Composable () -> Unit,
) {
    val route: String = title.replace(" ", "_")
}
