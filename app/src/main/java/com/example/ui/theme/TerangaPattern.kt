package com.example.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

fun Modifier.terangaPattern(alpha: Float = 0.14f): Modifier = drawBehind {
    val tile = 32f
    val stripe = HighDensityIndigo.copy(alpha = alpha)
    val gold = TerangaYellow.copy(alpha = alpha)
    val red = SenegalRed.copy(alpha = alpha)

    drawRect(stripe, topLeft = Offset.Zero, size = Size(size.width, size.height))
    var x = -tile
    while (x < size.width + tile) {
        var y = -tile
        while (y < size.height + tile) {
            val center = Offset(x + tile / 2, y + tile / 2)
            val diamond = Path().apply {
                moveTo(center.x, center.y - tile * 0.43f)
                lineTo(center.x + tile * 0.43f, center.y)
                lineTo(center.x, center.y + tile * 0.43f)
                lineTo(center.x - tile * 0.43f, center.y)
                close()
            }
            drawPath(diamond, gold)
            drawCircle(red, radius = tile * 0.12f, center = center)
            y += tile
        }
        x += tile
    }
}