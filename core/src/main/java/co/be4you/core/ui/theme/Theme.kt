package co.be4you.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = CommonBlue,
    onPrimary = White,
    secondary = BrandLightBlue,
    onSecondary = White,
    background = White,
    onBackground = Black,
    error = Error,
    onError = White,
    surface = White,
    onSurface = Black,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant,
)

@Composable
fun IndoorLocalizationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
