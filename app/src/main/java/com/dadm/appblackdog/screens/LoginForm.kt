package com.dadm.appblackdog.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dadm.appblackdog.R
import com.dadm.appblackdog.ui.theme.AppBlackDogTheme
import com.dadm.appblackdog.ui_elements.EmailField
import com.dadm.appblackdog.ui_elements.LabelCheckbox
import com.dadm.appblackdog.ui_elements.PasswordField
import com.dadm.appblackdog.viewmodels.LoginViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun LoginForm(
    loginViewModel: LoginViewModel = viewModel()
) {

    //variables
    val context = LocalContext.current
    val uiState by loginViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    // UI
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
    ) {
        EmailField(
            value = uiState.email,
            onChange = { data -> loginViewModel.updateEmail(data) },
            modifier = Modifier.fillMaxWidth()
        )
        PasswordField(
            value = uiState.password,
            modifier = Modifier.fillMaxWidth(),
            onChange = { data -> loginViewModel.updatePassword(data) },
            submit = {
                scope.launch { loginViewModel.firebaseLogin(context = context) }
            },
        )
        LabelCheckbox(
            label = stringResource(id = R.string.remember_me),
            onCheckChanged = {
                loginViewModel.updateRememberMe()
            },
            isChecked = uiState.remember
        )
        Button(
            onClick = {
                scope.launch { loginViewModel.firebaseLogin(context = context) }
            },
            enabled = true,
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.login))
        }
    }

}

@Preview(showBackground = true)
@Composable
fun LoginFormPreview() {
    AppBlackDogTheme {
        LoginForm()
    }
}