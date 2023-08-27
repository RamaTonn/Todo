package com.ramatonn.todo.ui.theme

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.ramatonn.todo.R

private val TodoDarkColorScheme = darkColorScheme(
    primary = Blue80,
    onPrimary = Blue20,
    primaryContainer = Blue30,
    onPrimaryContainer = Blue90,
    inversePrimary = Blue40,
    secondary = DarkBlue80,
    onSecondary = DarkBlue20,
    secondaryContainer = DarkBlue30,
    onSecondaryContainer = DarkBlue90,
    tertiary = Yellow80,
    onTertiary = Yellow20,
    tertiaryContainer = Yellow30,
    onTertiaryContainer = Yellow90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = Grey10,
    onBackground = Grey90,
    surface = Grey10,
    onSurface = Grey80,
    inverseSurface = Grey90,
    inverseOnSurface = Grey20,
    surfaceVariant = BlueGrey30,
    onSurfaceVariant = BlueGrey80,
    outline = BlueGrey60
)

private val TodoLightColorScheme = lightColorScheme(
    primary = Blue40,
    onPrimary = Color.White,
    primaryContainer = Blue90,
    onPrimaryContainer = Blue10,
    inversePrimary = Blue80,
    secondary = DarkBlue40,
    onSecondary = Color.White,
    secondaryContainer = DarkBlue90,
    onSecondaryContainer = DarkBlue10,
    tertiary = Yellow40,
    onTertiary = Color.White,
    tertiaryContainer = Yellow90,
    onTertiaryContainer = Yellow10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = Grey99,
    onBackground = Grey10,
    surface = Grey99,
    onSurface = Grey10,
    inverseSurface = Grey20,
    inverseOnSurface = Grey95,
    surfaceVariant = BlueGrey90,
    onSurfaceVariant = BlueGrey30,
    outline = BlueGrey50
)

@SuppressLint("NewApi")
@Composable
fun TodoTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
) {
    val myColorScheme = when {

        isDarkTheme -> TodoDarkColorScheme
        else -> TodoLightColorScheme
    }

    MaterialTheme(
        colorScheme = myColorScheme, typography = Typography, content = content
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ThemeToggleButton(isDarkTheme: MutableState<Boolean>) {
    IconButton(onClick = { isDarkTheme.value = !isDarkTheme.value }) {
        AnimatedVisibility(
            visible = isDarkTheme.value, enter = scaleIn(), exit = scaleOut()
        ) {
            Icon(
                modifier = Modifier.animateEnterExit(),
                imageVector = ImageVector.vectorResource(id = R.drawable.round_dark_mode_24),
                contentDescription = "Dark Mode"
            )
        }

        AnimatedVisibility(
            visible = !isDarkTheme.value, enter = scaleIn(), exit = scaleOut()
        ) {
            Icon(
                modifier = Modifier.animateEnterExit(),
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_light_mode_24),
                contentDescription = "Light Mode"
            )
        }
    }
}

/*
@Composable
fun animateColorSchemeAsState(colorScheme: ColorScheme): ColorScheme {

    val primaryColor by animateColorAsState(
        targetValue = colorScheme.primary, animationSpec = tween(durationMillis = 300), label = ""
    )

    val onPrimaryColor by animateColorAsState(
        targetValue = colorScheme.onPrimary,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val primaryContainerColor by animateColorAsState(
        targetValue = colorScheme.primaryContainer,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val onPrimaryContainerColor by animateColorAsState(
        targetValue = colorScheme.onPrimaryContainer,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val inversePrimaryColor by animateColorAsState(
        targetValue = colorScheme.inversePrimary,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val secondaryColor by animateColorAsState(
        targetValue = colorScheme.secondary,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val onSecondaryColor by animateColorAsState(
        targetValue = colorScheme.onSecondary,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val secondaryContainer by animateColorAsState(
        targetValue = colorScheme.secondaryContainer,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val onSecondaryContainer by animateColorAsState(
        targetValue = colorScheme.onSecondaryContainer,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val tertiaryColor by animateColorAsState(
        targetValue = colorScheme.tertiary, animationSpec = tween(durationMillis = 300), label = ""
    )

    val onTertiaryColor by animateColorAsState(
        targetValue = colorScheme.onTertiary,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val tertiaryContainer by animateColorAsState(
        targetValue = colorScheme.tertiaryContainer,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val onTertiaryContainer by animateColorAsState(
        targetValue = colorScheme.onTertiaryContainer,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val errorColor by animateColorAsState(
        targetValue = colorScheme.error, animationSpec = tween(durationMillis = 300), label = ""
    )

    val onErrorColor by animateColorAsState(
        targetValue = colorScheme.onError,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val errorContainer by animateColorAsState(
        targetValue = colorScheme.errorContainer,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val onErrorContainer by animateColorAsState(
        targetValue = colorScheme.onErrorContainer,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val backgroundColor by animateColorAsState(
        targetValue = colorScheme.background,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val onBackground by animateColorAsState(
        targetValue = colorScheme.onBackground,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val surfaceColor by animateColorAsState(
        targetValue = colorScheme.surface,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val onSurface by animateColorAsState(
        targetValue = colorScheme.onSurface,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val surfaceVariant by animateColorAsState(
        targetValue = colorScheme.surfaceVariant,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val onSurfaceVariant by animateColorAsState(
        targetValue = colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val inverseSurfaceColor by animateColorAsState(
        targetValue = colorScheme.inverseSurface,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val inverseOnSurfaceColor by animateColorAsState(
        targetValue = colorScheme.inverseOnSurface,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val outline by animateColorAsState(
        targetValue = colorScheme.outline,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val outlineVariant by animateColorAsState(
        targetValue = colorScheme.outlineVariant,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val scrim by animateColorAsState(
        targetValue = colorScheme.scrim,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val surfaceTint by animateColorAsState(
        targetValue = colorScheme.surfaceTint,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    return ColorScheme(
        primary = primaryColor,
        onPrimary = onPrimaryColor,
        primaryContainer = primaryContainerColor,
        onPrimaryContainer = onPrimaryContainerColor,
        inversePrimary = inversePrimaryColor,
        secondary = secondaryColor,
        onSecondary = onSecondaryColor,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiaryColor,
        onTertiary = onTertiaryColor,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        error = errorColor,
        onError = onErrorColor,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        background = backgroundColor,
        onBackground = onBackground,
        surface = surfaceColor,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        inverseSurface = inverseSurfaceColor,
        inverseOnSurface = inverseOnSurfaceColor,
        outline = outline,
        outlineVariant = outlineVariant,
        scrim = scrim,
        surfaceTint = surfaceTint
    )
}
*/
