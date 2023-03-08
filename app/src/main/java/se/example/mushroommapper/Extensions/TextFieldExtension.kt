package se.example.mushroommapper.Extensions

import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

@Composable
fun ContentColorComponent(
    contentColor: Color = LocalContentColor.current,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        content = content)
}