package com.dadm.appblackdog.ui_elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dadm.appblackdog.R
import kotlinx.coroutines.launch

@Composable
fun MainAppBar(
    label: String = stringResource(id = R.string.app_name),
    hideEndIcon: Boolean = false,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()
    val fontSize = 24.sp
    val contentModifier = Modifier
        .fillMaxWidth()
        .height(72.dp)
        .padding(4.dp)
//    containerColor = MaterialTheme.colorScheme.primaryContainer,
//    titleContentColor = MaterialTheme.colorScheme.primary,

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = contentModifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(id = R.string.default_description)
                )
            }
            Text(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = fontSize,
                fontWeight = FontWeight.Normal,
                text = label
            )
            Box(modifier = Modifier.weight(1f)) {
                if (!hideEndIcon) IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Update,
                        contentDescription = stringResource(id = R.string.default_description)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MainAppBarPreview() {
    MainAppBar(drawerState = DrawerState(DrawerValue.Closed))
}