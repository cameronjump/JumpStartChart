package com.jumpstartcharts.sample.ui.mock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomAppBarDefaults.ContentPadding
import androidx.compose.material3.ButtonDefaults.ContentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SampleScreen(samples: List<SampleItem>) {
    LazyColumn(
        modifier = Modifier.background(Color.White),
        state = rememberLazyListState(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        items(samples) {
            Text(text = it.title)
            Spacer(modifier = Modifier.height(16.dp))
            it.show()
        }
    }
}
