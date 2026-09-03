package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
private val AppFontFamily = FontFamily.SansSerif

val Typography = Typography().run {
  copy(
    displayLarge = displayLarge.copy(fontFamily = AppFontFamily),
    displayMedium = displayMedium.copy(fontFamily = AppFontFamily),
    displaySmall = displaySmall.copy(fontFamily = AppFontFamily),
    headlineLarge = headlineLarge.copy(fontFamily = AppFontFamily),
    headlineMedium = headlineMedium.copy(fontFamily = AppFontFamily),
    headlineSmall = headlineSmall.copy(fontFamily = AppFontFamily),
    titleLarge = titleLarge.copy(fontFamily = AppFontFamily),
    titleMedium = titleMedium.copy(fontFamily = AppFontFamily),
    titleSmall = titleSmall.copy(fontFamily = AppFontFamily),
    bodyLarge = bodyLarge.copy(fontFamily = AppFontFamily, letterSpacing = 0.sp),
    bodyMedium = bodyMedium.copy(fontFamily = AppFontFamily),
    bodySmall = bodySmall.copy(fontFamily = AppFontFamily),
    labelLarge = labelLarge.copy(fontFamily = AppFontFamily),
    labelMedium = labelMedium.copy(fontFamily = AppFontFamily),
    labelSmall = labelSmall.copy(fontFamily = AppFontFamily),
  )
}
