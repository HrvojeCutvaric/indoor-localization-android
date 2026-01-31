package co.be4you.indoorlocalization.view.createasset

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.view.common.DefaultTopBar
import co.be4you.indoorlocalization.viewmodel.createasset.AddAssetAction
import co.be4you.indoorlocalization.viewmodel.createasset.AddAssetState
import co.be4you.indoorlocalization.viewmodel.createasset.AddAssetViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddAssetScreen(
    viewModel: AddAssetViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    state?.let {
        AddAssetLayout(state = it, onAction = viewModel::execute)
    } ?: LoadingLayout()
}

@Composable
private fun AddAssetLayout(
    state: AddAssetState,
    onAction: (AddAssetAction) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        DefaultTopBar(
            title = "Add Asset",
            onBack = { onAction(AddAssetAction.OnBackClicked) },
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            DefaultLabel(text = "Floor map: ${state.floorMap.name}")
            Spacer(Modifier.height(12.dp))

            DefaultTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.name,
                label = R.string.generic_name_label,
                placeholder = R.string.generic_name_placeholder,
                onValueChange = { onAction(AddAssetAction.OnNameChanged(it)) }
            )

            Spacer(Modifier.height(17.dp))

            ColorPickerSection(
                colorHex = state.colorHex,
                onColorHexChanged = { hex ->
                    val normalized = if (hex.startsWith("#")) hex else "#$hex"
                    onAction(AddAssetAction.OnColorChanged(normalized))
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
                label = R.string.generic_save,
                isButtonLoading = state.isSaving,
                onButtonClicked = { onAction(AddAssetAction.OnSaveClicked) }
            )
        }
    }
}
