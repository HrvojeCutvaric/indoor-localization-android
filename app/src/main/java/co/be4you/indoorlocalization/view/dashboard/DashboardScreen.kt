package co.be4you.indoorlocalization.view.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.core.domain.models.Asset
import co.be4you.core.domain.models.FloorMap
import co.be4you.core.domain.models.Zone
import co.be4you.core.domain.models.ZonePoint
import co.be4you.core.ui.components.DefaultButton
import co.be4you.core.ui.components.DefaultDropdownSelector
import co.be4you.core.ui.theme.IndoorLocalizationTheme
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.view.common.DefaultTopBar
import co.be4you.indoorlocalization.viewmodel.dashboard.DashboardAction
import co.be4you.indoorlocalization.viewmodel.dashboard.DashboardState
import co.be4you.indoorlocalization.viewmodel.dashboard.DashboardViewModel
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import coil3.compose.AsyncImage
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = koinViewModel(),
    onAction: (MainAction) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest(onAction)
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            DefaultTopBar(
                title = "Dashboard",
                onBack = null,
                onLogout = {
                    viewModel.execute(DashboardAction.OnLogoutClicked)
                }
            )
        }
    ) { paddingValues ->
        state?.let { currentState ->
            DashboardLayout(
                state = currentState,
                onAction = viewModel::execute,
                modifier = Modifier.padding(paddingValues)
            )
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun DashboardLayout(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        Spacer(modifier = Modifier.height(2.dp))

        DefaultDropdownSelector(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            selectedValue = state.selectedFloorMap?.name.orEmpty(),
            placeholder = R.string.select_floor_map,
            isExpanded = state.isDropdownExpanded,
            onExpandedChange = { onAction(DashboardAction.OnDropdownExpandedChanged) },
            onDismissRequest = { onAction(DashboardAction.OnDismissRequest) },
        ) {
            state.floorMaps.forEach { floorMap ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = floorMap.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                            ),
                        )
                    },
                    onClick = {
                        onAction(DashboardAction.OnFloorMapSelected(floorMap = floorMap))
                    },
                )
            }
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            if (state.isFloorMapLoading.not()) {
                state.selectedFloorMap?.let { floorMap ->
                    PinchToZoomView(
                        modifier = Modifier.fillMaxSize(),
                        floorMap = floorMap,
                        assets = state.floorMapAssets,
                        zones = state.floorMapZones,
                    )
                } ?: run {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.please_select_floor_map),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    )
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }

        DefaultButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            label = R.string.assets,
            isButtonEnabled = state.selectedFloorMap != null,
            isButtonLoading = false,
            onButtonClicked = {
                state.selectedFloorMap?.let { map ->
                    onAction(
                        DashboardAction.OnNavigateToAssets(
                            floorMapId = map.id,
                            floorMapName = map.name
                        )
                    )
                }
            }
        )
    }
}

@Composable
fun PinchToZoomView(
    modifier: Modifier = Modifier,
    floorMap: FloorMap,
    assets: List<Asset>,
    zones: List<Zone>
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
                modifier = Modifier
                    .onGloballyPositioned { coords ->
                        imageSize = coords.size
                    },
                model = floorMap.imageUrl,
                contentDescription = floorMap.name,
                contentScale = ContentScale.None,
            )

            DrawZonesOverlay(
                modifier = Modifier
                    .size(
                        width = with(LocalDensity.current) { imageSize.width.toDp() },
                        height = with(LocalDensity.current) { imageSize.height.toDp() }
                    )
                    .clip(RectangleShape),
                zones = zones,
            )

            DrawAssetsOverlay(
                modifier = Modifier
                    .size(
                        width = with(LocalDensity.current) { imageSize.width.toDp() },
                        height = with(LocalDensity.current) { imageSize.height.toDp() }
                    )
                    .clip(RectangleShape),
                assets = assets,
                floorMap = floorMap
            )
        }
    }
}

@Composable
private fun DrawAssetsOverlay(
    modifier: Modifier = Modifier,
    assets: List<Asset>,
    floorMap: FloorMap,
) {
    val density = LocalDensity.current
    val fontSize = 8.sp
    val labelMaxWidth = 56.dp
    val dotSize = 4.dp
    val spacing = 4.dp

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
                Text(
                    modifier = Modifier
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

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    IndoorLocalizationTheme {
        DashboardLayout(
            state = DashboardState(
                floorMaps = listOf(
                    FloorMap(
                        id = 1,
                        name = "Test-1",
                        imageUrl = "https://picsum.photos/id/1/5000/3333",
                        imageWidthPx = 0,
                        imageHeightPx = 0,
                        widthInMeters = 0.0,
                        heightInMeters = 0.0,
                    )
                ),
                selectedFloorMap = FloorMap(
                    id = 1,
                    name = "Test-1",
                    imageUrl = "https://picsum.photos/id/1/5000/3333",
                    imageWidthPx = 0,
                    imageHeightPx = 0,
                    widthInMeters = 0.0,
                    heightInMeters = 0.0,
                ),
                isDropdownExpanded = true,
                isFloorMapLoading = false,
                floorMapAssets = emptyList(),
                floorMapZones = listOf(
                    Zone(
                        id = 1L,
                        floorMapId = 1L,
                        name = "Entrance Area",
                        points = listOf(
                            ZonePoint(x = 50.0, y = 550.0, ordinalNumber = 1),
                            ZonePoint(x = 300.0, y = 550.0, ordinalNumber = 2),
                            ZonePoint(x = 300.0, y = 650.0, ordinalNumber = 3),
                            ZonePoint(x = 50.0, y = 650.0, ordinalNumber = 4)
                        )
                    ),
                    Zone(
                        id = 2L,
                        floorMapId = 1L,
                        name = "Main Hall",
                        points = listOf(
                            ZonePoint(x = 200.0, y = 150.0, ordinalNumber = 1),
                            ZonePoint(x = 550.0, y = 150.0, ordinalNumber = 2),
                            ZonePoint(x = 600.0, y = 400.0, ordinalNumber = 3),
                            ZonePoint(x = 250.0, y = 420.0, ordinalNumber = 4)
                        )
                    ),
                    Zone(
                        id = 3L,
                        floorMapId = 1L,
                        name = "Office Zone",
                        points = listOf(
                            ZonePoint(x = 50.0, y = 50.0, ordinalNumber = 1),
                            ZonePoint(x = 250.0, y = 50.0, ordinalNumber = 2),
                            ZonePoint(x = 250.0, y = 200.0, ordinalNumber = 3),
                            ZonePoint(x = 50.0, y = 200.0, ordinalNumber = 4)
                        )
                    )
                ),
            ),
            onAction = { },
        )
    }
}
