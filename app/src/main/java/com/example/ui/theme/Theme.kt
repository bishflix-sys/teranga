package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.unit.dp

private val DarkColorScheme =
  darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurface = OnSurfaceDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark
  )

private val LightColorScheme =
  lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurface = OnSurfaceLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight
  )

@Composable
fun terangaOutlinedTextFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
  focusedContainerColor = SurfaceLight,
  unfocusedContainerColor = SurfaceLight,
  disabledContainerColor = HighDensitySlate100,
  errorContainerColor = HighDensityAlertBg,
  focusedTextColor = OnSurfaceLight,
  unfocusedTextColor = OnSurfaceLight,
  disabledTextColor = OnSurfaceVariantLight,
  errorTextColor = HighDensityAlertText,
  focusedLabelColor = PrimaryLight,
  unfocusedLabelColor = OnSurfaceVariantLight,
  disabledLabelColor = OnSurfaceVariantLight,
  errorLabelColor = HighDensityAlertText,
  focusedLeadingIconColor = PrimaryLight,
  unfocusedLeadingIconColor = OnSurfaceVariantLight,
  disabledLeadingIconColor = OnSurfaceVariantLight,
  errorLeadingIconColor = HighDensityAlertText,
  focusedTrailingIconColor = PrimaryLight,
  unfocusedTrailingIconColor = OnSurfaceVariantLight,
  disabledTrailingIconColor = OnSurfaceVariantLight,
  errorTrailingIconColor = HighDensityAlertText,
  focusedBorderColor = PrimaryLight,
  unfocusedBorderColor = OutlineLight,
  disabledBorderColor = OutlineVariantLight,
  errorBorderColor = HighDensityAlertRed,
  cursorColor = PrimaryLight,
  errorCursorColor = HighDensityAlertRed
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Set to false to preserve the high density design theme consistently
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    shapes = Shapes(
      extraSmall = RoundedCornerShape(10.dp),
      small = RoundedCornerShape(14.dp),
      medium = RoundedCornerShape(18.dp),
      large = RoundedCornerShape(24.dp),
      extraLarge = RoundedCornerShape(28.dp)
    ),
    content = content
  )
}
