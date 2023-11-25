package com.dadm.appblackdog.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dadm.appblackdog.R
import com.dadm.appblackdog.models.UiRegister
import com.dadm.appblackdog.ui.theme.AppBlackDogTheme
import com.dadm.appblackdog.ui.theme.primaryRed
import com.dadm.appblackdog.ui_elements.CustomInputText
import com.dadm.appblackdog.ui_elements.EmailField
import com.dadm.appblackdog.ui_elements.PasswordField
import com.dadm.appblackdog.viewmodels.AppViewModelProvider
import com.dadm.appblackdog.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by registerViewModel.uiState.collectAsState()

    // UI
    RegisterBody(uiState, registerViewModel)

}


@Composable
fun RegisterBody(uiState: UiRegister, registerViewModel: RegisterViewModel) {
    val dividerSize = 100.dp
    val space = 50.dp
    val space2 = 12.dp
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_large))
            .imePadding()
            .verticalScroll(rememberScrollState())
            .fillMaxHeight()
//            .weight(weight =1f, fill = false)

    ) {
        /** Header */
        Text(
            text = stringResource(id = R.string.register_title),
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.size(space2))
        Divider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(dividerSize)
        )
        /** form */
        Spacer(modifier = Modifier.size(space))
        RegisterForm(uiState, registerViewModel)
        Spacer(modifier = Modifier.size(space / 2))
        /** action buttons */
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            /** cancel button */
            Button(
                onClick = {},
                enabled = true,
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(stringResource(id = R.string.cancel))
            }
            /** create button */
            Button(
                onClick = { registerViewModel.validateForm() },
                enabled = true,
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(stringResource(id = R.string.create_user))
            }
        }
    }
}

@Composable
fun RegisterForm(uiState: UiRegister, registerViewModel: RegisterViewModel) {

    val modifier = Modifier
        .fillMaxWidth()
        .padding(dimensionResource(id = R.dimen.padding_medium))
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        /** name textField */
        CustomInputText(
            error = uiState.nameError,
            value = uiState.name,
            icon = Icons.Default.Person,
            label = stringResource(id = R.string.name),
            onChange = { data -> registerViewModel.updateName(data) },
            modifier = Modifier.fillMaxWidth()
        )
        /** lastname textField */
        CustomInputText(
            error = uiState.lastnameError,
            value = uiState.lastname,
            icon = Icons.Default.Person,
            label = stringResource(id = R.string.lastname),
            onChange = { data -> registerViewModel.updateLastname(data) },
            modifier = Modifier.fillMaxWidth()
        )
        /** email textField */
        EmailField(
            error = uiState.emailError,
            value = uiState.email,
            onChange = { data -> registerViewModel.updateEmail(data) },
            modifier = Modifier.fillMaxWidth()
        )
        /** password textField */
        PasswordField(
            error = uiState.passwordError,
            value = uiState.password,
            onChange = { data -> registerViewModel.updatePassword(data) },
            isLastField = false,
            modifier = Modifier.fillMaxWidth()
        )
        /** validated password textField */
        PasswordField(
            error = uiState.validatePasswordError,
            value = uiState.validatedPassword,
            errorLabel = stringResource(id = R.string.invalid_password_confirm),
            onChange = { data -> registerViewModel.updateValidatePassword(data) },
            label = stringResource(id = R.string.validated_password),
            placeholder = stringResource(R.string.validated_password),
            isLastField = false,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    AppBlackDogTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            RegisterBody(
                uiState = UiRegister(),
                RegisterViewModel(
                    firebaseService = null,
                    ownerRepository = null,
                )
            )
        }
    }
}