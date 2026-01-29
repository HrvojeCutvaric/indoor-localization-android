package co.be4you.indoorlocalization.view.heatmap

import androidx.compose.runtime.Composable
import co.be4you.indoorlocalization.utils.HeatmapScreenMode
import co.be4you.indoorlocalization.viewmodel.heatmap.HeatmapAction
import co.be4you.indoorlocalization.viewmodel.heatmap.HeatmapState

@Composable
fun HeatmapScreen(
    state: HeatmapState,
    onAction: (HeatmapAction) -> Unit
) {
    when (state.mode) {
        HeatmapScreenMode.FILTERS -> HeatmapFiltersScreen(state, onAction)
        HeatmapScreenMode.REPORT -> {
            // TODO: implement
        }
    }
}
