package com.dadm.appblackdog.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dadm.appblackdog.R
import com.dadm.appblackdog.models.UiReminder
import com.dadm.appblackdog.ui.theme.AppBlackDogTheme
import com.dadm.appblackdog.ui_elements.CalendarItem
import com.dadm.appblackdog.ui_elements.MainAppBar
import com.dadm.appblackdog.viewmodels.ReminderViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun CalendarScreen(
    drawerState: DrawerState,
    reminderViewModel: ReminderViewModel
) {
    val scope = rememberCoroutineScope()
    val uiState by reminderViewModel.uiState.collectAsState()

    when {
        uiState.items.isEmpty() -> {
            reminderViewModel.loadReminders()
        }
    }
    //content
    Scaffold(
        topBar = {
            Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                MainAppBar(
                    label = stringResource(id = R.string.calendar),
                    trailingIcon = Icons.Default.Update,
                    leadingAction = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    trailingAction = {
                    }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillHeight,
                painter = painterResource(id = R.drawable.reminder_background),
                contentDescription = stringResource(
                    id = R.string.default_description
                )
            )
            Box(modifier = Modifier.padding(padding)) {
                CalendarScreenBody(uiState)
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun CalendarScreenBody(uiState: UiReminder) {
    val cardRadius = 40.dp
    val titleTextSize = 20.sp
    val subTitleTextSize = 16.sp
    val bodyTextSize = 12.sp
    val space = 16.dp
    val dateFormat = SimpleDateFormat("dd/MMM/yyyy")

    Column {
        Card(
            modifier = Modifier
                .weight(2f)
                .padding(12.dp)
                .fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
            )
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    fontSize = titleTextSize,
                    text = dateFormat.format(Date()),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                CustomCalendar(uiState)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(7f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            uiState.items.forEach {
                val date = Date(it.time)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                        .border(
                            2.dp,
                            Color.White,
                            RoundedCornerShape(topStart = cardRadius, bottomStart = cardRadius)
                        ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    shape = RoundedCornerShape(topStart = cardRadius, bottomStart = cardRadius),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                    ),
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = it.name,
                            fontSize = titleTextSize,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 2
                        )
                        Text(
                            text = dateFormat.format(date),
                            fontSize = subTitleTextSize,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.size(space))
                        Divider(color = Color.White, thickness = 1.dp)
                        Spacer(modifier = Modifier.size(space))
                        Text(
                            fontSize = bodyTextSize,
                            text = it.description,
                            fontWeight = FontWeight.Normal,
                            color = Color.White,
                        )
                    }
                }
                Spacer(modifier = Modifier.size(space))
            }
        }
    }
}

@Composable
fun CustomCalendar(uiState: UiReminder) {
    LazyRow {
        items(uiState.calendarItems) { date ->
            CalendarItem(date.day, date.value, date.selected)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {
    AppBlackDogTheme {
        Surface {
            CalendarScreenBody(UiReminder())
        }
    }
}