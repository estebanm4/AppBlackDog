package com.dadm.appblackdog.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dadm.appblackdog.R
import com.dadm.appblackdog.models.UiInfoPost
import com.dadm.appblackdog.ui.theme.AppBlackDogTheme
import com.dadm.appblackdog.ui_elements.MainAppBar
import com.dadm.appblackdog.viewmodels.InfoViewModel
import kotlinx.coroutines.launch

@Composable
fun InfoScreen(
    drawerState: DrawerState,
    infoViewModel: InfoViewModel
) {
    val scope = rememberCoroutineScope()
    val uiState by infoViewModel.uiState.collectAsState()

    when {
        uiState.items.isEmpty() -> {
            infoViewModel.loadPost()
        }
    }
    Scaffold(
        topBar = {
            Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                MainAppBar(
                    label = stringResource(id = R.string.more_info),
                    trailingIcon = Icons.Default.Send,
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
                painter = painterResource(id = R.drawable.dog_info),
                contentDescription = stringResource(
                    id = R.string.default_description
                )
            )
            InfoScreenBody(uiState, padding)
        }
    }
}

@Composable
fun InfoScreenBody(uiState: UiInfoPost, padding: PaddingValues) {
    val cardRadius = 40.dp
    val titleTextSize =
        with(LocalDensity.current) { dimensionResource(id = R.dimen.title_large).toSp() }


    Column(
        modifier = Modifier
//            .padding(42.dp)
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        uiState.items.forEach {
            Card(
                modifier = Modifier
                    .fillMaxWidth().padding(start = 16.dp)
                    .border(
                        2.dp,
                        Color.White,
                        RoundedCornerShape(topStart = cardRadius, bottomStart = cardRadius)
                    ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                shape = RoundedCornerShape(topStart = cardRadius),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                ),
            ) {
                Column(modifier = Modifier.padding(42.dp)) {
                    Text(
                        text = it.name,
                        fontSize = titleTextSize,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.size(24.dp))
                    Divider(color = Color.White, thickness = 1.5.dp)
                    Spacer(modifier = Modifier.size(24.dp))
                    Text(
                        text = it.description,
//                    fontSize = titleTextSize,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                    )
                }
            }
            Spacer(modifier = Modifier.size(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InfoScreenPreview() {
    AppBlackDogTheme {
        InfoScreenBody(UiInfoPost(), PaddingValues(top = 32.dp))
    }
}