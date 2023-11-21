package com.dadm.appblackdog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dadm.appblackdog.ui.theme.AppBlackDogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppBlackDogTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HelloOwner()
                }
            }
        }
    }
}

@Composable
fun HelloOwner() {
    //data
    val context = LocalContext.current
    //content
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.owner_profile),

            contentDescription = "owner",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .border(
                    1.5.dp,
                    MaterialTheme.colorScheme.secondary,
                    CircleShape
                )
                .clickable {
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    (context as Activity).finish()
                }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppBlackDogTheme {
        HelloOwner()
    }
}