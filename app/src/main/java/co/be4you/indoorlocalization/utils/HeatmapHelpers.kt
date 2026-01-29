package co.be4you.indoorlocalization.utils

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import co.be4you.core.data.network.ws.api.models.floorMap.FloorMapDto
import co.be4you.core.domain.models.AssetPositionHistory
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

data class HeatPointPx(val x: Float, val y: Float)

fun generateHeatmapBitmapTrailLike(
    pointsPx: List<HeatPointPx>,
    mapW: Int,
    mapH: Int,
    gridMaxSide: Int = 700,      // higher -> sharper trail, more cost
    radiusPx: Int = 16,          // visual radius in MAP pixels
    intensity: Float = 1.0f,     // overall strength
): Bitmap {

    if (pointsPx.isEmpty() || mapW <= 0 || mapH <= 0) {
        return createBitmap(max(mapW, 1), max(mapH, 1))
    }

    // Grid size preserving aspect ratio
    val aspect = mapW.toFloat() / mapH.toFloat()
    val (gridW, gridH) = if (aspect >= 1f) {
        gridMaxSide to max(1, (gridMaxSide / aspect).toInt())
    } else {
        max(1, (gridMaxSide * aspect).toInt()) to gridMaxSide
    }

    val grid = FloatArray(gridW * gridH)

    // Mapping from map px -> grid
    val sx = gridW.toFloat() / mapW.toFloat()
    val sy = gridH.toFloat() / mapH.toFloat()

    // Convert radius from MAP px to GRID px
    val rGX = max(1, (radiusPx * sx).toInt())
    val rGY = max(1, (radiusPx * sy).toInt())

    // Precompute Gaussian kernel (separable-ish but simplest is full 2D kernel)
    // Sigma chosen so most energy within radius.
    val sigmaX = rGX / 3f
    val sigmaY = rGY / 3f
    val twoSigmaX2 = 2f * sigmaX * sigmaX
    val twoSigmaY2 = 2f * sigmaY * sigmaY

    // Stamp gaussian around each point (grid space)
    for (p in pointsPx) {
        val gx = (p.x * sx).toInt()
        val gy = (p.y * sy).toInt()

        val x0 = max(0, gx - rGX)
        val x1 = min(gridW - 1, gx + rGX)
        val y0 = max(0, gy - rGY)
        val y1 = min(gridH - 1, gy + rGY)

        for (y in y0..y1) {
            val dy = (y - gy).toFloat()
            val wy = exp(-(dy * dy) / twoSigmaY2)
            val row = y * gridW
            for (x in x0..x1) {
                val dx = (x - gx).toFloat()
                val wx = exp(-(dx * dx) / twoSigmaX2)
                grid[row + x] += (wx * wy)
            }
        }
    }

    // Normalize & colorize to a grid bitmap, then scale to map size
    return renderHeatmapGridToBitmap(
        density = grid,
        gridW = gridW,
        gridH = gridH,
        mapW = mapW,
        mapH = mapH,
        intensity = intensity
    )
}

private fun renderHeatmapGridToBitmap(
    density: FloatArray,
    gridW: Int,
    gridH: Int,
    mapW: Int,
    mapH: Int,
    intensity: Float,
): Bitmap {
    var maxV = 0f
    for (v in density) maxV = max(maxV, v)
    val gridBmp = createBitmap(gridW, gridH)
    val pixels = IntArray(gridW * gridH)

    if (maxV <= 0f) {
        gridBmp.eraseColor(android.graphics.Color.TRANSPARENT)
        return gridBmp.scale(mapW, mapH)
    }

    fun lerp(a: Int, b: Int, t: Float) = (a + (b - a) * t).toInt()

    fun colorRamp(t: Float): Int {
        val x = t.coerceIn(0f, 1f)
        // blue -> green -> yellow -> red (same as your legend)
        return when {
            x < 0.33f -> {
                val k = x / 0.33f
                val g = lerp(0, 255, k)
                val b = lerp(255, 0, k)
                (0) or (g shl 8) or b
            }

            x < 0.66f -> {
                val k = (x - 0.33f) / 0.33f
                val r = lerp(0, 255, k)
                val g = 255
                val b = 0
                (r shl 16) or (g shl 8) or b
            }

            else -> {
                val k = (x - 0.66f) / 0.34f
                val r = 255
                val g = lerp(255, 0, k)
                val b = 0
                (r shl 16) or (g shl 8) or b
            }
        }
    }

    for (i in density.indices) {
        val v = (density[i] / maxV) * intensity
        // sqrt boosts weaker trails (looks like the web)
        val norm = sqrt(v.coerceIn(0f, 1f))
        if (norm < 0.02f) {
            pixels[i] = 0x00000000
        } else {
            val rgb = colorRamp(norm)
            val a = (norm * 210f).toInt().coerceIn(0, 255)
            pixels[i] = (a shl 24) or rgb
        }
    }

    gridBmp.setPixels(pixels, 0, gridW, 0, 0, gridW, gridH)
    return gridBmp.scale(mapW, mapH)
}

fun historyToHeatPointsPx(
    history: List<AssetPositionHistory>,
    floorMap: FloorMapDto
): List<HeatPointPx> {
    val mapW = floorMap.imageWidthPx ?: return emptyList()
    val mapH = floorMap.imageHeightPx ?: return emptyList()

    return history.map { h ->
        val pxX = (h.x / floorMap.widthInMeters) * mapW
        val pxY = (h.y / floorMap.heightInMeters) * mapH
        HeatPointPx(pxX.toFloat(), pxY.toFloat())
    }
}


