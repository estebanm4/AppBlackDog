package com.dadm.appblackdog.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dadm.appblackdog.R
import com.dadm.appblackdog.models.UiLogin
import com.dadm.appblackdog.ui.theme.AppBlackDogTheme
import com.dadm.appblackdog.ui_elements.EmailField
import com.dadm.appblackdog.ui_elements.PasswordField
import com.dadm.appblackdog.viewmodels.AppViewModelProvider
import com.dadm.appblackdog.viewmodels.LoginViewModel
import com.dadm.appblackdog.viewmodels.RegisterViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginFormScreen(
    loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory),
    registerViewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory),
    actionNavigation: () -> Unit,
) {
    val uiState by loginViewModel.uiState.collectAsState()

    // UI
    if (!uiState.isLoaderEnable)
        LoginForm(loginViewModel, uiState, registerViewModel, actionNavigation)
    else Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }

}

@Composable
fun LoginForm(
    loginViewModel: LoginViewModel?,
    uiState: UiLogin,
    registerViewModel: RegisterViewModel?,
    actionNavigation: () -> Unit,
) {

    //variables
    val context = LocalContext.current
    val modifier = Modifier
        .padding(dimensionResource(id = R.dimen.padding_large))
        .fillMaxHeight()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Image(
                painter = painterResource(id = R.drawable.dog_1),
                contentDescription = stringResource(id = R.string.default_description),
                modifier = Modifier
                    .size(150.dp)
            )
            Spacer(modifier = Modifier.size(12.dp))
            EmailField(
                value = uiState.email,
                onChange = { data -> loginViewModel?.updateEmail(data) },
                modifier = Modifier.fillMaxWidth()
            )
            PasswordField(
                value = uiState.password,
                modifier = Modifier.fillMaxWidth(),
                onChange = { data -> loginViewModel?.updatePassword(data) },
                submit = {
                    loginViewModel?.startLogin(context = context)
                },
            )
            Button(
                onClick = {
                    loginViewModel?.startLogin(context = context)
                },
                enabled = true,
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(id = R.string.login))
            }
            Button(
                onClick = {
                    registerViewModel?.resetData()
                    actionNavigation.invoke()
                },
                enabled = true,
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(id = R.string.register))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginFormPreview() {
    AppBlackDogTheme {
        LoginForm(
            loginViewModel = null,
            uiState = UiLogin(),
            registerViewModel = null,
            actionNavigation = {}
        )
    }
}