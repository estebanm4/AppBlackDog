package com.dadm.appblackdog.ui_elements

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GenericSpacer(space: Dp = 12.dp) {
    Spacer(modifier = Modifier.padding(vertical = space))
}