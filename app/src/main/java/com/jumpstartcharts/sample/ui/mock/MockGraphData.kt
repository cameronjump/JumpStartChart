package com.jumpstartcharts.sample.ui.mock

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.jumpstartcharts.jumpstartcharts.data.ChartDataset
import com.jumpstartcharts.jumpstartcharts.data.ChartPoint
import com.jumpstartcharts.sample.ui.theme.Pink40
import com.jumpstartcharts.sample.ui.theme.Pink80
import com.jumpstartcharts.sample.ui.theme.Purple40
import com.jumpstartcharts.sample.ui.theme.Purple80
import com.jumpstartcharts.sample.ui.theme.PurpleGrey40
import com.jumpstartcharts.sample.ui.theme.PurpleGrey80

internal object MockGraphData {

    data class MockPoint(
        override val x: Long,
        override val y: Float,
        val label: String,
    ) : ChartPoint

    data class MockDataset<T : ChartPoint>(
        override val color: Color = Color.Black,
        override val highlightColor: Color = lerp(color, Color.White, .5f),
        override val points: List<T>,
    ) : ChartDataset<T>

    val MOCK_LINE_CHART_DATA_A = listOf(
        MockPoint(0, 1234.0f, "Jan 2020"),
        MockPoint(1, 1532.0f, "Feb 2020"),
        MockPoint(2, 1532.0f, "Mar 2020"),
        MockPoint(3, 1153.0f, "Apr 2020"),
        MockPoint(4, 2352.0f, "May 2020"),
        MockPoint(5, 1532.0f, "Jun 2020"),
        MockPoint(6, 1914.0f, "Jul 2020"),
        MockPoint(7, 2034.0f, "Aug 2020"),
        MockPoint(8, 2132.0f, "Sep 2020"),
        MockPoint(9, 1332.0f, "Oct 2020"),
        MockPoint(10, 2432.0f, "Nov 2020"),
        MockPoint(11, 2634.0f, "Dec 2020"),
        MockPoint(12, 2332.0f, "Jan 2021"),
        MockPoint(13, 2513.0f, "Feb 2021"),
        MockPoint(14, 2843.0f, "Mar 2021"),
    )

    val MOCK_LINE_CHART_DATASET_A = MockDataset(
        points = MOCK_LINE_CHART_DATA_A,
        color = Purple40,
    )

    val MOCK_LINE_CHART_DATA_B = listOf(
        MockPoint(0, 1355.0f, "Jan 2020"),
        MockPoint(1, 1560.0f, "Feb 2020"),
        MockPoint(2, 1621.0f, "Mar 2020"),
        MockPoint(3, 1250.0f, "Apr 2020"),
        MockPoint(4, 2100.0f, "May 2020"),
        MockPoint(5, 1402.0f, "Jun 2020"),
        MockPoint(6, 2052.0f, "Jul 2020"),
        MockPoint(7, 2224.0f, "Aug 2020"),
        MockPoint(8, 1632.0f, "Sep 2020"),
        MockPoint(9, 2000.0f, "Oct 2020"),
        MockPoint(10, 2150.0f, "Nov 2020"),
        MockPoint(11, 2912.0f, "Dec 2020"),
        MockPoint(12, 2523.0f, "Jan 2021"),
        MockPoint(13, 2421.0f, "Feb 2021"),
        MockPoint(14, 2299.0f, "Mar 2021"),
    )

    val MOCK_LINE_CHART_DATASET_B = MockDataset(
        points = MOCK_LINE_CHART_DATA_B,
        color = PurpleGrey40,
    )

