package co.be4you.indoorlocalization.view.assetdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.core.ui.components.HandleLifecycleEvents
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.utils.formatDateTime
import co.be4you.indoorlocalization.view.assetdetail.components.AssetHeaderCard
import co.be4you.indoorlocalization.view.assetdetail.components.AssetInfoCard
import co.be4you.indoorlocalization.view.common.DefaultTopBar
import co.be4you.indoorlocalization.viewmodel.assetdetail.AssetDetailAction
import co.be4you.indoorlocalization.viewmodel.assetdetail.AssetDetailState
import co.be4you.indoorlocalization.viewmodel.assetdetail.AssetDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AssetDetailScreen(
    viewModel: AssetDetailViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    HandleLifecycleEvents(onResume = viewModel::onResume)

    AssetDetailLayout(state = state, onAction = viewModel::execute)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetDetailLayout(
    state: AssetDetailState,
    onAction: (AssetDetailAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DefaultTopBar(
                title = stringResource(R.string.asset_details),
                onBack = { onAction(AssetDetailAction.OnBackClicked) },
                actions = {
                    if (state.asset != null) {
                        IconButton(
                            onClick = { onAction(AssetDetailAction.OnEditClicked) },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                            )
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_edit),
                                contentDescription = null,
                            )
                        }
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }

                state.errorResource != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = state.errorResource),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }

                state.asset != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState()),
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        AssetHeaderCard(
                            name = state.asset.name,
                            isActive = state.asset.active,
                            assetColor = state.asset.color,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        AssetInfoCard(
                            lastPosition = "(${state.asset.x}, ${state.asset.y})",
                            floorMapId = "${state.asset.floorMapId}",
                            lastSync = state.asset.lastSync?.formatDateTime()
                                ?: stringResource(R.string.unknown),
                        )
                    }
                }
            }
        }
    }
}
