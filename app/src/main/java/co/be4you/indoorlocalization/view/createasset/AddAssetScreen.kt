package co.be4you.indoorlocalization.view.createasset

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.core.ui.components.DefaultButton
import co.be4you.core.ui.components.DefaultLabel
import co.be4you.core.ui.components.DefaultTextField
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.view.common.DefaultTopBar
import co.be4you.indoorlocalization.viewmodel.createasset.AddAssetAction
import co.be4you.indoorlocalization.viewmodel.createasset.AddAssetViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.res.stringResource

@Composable
fun AddAssetScreen(
    floorMapId: Long,
    floorMapName: String,
    viewModel: AddAssetViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(floorMapId) {
        viewModel.setFloorMapId(floorMapId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DefaultTopBar(
            title = "Add Asset",
            onBack = { viewModel.execute(AddAssetAction.OnBackClicked) },
            onLogout = { viewModel.execute(AddAssetAction.OnLogoutClicked) }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            DefaultLabel(text = "Floor map: $floorMapName")
            Spacer(Modifier.height(12.dp))

            DefaultTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.name,
                label = R.string.generic_name_label,
                placeholder = R.string.generic_name_placeholder,
                onValueChange = { viewModel.execute(AddAssetAction.OnNameChanged(it)) }
            )

            Spacer(Modifier.height(12.dp))

            DefaultTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.x,
                label = R.string.generic_x_label,
                placeholder = R.string.generic_x_placeholder,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.execute(AddAssetAction.OnXChanged(it)) }
            )

            Spacer(Modifier.height(12.dp))

            DefaultTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.y,
                label = R.string.generic_y_label,
                placeholder = R.string.generic_y_placeholder,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.execute(AddAssetAction.OnYChanged(it)) }
            )

            Spacer(Modifier.height(12.dp))

            DefaultTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.color,
                label = R.string.generic_color_label,
                placeholder = R.string.generic_color_placeholder,
                onValueChange = { viewModel.execute(AddAssetAction.OnColorChanged(it)) }
            )

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
                onButtonClicked = { viewModel.execute(AddAssetAction.OnSaveClicked) }
            )
        }
    }
}