    private val MOCK_LINE_CHART_DATA_NEGATIVE_VALUES = listOf(
        MockPoint(0, -235.0f, "Jan 2020"),
        MockPoint(1, -211.0f, "Feb 2020"),
        MockPoint(2, -5.0f, "Mar 2020"),
        MockPoint(3, -509.0f, "Apr 2020"),
        MockPoint(4, -550.0f, "May 2020"),
        MockPoint(5, -700.0f, "Jun 2020"),
        MockPoint(6, -300.0f, "Jul 2020"),
        MockPoint(7, -200.0f, "Aug 2020"),
        MockPoint(8, -150.0f, "Sep 2020"),
        MockPoint(9, -10.0f, "Oct 2020"),
        MockPoint(10, -15.0f, "Nov 2020"),
        MockPoint(11, -20.0f, "Dec 2020"),
        MockPoint(12, -11.0f, "Jan 2021"),
        MockPoint(13, -0.0f, "Feb 2021"),
        MockPoint(14, -2.0f, "Mar 2021"),
    )

    val MOCK_LINE_CHART_DATASET_C = MockDataset(
        points = MOCK_LINE_CHART_DATA_NEGATIVE_VALUES,
        color = Pink40,
    )

    private val MOCK_LINE_CHART_DATA_D = listOf(
        MockPoint(0, 135.0f, "Jan 2020"),
        MockPoint(1, 551.0f, "Feb 2020"),
        MockPoint(2, 150.0f, "Mar 2020"),
        MockPoint(3, -400.0f, "Apr 2020"),
        MockPoint(4, -650.0f, "May 2020"),
        MockPoint(5, -1200.0f, "Jun 2020"),
        MockPoint(6, -1700.0f, "Jul 2020"),
        MockPoint(7, -200.0f, "Aug 2020"),
        MockPoint(8, 300.0f, "Sep 2020"),
        MockPoint(9, 700.0f, "Oct 2020"),
        MockPoint(10, 2000.0f, "Nov 2020"),
        MockPoint(11, 2100f, "Dec 2020"),
        MockPoint(12, 900.0f, "Jan 2021"),
        MockPoint(13, 550f, "Feb 2021"),
        MockPoint(14, 300.0f, "Mar 2021"),
    )

    val MOCK_LINE_CHART_DATASET_D = MockDataset(
        points = MOCK_LINE_CHART_DATA_D,
        color = Pink80,
    )

    private val MOCK_BAR_CHART_DATA_A = listOf(
        MockPoint(0, 135.0f, "Jan 2020"),
        MockPoint(1, 551.0f, "Feb 2020"),
        MockPoint(2, 150.0f, "Mar 2020"),
        MockPoint(3, -400.0f, "Apr 2020"),
        MockPoint(4, -650.0f, "May 2020"),
        MockPoint(5, -1200.0f, "Jun 2020"),
    )

    val MOCK_BAR_CHART_DATASET_A = MockDataset(
        points = MOCK_BAR_CHART_DATA_A,
        color = Purple40,
        highlightColor = Purple80,
    )

    private val MOCK_BAR_CHART_DATA_B = listOf(
        MockPoint(0, 115.0f, "Jan 2020"),
        MockPoint(1, 231.0f, "Feb 2020"),
        MockPoint(2, 80.0f, "Mar 2020"),
        MockPoint(3, -300.0f, "Apr 2020"),
        MockPoint(4, 400.0f, "May 2020"),
        MockPoint(5, -330.0f, "Jun 2020"),
    )

    val MOCK_BAR_CHART_DATASET_B = MockDataset(
        points = MOCK_BAR_CHART_DATA_B,
        color = PurpleGrey40,
        highlightColor = PurpleGrey80,
    )

    private val MOCK_BAR_CHART_DATA_C = listOf(
        MockPoint(0, -735.0f, "Jan 2020"),
    )

    val MOCK_BAR_CHART_DATASET_C = MockDataset(
        points = MOCK_BAR_CHART_DATA_C,
        color = Pink40,
        highlightColor = Pink80,
    )

    private val MOCK_BAR_CHART_DATA_D = listOf(
        MockPoint(0, 235.0f, "Jan 2020"),
    )

    val MOCK_BAR_CHART_DATASET_D = MockDataset(
        points = MOCK_BAR_CHART_DATA_D,
        color = Color.Magenta,

    )
}
