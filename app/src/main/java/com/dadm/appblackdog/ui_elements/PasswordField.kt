package com.dadm.appblackdog.ui_elements

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.dadm.appblackdog.R
import com.dadm.appblackdog.ui.theme.primaryRed

@Composable
fun PasswordField(
    value: String,
    modifier: Modifier = Modifier,
    error: Boolean = false,
    onChange: (String) -> Unit,
    submit: () -> Unit = {},
    label: String = stringResource(id = R.string.password_label),
    placeholder: String = stringResource(id = R.string.password_placeholder),
    errorLabel: String = stringResource(id = R.string.invalid_password),
    isLastField: Boolean = true,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    // icons

    val leadingIcon = @Composable {
        Icon(
            Icons.Filled.Key,
            contentDescription = "",
            tint = if (error) primaryRed else MaterialTheme.colorScheme.primary
        )
    }
    val trailingIcon = @Composable {
        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
            Icon(
                if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                contentDescription = "",
                tint = if (error) primaryRed else MaterialTheme.colorScheme.primary
            )
        }
    }
    // UI
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            imeAction = if (isLastField) ImeAction.Done else ImeAction.Next,
            keyboardType = KeyboardType.Password,
        ),
        keyboardActions =
        if (isLastField)
            KeyboardActions(onDone = { submit() })
        else KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        placeholder = { Text(if (error) errorLabel else placeholder) },
        label = { Text(if (error) errorLabel else label) },
        singleLine = true,
        isError = error,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}