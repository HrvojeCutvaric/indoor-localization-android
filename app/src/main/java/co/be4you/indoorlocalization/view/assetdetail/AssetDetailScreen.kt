package co.be4you.indoorlocalization.view.assetdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.core.ui.components.DefaultButton
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.utils.formatDateTime
import co.be4you.indoorlocalization.view.common.DefaultTopBar
import co.be4you.indoorlocalization.viewmodel.assetdetail.AssetDetailAction
import co.be4you.indoorlocalization.viewmodel.assetdetail.AssetDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AssetDetailScreen(
    viewModel: AssetDetailViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_this_asset)) },
            text = { Text(stringResource(R.string.this_action_cannot_be_undone)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.execute(AssetDetailAction.OnDeleteClicked)
                    },
                    enabled = !state.isDeleting
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false },
                    enabled = !state.isDeleting
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DefaultTopBar(
                title = stringResource(R.string.asset_details),
                onBack = { viewModel.execute(AssetDetailAction.OnBackClicked) },
            )
        }
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
                    val asset = state.asset

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(
                                        color = asset.color,
                                        shape = MaterialTheme.shapes.medium
                                    )
                            )

                            Spacer(Modifier.width(15.dp))

                            Text(
                                text = asset.name,
                                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(Modifier.height(15.dp))

                        InfoItem(
                            stringResource(R.string.status),
                            if (asset.active) stringResource(R.string.active) else stringResource(
                                R.string.inactive
                            )
                        )
                        InfoItem(
                            stringResource(R.string.last_known_position),
                            "(${asset.x}, ${asset.y})"
                        )
                        InfoItem(stringResource(R.string.floor_map), asset.floorMapId.toString())
                        InfoItem(
                            stringResource(R.string.last_sync),
                            asset.lastSync?.formatDateTime() ?: stringResource(R.string.unknown)
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

@Composable
fun InfoItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 10.dp)) {

        Text(
            text = label,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Light)
        )
        Spacer(Modifier.height(12.dp))
    }
}
