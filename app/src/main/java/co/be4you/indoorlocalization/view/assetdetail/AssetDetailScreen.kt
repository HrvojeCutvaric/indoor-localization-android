package co.be4you.indoorlocalization.view.assetdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.core.ui.components.DefaultButton
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

    AssetDetailLayout(state = state, onAction = viewModel::execute)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetDetailLayout(
    state: AssetDetailState,
    onAction: (AssetDetailAction) -> Unit,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = stringResource(R.string.delete_this_asset),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.this_action_cannot_be_undone),
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onAction(AssetDetailAction.OnDeleteClicked)
                    },
                    enabled = !state.isDeleting,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.delete).uppercase(),
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false },
                    enabled = !state.isDeleting,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.cancel).uppercase(),
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DefaultTopBar(
                title = stringResource(R.string.asset_details),
                onBack = { onAction(AssetDetailAction.OnBackClicked) },
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

                        Spacer(Modifier.height(32.dp))

                        DefaultButton(
                            modifier = Modifier.fillMaxWidth(),
                            buttonColors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError,
                            ),
                            onButtonClicked = { showDeleteDialog = true },
                            isButtonEnabled = state.isDeleting.not(),
                            isButtonLoading = state.isDeleting,
                            content = { if (!state.isDeleting) Text("Delete") }
                        )
                    }
                }
            }
        }
    }
}
