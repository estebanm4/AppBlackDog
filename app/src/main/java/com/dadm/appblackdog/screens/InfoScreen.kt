package com.dadm.appblackdog.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dadm.appblackdog.R
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

    Scaffold(
        topBar = {
            Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                MainAppBar(
                    label = stringResource(id = R.string.more_info),
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
        Box(modifier = Modifier.padding(padding)) {
            InfoScreenBody()
        }
    }
}

@Composable
fun InfoScreenBody() {

}

@Preview(showBackground = true)
@Composable
fun InfoScreenPreview() {
    AppBlackDogTheme {
        InfoScreenBody()
    }
}