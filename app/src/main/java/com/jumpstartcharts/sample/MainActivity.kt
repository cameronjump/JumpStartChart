package com.jumpstartcharts.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jumpstartcharts.sample.ui.canvaschart.CanvasChartSampleScreen
import com.jumpstartcharts.sample.ui.mock.SampleItem
import com.jumpstartcharts.sample.ui.mock.samples
import com.jumpstartcharts.sample.ui.theme.JumpStartChartsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JumpStartChartsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") { MainScreen(navController) }
                        samples.forEach { item ->
                            composable(item.route) { item.show() }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        samples.forEach {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(it.route) },
            ) {
                Text(text = it.title)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    JumpStartChartsTheme {
        MainScreen(rememberNavController())
    }
}
