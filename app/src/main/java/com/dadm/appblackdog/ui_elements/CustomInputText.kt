package com.dadm.appblackdog.ui_elements

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import com.dadm.appblackdog.R
import com.dadm.appblackdog.ui.theme.primaryRed

@Composable
fun CustomInputText(
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    isLastField: Boolean = false,
    enabled: Boolean = true,
    error: Boolean = false,
    onChange: (String) -> Unit,
    submit: () -> Unit = {},
    label: String = "",
    placeholder: String = label,
    errorLabel: String = "$label ${stringResource(id = R.string.required)}",
    errorPlaceholder: String = stringResource(id = R.string.required_field),
) {
    // data
    val focusManager = LocalFocusManager.current
    val leadingIcon = @Composable {
        Icon(
            imageVector = icon,
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
            imeAction = if (isLastField) ImeAction.Done else ImeAction.Next
        ),
        keyboardActions =
        if (isLastField)
            KeyboardActions(onDone = { submit() })
        else KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        placeholder = { Text(if (error) errorPlaceholder else placeholder) },
        label = { Text(if (error) errorLabel else label) },
        enabled = enabled,
        singleLine = true,
        isError = error,
        visualTransformation = VisualTransformation.None
    )
}