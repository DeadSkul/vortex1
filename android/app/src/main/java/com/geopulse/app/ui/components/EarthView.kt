package com.geopulse.app.ui.components

import android.animation.ValueAnimator
import android.view.SurfaceView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun EarthView(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(260.dp)
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            SurfaceView(ctx).apply {
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
            }
        }
    )
}
