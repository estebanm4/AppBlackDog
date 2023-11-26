package com.dadm.appblackdog.ui_elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.dadm.appblackdog.R
import com.dadm.appblackdog.ui.theme.primaryRed

@Composable
fun CustomDateField(
    value: String,
    icon: ImageVector,
    label: String = "",
    onResult: (String) -> Unit,
    error: Boolean = false,
    errorLabel: String = "$label ${stringResource(id = R.string.required)}",
) {
    var openAlertDialog by remember { mutableStateOf(false) }

    val trailingIcon = @Composable {
        IconButton(
            onClick = { openAlertDialog = !openAlertDialog }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.default_description),
                tint = if (error) primaryRed else MaterialTheme.colorScheme.primary
            )
        }
    }
    val leadingIcon = @Composable {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = if (error) primaryRed else MaterialTheme.colorScheme.primary
        )
    }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        isError = error,
        onValueChange = {},
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        label = { Text(if (error) errorLabel else label) },
        readOnly = true
        //            enabled = false
    )
    when {
        openAlertDialog -> CustomDatePicker(
            onDateSelected = onResult,
            onDismiss = {
                openAlertDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val openDialog = remember { mutableStateOf(true) }
    val state = rememberDatePickerState()

    if (openDialog.value) {
        DatePickerDialog(
            onDismissRequest = {
                openDialog.value = false
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onDateSelected(if (state.selectedDateMillis != null) state.selectedDateMillis.toString() else "")
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                        openDialog.value = false
                    }
                ) {
                    Text("CANCEL")
                }
            }
        ) {
            DatePicker(state = state)
        }
    }
}