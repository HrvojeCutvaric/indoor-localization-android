package co.be4you.indoorlocalization.view.createasset

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.core.ui.components.DefaultButton
import co.be4you.core.ui.components.DefaultLabel
import co.be4you.core.ui.components.DefaultTextField
import co.be4you.core.ui.components.LoadingLayout
import co.be4you.core.ui.components.SecondaryButton
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.view.common.DefaultTopBar
import co.be4you.indoorlocalization.viewmodel.createasset.CreateEditAssetAction
import co.be4you.indoorlocalization.viewmodel.createasset.CreateEditAssetState
import co.be4you.indoorlocalization.viewmodel.createasset.CreateEditAssetViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateEditAssetScreen(
    viewModel: CreateEditAssetViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    state?.let {
        CreateEditAssetLayout(state = it, onAction = viewModel::execute)
    } ?: LoadingLayout()
}

@Composable
private fun CreateEditAssetLayout(
    state: CreateEditAssetState,
    onAction: (CreateEditAssetAction) -> Unit,
) {
    val title =
        if (state.asset == null) stringResource(R.string.create_asset) else stringResource(R.string.edit_asset)

    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { onAction(CreateEditAssetAction.OnDismissDeleteClicked) },
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
                    onClick = { onAction(CreateEditAssetAction.OnConfirmDeleteClicked) },
                    enabled = !state.isSaving,
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
                    onClick = { onAction(CreateEditAssetAction.OnDismissDeleteClicked) },
                    enabled = !state.isSaving,
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

    Column(modifier = Modifier.fillMaxSize()) {
        DefaultTopBar(
            title = title,
            onBack = { onAction(CreateEditAssetAction.OnBackClicked) },
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            DefaultLabel(
                text = stringResource(
                    R.string.create_edit_asset_floor_map,
                    state.floorMap.name
                )
            )
            Spacer(Modifier.height(12.dp))

            DefaultTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.name,
                label = R.string.generic_name_label,
                placeholder = R.string.generic_name_placeholder,
                onValueChange = { onAction(CreateEditAssetAction.OnNameChanged(it)) }
            )

            Spacer(Modifier.height(17.dp))

            ColorPickerSection(
                colorHex = state.colorHex,
                onColorHexChanged = { hex ->
                    val normalized = if (hex.startsWith("#")) hex else "#$hex"
                    onAction(CreateEditAssetAction.OnColorChanged(normalized))
                }
            )

            Spacer(Modifier.height(12.dp))

            state.errorResource?.let { resId ->
                Spacer(Modifier.height(12.dp))
                DefaultLabel(
                    text = stringResource(id = resId),
                    isError = true
                )
            }

            Spacer(Modifier.height(16.dp))

            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                label = if (state.asset == null) R.string.create else R.string.generic_save,
                isButtonLoading = state.isSaving,
                onButtonClicked = { onAction(CreateEditAssetAction.OnSaveClicked) }
            )

            if (state.asset != null) {
                Spacer(modifier = Modifier.height(4.dp))

                SecondaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.delete),
                    onButtonClicked = { onAction(CreateEditAssetAction.OnDeleteClicked) },
                    isLoading = state.isSaving,
                    isEnabled = state.isSaving.not(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                    borderStroke = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.error
                    ),
                )
            }
        }
    }
}
