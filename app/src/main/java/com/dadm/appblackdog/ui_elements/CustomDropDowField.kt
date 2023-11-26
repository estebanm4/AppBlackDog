package com.dadm.appblackdog.ui_elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dadm.appblackdog.R
import com.dadm.appblackdog.ui.theme.primaryRed

@Composable
fun CustomDropDownField(
    value: String,
    icon: ImageVector,
    items: MutableList<String>,
    label: String = "",
    onItemChange: (String) -> Unit,
    error: Boolean = false,
    errorLabel: String = "$label ${stringResource(id = R.string.required)}",
    ) {
    var expanded by remember { mutableStateOf(false) }

    val leadingIcon = @Composable {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = if (error) primaryRed else MaterialTheme.colorScheme.primary
        )
    }
    val trailingIcon = @Composable {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = "",
                tint = if (error) primaryRed else MaterialTheme.colorScheme.primary
            )
        }
    }
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = value,
            isError = error,
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            label = { Text(if (error) errorLabel else label) },
            placeholder = { Text(if (error) errorLabel else label) },
            singleLine = true,
            onValueChange = {},
            trailingIcon = trailingIcon,
            leadingIcon = leadingIcon
        )
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = { Text(text = item) },
                    onClick = {
                        expanded = false
                        onItemChange(item)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun CustomDropDownFieldPreview() {
    CustomDropDownField(value = "", Icons.Default.Pets, items = arrayListOf(""), onItemChange = {})
}