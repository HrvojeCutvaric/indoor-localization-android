package co.be4you.indoorlocalization.viewmodel.createasset

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.core.data.repositories.AssetRepository
import co.be4you.core.data.repositories.FloorMapRepository
import co.be4you.core.domain.models.Asset
import co.be4you.core.navigation.AppNavigator
import co.be4you.core.navigation.Route
import co.be4you.indoorlocalization.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateEditAssetViewModel(
    private val floorMapRepository: FloorMapRepository,
    private val assetRepository: AssetRepository,
    private val appNavigator: AppNavigator
) : ViewModel() {

    private val route = appNavigator.getRouteOrNull<Route.CreateEditAsset>()
    private val floorMapId: Long? = route?.floorMapId
    private val assetId: Long? = route?.assetId

    private val _state = MutableStateFlow<CreateEditAssetState?>(null)
    val state = _state.asStateFlow()

    init {
        setState()
    }

    fun execute(action: CreateEditAssetAction) {
        when (action) {
            is CreateEditAssetAction.OnNameChanged -> _state.update {
                it?.copy(
                    name = action.value,
                    errorResource = null
                )
            }

            is CreateEditAssetAction.OnColorChanged -> _state.update {
                it?.copy(
                    colorHex = action.value,
                    errorResource = null
                )
            }

            CreateEditAssetAction.OnBackClicked -> appNavigator.navigateBack()

            CreateEditAssetAction.OnConfirmClicked -> onConfirmClicked()

            CreateEditAssetAction.OnDeleteClicked -> {
                _state.update { it?.copy(showDeleteDialog = true) }
            }

            CreateEditAssetAction.OnConfirmDeleteClicked -> deleteAsset()

            CreateEditAssetAction.OnDismissDeleteClicked -> _state.update {
                it?.copy(
                    showDeleteDialog = false
                )
            }

            CreateEditAssetAction.OnColorPickerClicked -> {
                _state.update { it?.copy(showColorPicker = true) }
            }

            CreateEditAssetAction.OnColorPickerDismissed -> {
                _state.update { it?.copy(showColorPicker = false) }
            }

            is CreateEditAssetAction.OnColorPickerConfirmed -> {
                _state.update { it?.copy(showColorPicker = false, selectedColor = action.color) }
            }
        }
    }

    private fun setState() {
        floorMapId?.let { floorMapId ->
            viewModelScope.launch(Dispatchers.IO) {
                floorMapRepository.getFloorMap(floorMapId).fold(
                    onSuccess = { floorMap ->
                        if (assetId == null) {
                            _state.value = CreateEditAssetState(
                                floorMap = floorMap,
                                asset = null,
                                isSaving = false,
                                name = "",
                                colorHex = "",
                                errorResource = null,
                                showDeleteDialog = false,
                                showColorPicker = false,
                                selectedColor = Color.White,
                            )
                            return@launch
                        }

                        assetRepository.getAsset(assetId).fold(
                            onSuccess = { asset ->
                                _state.value = CreateEditAssetState(
                                    floorMap = floorMap,
                                    asset = asset,
                                    isSaving = false,
                                    name = asset.name,
                                    colorHex = asset.colorHex.orEmpty(),
                                    errorResource = null,
                                    showDeleteDialog = false,
                                    showColorPicker = false,
                                    selectedColor = asset.color,
                                )
                            },
                            onFailure = {
                                Log.e("AddAssetViewModel", "Error getting asset", it)
                                appNavigator.navigateBack()
                            },
                        )
                    },
                    onFailure = {
                        Log.e("AddAssetViewModel", "Error getting floor map", it)
                        appNavigator.navigateBack()
                    }
                )
            }
        }
    }

    private fun onConfirmClicked() {
        _state.value?.let { currentState ->
            _state.update { it?.copy(isSaving = true) }

            if (currentState.name.isBlank()) {
                _state.update { it?.copy(errorResource = R.string.asset_error_name_required) }
                return
            }

            val newAsset = Asset(
                id = assetId ?: 0,
                name = currentState.name,
                colorHex = String.format("#%08X", currentState.selectedColor.toArgb()),
                x = null,
                y = null,
                floorMapId = currentState.floorMap.id,
                active = true,
                lastSync = null,
            )

            _state.update { it?.copy(isSaving = true, errorResource = null) }

            viewModelScope.launch(Dispatchers.IO) {
                val call = if (assetId == null) assetRepository.createAsset(asset = newAsset)
                else assetRepository.updateAsset(asset = newAsset)

                call.fold(
                    onSuccess = {
                        appNavigator.navigateBack()
                    },
                    onFailure = {
                        _state.update {
                            it?.copy(
                                isSaving = false,
                                errorResource = R.string.generic_error_message
                            )
                        }
                    }
                )
            }
        }
    }

    private fun deleteAsset() {
        _state.value?.let { currentState ->
            currentState.asset?.let { asset ->
                _state.update { it?.copy(isSaving = true, errorResource = null) }

                viewModelScope.launch(Dispatchers.IO) {
                    assetRepository.deleteAsset(asset.id).fold(
                        onSuccess = {
                            appNavigator.navigateBack()
                        },
                        onFailure = {
                            _state.update {
                                it?.copy(
                                    isSaving = false,
                                    errorResource = R.string.error_deleting_asset
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
