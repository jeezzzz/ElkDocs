package com.jeezzzz.elkdocs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeezzzz.elkdocs.ui.theme.ElkDocsTheme
import kotlinx.coroutines.delay
import kotlin.math.sin
import kotlin.math.PI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElkDocsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    BraidAnimationScreen()
                }
            }
        }
    }
}

@Composable
fun BraidAnimationScreen() {
    var phase1Left by remember { mutableStateOf(0f) }
    var phase2Left by remember { mutableStateOf(0f) }
    var phase3Left by remember { mutableStateOf(0f) }
    var phase1Right by remember { mutableStateOf(0f) }
    var phase2Right by remember { mutableStateOf(0f) }
    var phase3Right by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            phase1Left += 0.03f
            phase2Left += 0.04f
            phase3Left += 0.02f
            phase1Right += 0.04f
            phase2Right += 0.03f
            phase3Right += 0.02f
            delay(20)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .width(20.dp)
                .padding(start = 5.dp)
                .align(Alignment.CenterStart)
                .graphicsLayer { rotationZ = 180f }
        ) {
            BraidCanvas(phase1Left, phase2Left, phase3Left, true)
        }

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .width(20.dp)
                .padding(end = 5.dp)
                .align(Alignment.CenterEnd)
                .graphicsLayer { rotationZ = 180f }
        ) {
            BraidCanvas(phase1Right, phase2Right, phase3Right, false)
        }

        Text(
            text = "Ajeesh Rawal\nElkDocs Task",
            color = Color.White,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun BraidCanvas(phase1: Float, phase2: Float, phase3: Float, isLeft: Boolean) {
    val density = LocalDensity.current.density

    Canvas(modifier = Modifier.fillMaxHeight().width(100.dp)) {
        val braidWidth = 2.dp.toPx()
        val amplitude = 5.dp.toPx()
        val wavelength = 500.dp.toPx()
        val braidSpacing = 5.dp.toPx()  // Spacing between braids

        fun drawBraid(offsetX: Float, color: Color, phase: Float) {
            val path = Path().apply {
                if (isLeft) {
                    moveTo(offsetX, -amplitude)  // Start above the visible area
                    for (y in -amplitude.toInt()..size.height.toInt()) {
                        val x = amplitude * sin((y / wavelength + phase) * 2 * PI).toFloat() + offsetX
                        lineTo(x, y.toFloat())
                    }
                    // Draw to beyond the bottom of the canvas and extend to the left edge
                    lineTo(size.width, size.height + amplitude)
                    lineTo(size.width, -amplitude)
                    lineTo(offsetX, -amplitude)
                } else {
                    moveTo(size.width - offsetX, -amplitude)  // Start above the visible area
                    for (y in -amplitude.toInt()..size.height.toInt()) {
                        val x = size.width - (amplitude * sin((y / wavelength + phase) * 2 * PI).toFloat() + offsetX)
                        lineTo(x, y.toFloat())
                    }
                    // Draw to beyond the bottom of the canvas and extend to the right edge
                    lineTo(-braidSpacing, size.height + amplitude)
                    lineTo(-braidSpacing, -amplitude)
                    lineTo(size.width - offsetX, -amplitude)
                }
                close()  // Ensure the path is properly closed
            }

            drawPath(
                path = path,
                color = color,
                style = Stroke(width = braidWidth)
            )
        }

        // Draw three independent braids
        drawBraid(0f, Color(0xff7f00ff), phase1)
        drawBraid(braidSpacing, Color(0xffe91e63), phase2)
        drawBraid(2 * braidSpacing, Color(0xff008080), phase3)
    }
}
