package com.dadm.appblackdog.ui_elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dadm.appblackdog.R

@Composable
fun CustomIconItem(icon: Painter, text: String, maxLines: Int = 1) {

    val textSize =
        with(LocalDensity.current) { dimensionResource(id = R.dimen.text_large).toSp() }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = icon,
            contentDescription = "",
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(text = text, color = Color.White, fontSize = textSize, maxLines = maxLines)
    }
}