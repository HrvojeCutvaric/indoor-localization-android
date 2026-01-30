package co.be4you.indoorlocalization.view.heatmap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.be4you.core.ui.theme.CommonBlue
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.view.components.IndoorLocalizationPinchToZoomView
import co.be4you.indoorlocalization.viewmodel.heatmap.HeatmapAction
import co.be4you.indoorlocalization.viewmodel.heatmap.HeatmapState

@Composable
fun HeatmapReportLayout(
    state: HeatmapState,
    onAction: (HeatmapAction) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.floorMap == null -> {
                Text(
                    text = stringResource(R.string.please_select_floor_map),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            state.errorRes != null -> {
                Text(
                    text = stringResource(state.errorRes),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> {
                IndoorLocalizationPinchToZoomView(
                    modifier = Modifier.fillMaxSize(),
                    floorMap = state.floorMap,
                    zones = if (state.showZones) state.zones else emptyList(),
                    assets = emptyList(),
                    heatmapBitmap = state.heatmapBitmap,
                )
            }
        }

        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            onClick = { onAction(HeatmapAction.OnBackToFiltersClicked) },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = CommonBlue,
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = stringResource(R.string.back)
            )
        }
    }
}
