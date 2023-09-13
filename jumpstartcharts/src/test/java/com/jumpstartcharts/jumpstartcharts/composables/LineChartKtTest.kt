package com.jumpstartcharts.jumpstartcharts.composables

import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

class LineChartKtTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
        // ...see docs for more options
    )

//    @Test
//    fun launchView() {
//        val view = LaunchView(paparazzi.context)
//        // or...
//        // val view = LaunchView(paparazzi.context)
//
//        view.setModel(LaunchModel(title = "paparazzi"))
//        paparazzi.snapshot(view)
//    }

    @Test
    fun launchComposable() {
        paparazzi.snapshot {
            BarChartPreview()
        }
    }
}
