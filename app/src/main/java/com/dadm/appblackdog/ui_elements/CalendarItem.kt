package com.dadm.appblackdog.ui_elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalendarItem(day: String, date: String, selected: Boolean = false) {
    Card(
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
        ),
    ) {
        Column(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .padding(4.dp)
        ) {
            Text(
                text = day,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = date,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}