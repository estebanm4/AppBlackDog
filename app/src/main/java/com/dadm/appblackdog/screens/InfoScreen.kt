package com.dadm.appblackdog.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dadm.appblackdog.LoginActivity
import com.dadm.appblackdog.R
import com.dadm.appblackdog.ui.theme.AppBlackDogTheme

@Composable
fun InfoScreen(
    action: () -> Unit,
    ) {
    //content
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Info")
        Spacer(modifier = Modifier.padding(vertical = 5.dp))
        Image(
            painter = painterResource(id = R.drawable.owner_profile),

            contentDescription = "owner",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(250.dp)
                .clip(CircleShape)
                .border(
                    3.dp,
                    MaterialTheme.colorScheme.secondary,
                    CircleShape
                )
        )
        Spacer(modifier = Modifier.padding(vertical = 5.dp))
        OutlinedButton(onClick = action) {
            Text(text = stringResource(id = R.string.finish_button))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InfoScreenPreview() {
    AppBlackDogTheme {
        InfoScreen(action = {})
    }
}