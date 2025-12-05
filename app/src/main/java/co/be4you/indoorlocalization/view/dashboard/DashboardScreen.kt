package co.be4you.indoorlocalization.view.dashboard

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.core.domain.models.FloorMap
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.navigation.Route
import co.be4you.indoorlocalization.ui.theme.IndoorLocalizationTheme
import co.be4you.indoorlocalization.view.common.DefaultButton
import co.be4you.indoorlocalization.viewmodel.dashboard.DashboardState
import co.be4you.indoorlocalization.viewmodel.dashboard.DashboardViewModel
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import coil3.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreen(
    onAction: (MainAction) -> Unit,
    viewModel: DashboardViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    state?.let { currentState ->
        DashboardLayout(
            state = currentState,
            onAction = onAction
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
    onAction: (MainAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            state.floorMap?.let { floorMap ->
                PinchToZoomView(
                    modifier = Modifier.fillMaxSize(),
                    floorMap = floorMap,
                )
            } ?: run {
                Text(
                    text = stringResource(R.string.failed_to_load_floor_map),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error,
                    ),
                )
            }
        }
        DefaultButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            label = R.string.assets,
            isButtonEnabled = true,
            isButtonLoading = false,
            onButtonClicked = {
                onAction(
                    MainAction.NavigateTo(
                        Route.Assets(
                            floorMapId = state.floorMap?.id?.toString() ?: "0",
                            floorMapName = state.floorMap?.name ?: ""
                        )
                    )
                )
            }
        )

    }
}

@Composable
private fun PinchToZoomView(
    modifier: Modifier,
    floorMap: FloorMap,
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
                    val newScale = scale * zoom
                    scale = newScale.coerceIn(minScale, maxScale)

                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val offsetXChange = (centerX - offsetX) * (newScale / scale - 1)
                    val offsetYChange = (centerY - offsetY) * (newScale / scale - 1)

                    val maxOffsetX = (size.width / 2) * (scale - 1)
                    val minOffsetX = -maxOffsetX
                    val maxOffsetY = (size.height / 2) * (scale - 1)
                    val minOffsetY = -maxOffsetY

                    if (scale * zoom <= maxScale) {
                        offsetX = (offsetX + pan.x * scale * slowMovement + offsetXChange)
                            .coerceIn(minOffsetX, maxOffsetX)
                        offsetY = (offsetY + pan.y * scale * slowMovement + offsetYChange)
                            .coerceIn(minOffsetY, maxOffsetY)
                    }

                    if (pan != Offset(0f, 0f) && initialOffset == Offset(0f, 0f)) {
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
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offsetX
                translationY = offsetY
            },
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(0.95f),
            model = floorMap.imageUrl,
            contentDescription = floorMap.name,
            contentScale = ContentScale.FillWidth,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    IndoorLocalizationTheme {
        DashboardLayout(
            state = DashboardState(floorMap = null),
            onAction = {}
        )
    }
}
