package com.pixeldev.composetv.utlis

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Brush

@Composable
fun TVGradientLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp, // Bigger size for TV
    strokeWidth: Dp = 6.dp
) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val gradientBrush = Brush.sweepGradient(
        colors = listOf(
            Color(0xFF9C27FF), // Purple
            Color(0xFF2196F3), // Blue
            Color(0xFF9C27FF), // Loop back to purple for smoother rotation
        )
    )

    Canvas(
        modifier = modifier
            .size(size)
    ) {
        rotate(angle) {
            drawArc(
                brush = gradientBrush,
                startAngle = 0f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }
    }
}
