package co.be4you.indoorlocalization.view.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.core.domain.models.Asset
import co.be4you.core.domain.models.FloorMap
import co.be4you.core.ui.components.DefaultButton
import co.be4you.core.ui.components.DefaultDropdownSelector
import co.be4you.core.ui.theme.IndoorLocalizationTheme
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.viewmodel.dashboard.DashboardAction
import co.be4you.indoorlocalization.viewmodel.dashboard.DashboardState
import co.be4you.indoorlocalization.viewmodel.dashboard.DashboardViewModel
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import coil3.compose.AsyncImage
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

    state?.let { currentState ->
        DashboardLayout(
            state = currentState,
            onAction = viewModel::execute
        )
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun DashboardLayout(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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
            state.selectedFloorMap?.let { floorMap ->
                PinchToZoomView(
                    modifier = Modifier.fillMaxSize(),
                    floorMap = floorMap,
                    assets = state.floorMapAssets,
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
    modifier: Modifier,
    floorMap: FloorMap,
    assets: List<Asset>,
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
            .fillMaxSize()
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
        // Apply zoom + pan to the whole map (image + assets)
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offsetX
                    translationY = offsetY
                },
            contentAlignment = Alignment.Center
        ) {
            // FLOOR MAP
            AsyncImage(
                modifier = Modifier.fillMaxSize(0.95f),
                model = floorMap.imageUrl,
                contentDescription = floorMap.name,
                contentScale = ContentScale.FillWidth,
            )

            // ASSET MARKERS ON TOP
            DrawAssetsOverlay(
                assets = assets,
                floorMap = floorMap
            )
        }
    }
}

@Composable
private fun DrawAssetsOverlay(
    assets: List<Asset>,
    floorMap: FloorMap,
) {
    Canvas(modifier = Modifier) {

        val imageWidth = floorMap.imageWidthPx.toFloat()
        val imageHeight = floorMap.imageHeightPx.toFloat()

        assets.forEach { asset ->
            val x = asset.x ?: return@forEach
            val y = asset.y ?: return@forEach

            val pxX = ((x / floorMap.widthInMeters) * imageWidth).toFloat()
            val pxY = ((y / floorMap.heightInMeters) * imageHeight).toFloat()

            drawCircle(
                color = Color.Red,
                radius = 12f,
                center = Offset(pxX, pxY)
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
                floorMapAssets = emptyList(),
            ),
            onAction = { },
        )
    }
}
