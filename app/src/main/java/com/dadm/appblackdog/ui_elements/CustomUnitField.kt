package com.dadm.appblackdog.ui_elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomUnitField(
    value: String,
    valueSelector: String,
    items: MutableList<String>,
    icon: ImageVector,
    label: String = "",
    selectorLabel: String = "",
    onChange: (String) -> Unit,
    onItemChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    var expanded by remember { mutableStateOf(false) }

    val trailingIcon = @Composable {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        CustomInputText(
            modifier = Modifier
                .weight(2f)
                .padding(end = 8.dp),
            value = value,
            label = label,
            icon = icon,
            onChange = onChange,
            keyboardType = keyboardType
        )
        Box(
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                value = valueSelector,
                readOnly = true,
                singleLine = true,
                label = { Text(selectorLabel, maxLines = 1) },
                placeholder = { Text(selectorLabel, maxLines = 1) },
                onValueChange = {},
                trailingIcon = trailingIcon
            )
            DropdownMenu(
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
}

@Preview
@Composable
fun CustomUnitFieldPreview() {
    CustomUnitField(
        value = "",
        valueSelector = "",
        icon = Icons.Default.Person,
        onChange = {},
        onItemChange = {},
        items = arrayListOf("", "")
    )
}