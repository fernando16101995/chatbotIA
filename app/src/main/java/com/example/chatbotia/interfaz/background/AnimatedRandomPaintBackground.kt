package com.example.chatbotia.interfaz.background

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.chatbotia.interfaz.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class PaintBlob(
    val center: Offset,
    val maxRadius: Float,
    val animation: Animatable<Float, *>
)

@Composable
fun AnimatedRandomPaintBackground(
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val scope = rememberCoroutineScope()
    val blobs = remember { mutableStateListOf<PaintBlob>() }
    val blackStroke = Color.Black

    // Generar blobs continuamente
    LaunchedEffect(Unit) {
        while (true) {
            val anim = Animatable(0f)
            val blob = PaintBlob(
                center = Offset(Random.nextFloat(), Random.nextFloat()),
                maxRadius = Random.nextFloat() * 500f + 200f,
                animation = anim
            )
            blobs.add(blob)

            scope.launch {
                anim.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 2000)
                )
            }

            delay(800)
            if (blobs.size > 8) blobs.removeAt(0)
        }
    }

    Canvas(modifier = modifier) {
        // 1. Fondo (usando el del tema)
        drawRect(color = BgDark)

        // 2. Dibujar cada mancha
        blobs.forEach { blob ->
            val radius = blob.maxRadius * blob.animation.value
            val centerOffset = Offset(
                x = size.width * blob.center.x,
                y = size.height * blob.center.y
            )

            // Mancha (usando color de acento del tema)
            drawCircle(
                color = AccentViolet.copy(alpha = 0.4f),
                radius = radius,
                center = centerOffset
            )

            // Borde sutil
            drawCircle(
                color = BorderSubtle,
                radius = radius,
                center = centerOffset,
                style = Stroke(width = 2f)
            )
        }
    }
}
