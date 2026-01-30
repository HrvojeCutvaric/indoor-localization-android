package co.be4you.indoorlocalization.view.heatmap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.utils.HeatmapScreenMode
import co.be4you.indoorlocalization.view.common.DefaultTopBar
import co.be4you.indoorlocalization.viewmodel.heatmap.HeatmapAction
import co.be4you.indoorlocalization.viewmodel.heatmap.HeatmapState
import co.be4you.indoorlocalization.viewmodel.heatmap.HeatmapViewModel
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import org.koin.androidx.compose.koinViewModel

@Composable
fun HeatmapScreen(
    viewModel: HeatmapViewModel = koinViewModel(),
    onAction: (MainAction) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.event.collect(onAction) }

    state?.let { currentState ->
        HeatmapLayout(
            state = currentState,
            onAction = viewModel::execute,
        )
    } ?: Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }

}

@Composable
fun HeatmapLayout(
    state: HeatmapState,
    onAction: (HeatmapAction) -> Unit
) {
    Scaffold(
        topBar = {
            DefaultTopBar(
                title = stringResource(R.string.heatmap),
                onBack = { onAction(HeatmapAction.OnBackClicked) },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when (state.mode) {
                HeatmapScreenMode.FILTERS -> HeatmapFiltersLayout(state, onAction)
                HeatmapScreenMode.REPORT -> HeatmapReportLayout(state, onAction)
                HeatmapScreenMode.SELECT_ASSETS -> HeatmapSelectAssetsLayout(state, onAction)
            }
        }
    }
}
