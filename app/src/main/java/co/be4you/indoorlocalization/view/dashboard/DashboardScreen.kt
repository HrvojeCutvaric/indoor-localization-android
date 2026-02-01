package co.be4you.indoorlocalization.view.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.core.domain.models.FloorMap
import co.be4you.core.domain.models.Zone
import co.be4you.core.domain.models.ZonePoint
import co.be4you.core.ui.components.DefaultButton
import co.be4you.core.ui.components.DefaultDropdownSelector
import co.be4you.core.ui.theme.IndoorLocalizationTheme
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.view.common.DefaultTopBar
import co.be4you.indoorlocalization.view.components.IndoorLocalizationPinchToZoomView
import co.be4you.indoorlocalization.viewmodel.dashboard.DashboardAction
import co.be4you.indoorlocalization.viewmodel.dashboard.DashboardState
import co.be4you.indoorlocalization.viewmodel.dashboard.DashboardViewModel
import co.be4you.indoorlocalization.viewmodel.main.MainAction
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
                    IndoorLocalizationPinchToZoomView(
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DefaultButton(
                modifier = Modifier.weight(1f),
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

            DefaultButton(
                modifier = Modifier.weight(1f),
                label = R.string.heatmap,
                isButtonEnabled = state.selectedFloorMap != null,
                isButtonLoading = false,
                onButtonClicked = { onAction(DashboardAction.OnHeatmapClicked) }
            )

            DefaultButton(
                modifier = Modifier.weight(1f),
                label = R.string.zone_retention,
                isButtonEnabled = state.selectedFloorMap != null,
                isButtonLoading = false,
                onButtonClicked = { onAction(DashboardAction.OnZoneRetentionClicked) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
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
