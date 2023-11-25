package com.dadm.appblackdog.ui_elements

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import com.dadm.appblackdog.R
import com.dadm.appblackdog.ui.theme.primaryRed

@Composable
fun EmailField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: Boolean = false,
    label: String = stringResource(id = R.string.email_label),
    placeholder: String = stringResource(id = R.string.email_placeholder),
    errorLabel: String = stringResource(id = R.string.invalid_email),
) {
    // data
    val focusManager = LocalFocusManager.current
    val leadingIcon = @Composable {
        Icon(
            imageVector = Icons.Default.Mail,
            contentDescription = "",
            tint = if (error) primaryRed else MaterialTheme.colorScheme.primary
        )
    }
    // UI
    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        placeholder = { Text(if (error) errorLabel else placeholder) },
        label = { Text(if (error) errorLabel else label) },
        singleLine = true,
        isError = error,
        visualTransformation = VisualTransformation.None
    )

}