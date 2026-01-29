package co.be4you.indoorlocalization.view.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import co.be4you.core.domain.models.Asset
import co.be4you.core.domain.models.FloorMap
import co.be4you.core.domain.models.Zone
import coil3.compose.AsyncImage
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun IndoorLocalizationPinchToZoomView(
    modifier: Modifier = Modifier,
    floorMap: FloorMap,
    assets: List<Asset>,
    zones: List<Zone>,
    heatmapBitmap: ImageBitmap? = null,
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    val minScale = 1f
    val maxScale = 4f

    var initialOffset by remember { mutableStateOf(Offset(0f, 0f)) }
    val slowMovement = 0.5f

    Box(
        modifier = modifier
            .clip(RectangleShape)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    val newScale = (scale * zoom).coerceIn(minScale, maxScale)

                    val centerX = size.width / 2
                    val centerY = size.height / 2

                    val offsetXChange = (centerX - offsetX) * (newScale / scale - 1)
                    val offsetYChange = (centerY - offsetY) * (newScale / scale - 1)

                    val maxOffsetX = (size.width / 2) * (newScale - 1)
                    val minOffsetX = -maxOffsetX
                    val maxOffsetY = (size.height / 2) * (newScale - 1)
                    val minOffsetY = -maxOffsetY

                    offsetX = (offsetX + pan.x * scale * slowMovement + offsetXChange)
                        .coerceIn(minOffsetX, maxOffsetX)
                    offsetY = (offsetY + pan.y * scale * slowMovement + offsetYChange)
                        .coerceIn(minOffsetY, maxOffsetY)

                    scale = newScale

                    if (pan != Offset.Zero && initialOffset == Offset.Zero) {
                        initialOffset = Offset(offsetX, offsetY)
                    }
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (scale != 1f) {
                            scale = 1f
                            offsetX = initialOffset.x
                            offsetY = initialOffset.y
                        } else {
                            scale = 2f
                        }
                    }
                )
            }
    ) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offsetX
                    translationY = offsetY
                },
            contentAlignment = Alignment.Center
        ) {
            var imageSize by remember { mutableStateOf(IntSize.Zero) }

            AsyncImage(
                modifier = Modifier.onGloballyPositioned { coords -> imageSize = coords.size },
                model = floorMap.imageUrl,
                contentDescription = floorMap.name,
                contentScale = ContentScale.None,
            )

            if (zones.isNotEmpty()) {
                DrawZonesOverlay(
                    modifier = Modifier
                        .size(
                            width = with(LocalDensity.current) { imageSize.width.toDp() },
                            height = with(LocalDensity.current) { imageSize.height.toDp() }
                        )
                        .clip(RectangleShape),
                    zones = zones,
                )
            }

            if (assets.isNotEmpty()) {
                DrawAssetsOverlay(
                    modifier = Modifier
                        .size(
                            width = with(LocalDensity.current) { imageSize.width.toDp() },
                            height = with(LocalDensity.current) { imageSize.height.toDp() }
                        )
                        .clip(RectangleShape),
                    assets = assets,
                    floorMap = floorMap,
                    scale = scale,
                )
            }

            if (heatmapBitmap != null) {
                Image(
                    bitmap = heatmapBitmap,
                    contentDescription = null,
                    modifier = Modifier
                        .size(
                            width = with(LocalDensity.current) { imageSize.width.toDp() },
                            height = with(LocalDensity.current) { imageSize.height.toDp() },
                        )
                        .alpha(0.65f)
                )
            }
        }
    }
}

@Composable
private fun DrawAssetsOverlay(
    modifier: Modifier = Modifier,
    assets: List<Asset>,
    floorMap: FloorMap,
    scale: Float,
) {
    val density = LocalDensity.current
    val fontSize = 8.sp
    val labelMaxWidth = 56.dp
    val dotSize = 4.dp
    val spacing = 4.dp
    val startFadingAt = 2.6f
    val fullyHiddenAt = 3.6f

    val t = ((scale - startFadingAt) / (fullyHiddenAt - startFadingAt))
        .coerceIn(0f, 1f)

    val targetAlpha = 1f - t
    val alpha by animateFloatAsState(targetAlpha, label = "labelAlpha")

    Box(modifier = modifier.fillMaxSize()) {
        assets.forEach { asset ->
            val x = asset.x ?: return@forEach
            val y = asset.y ?: return@forEach

            val pxX = ((x / floorMap.widthInMeters) * floorMap.imageWidthPx).toFloat()
            val pxY = ((y / floorMap.heightInMeters) * floorMap.imageHeightPx).toFloat()

            val color = asset.colorHex?.let { hex ->
                runCatching { Color(hex.toColorInt()) }.getOrElse { Color.Gray }
            } ?: Color.Gray

            val labelSizePx = remember(asset.id) { mutableStateOf(IntSize.Zero) }

            Box(
                modifier = Modifier.offset {
                    IntOffset(pxX.roundToInt(), pxY.roundToInt())
                }
            ) {
                if (alpha > 0.02f) {
                    Text(
                        modifier = Modifier
                            .alpha(alpha)
                            .onGloballyPositioned { coords -> labelSizePx.value = coords.size }
                            .offset {
                                val w = labelSizePx.value.width
                                val h = labelSizePx.value.height
                                val spacingPx = with(density) { spacing.roundToPx() }

                                IntOffset(
                                    x = -w / 2,
                                    y = -(h + spacingPx)
                                )
                            }
                            .widthIn(max = labelMaxWidth)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        text = asset.name,
                        style = TextStyle(
                            fontSize = fontSize,
                            color = MaterialTheme.colorScheme.onPrimary,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = false,
                    )
                }

                Box(
                    modifier = Modifier
                        .offset {
                            val r = with(density) { (dotSize / 2).roundToPx() }
                            IntOffset(-r, -r)
                        }
                        .size(dotSize)
                        .background(color, CircleShape)
                )
            }
        }
    }
}

@Composable
private fun DrawZonesOverlay(
    modifier: Modifier = Modifier,
    zones: List<Zone>,
) {
    Canvas(modifier = modifier) {

        zones.forEachIndexed { index, zone ->
            if (zone.points.size < 3) return@forEachIndexed

            val path = Path()

            zone.points
                .sortedBy { it.ordinalNumber }
                .forEachIndexed { index, point ->

                    val x = point.x.toFloat()
                    val y = point.y.toFloat()

                    if (index == 0) {
                        path.moveTo(x, y)
                    } else {
                        path.lineTo(x, y)
                    }
                }

            path.close()

            val random = Random(index)

            val zoneColor = Color(
                red = random.nextInt(256),
                green = random.nextInt(256),
                blue = random.nextInt(256),
                alpha = 255
            )

            drawPath(
                path = path,
                color = zoneColor.copy(alpha = 0.33f),
            )

            drawPath(
                path = path,
                color = zoneColor,
                style = Stroke(width = 3f)
            )
        }
    }
}
