package co.be4you.indoorlocalization.viewmodel.createasset

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.core.data.network.ws.api.models.assets.CreateAssetRequestDto
import co.be4you.core.data.repositories.AssetRepository
import co.be4you.core.data.repositories.FloorMapRepository
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

            CreateEditAssetAction.OnSaveClicked -> save()

            CreateEditAssetAction.OnDeleteClicked -> {
                _state.update { it?.copy(showDeleteDialog = true) }
            }

            CreateEditAssetAction.OnConfirmDeleteClicked -> deleteAsset()

            CreateEditAssetAction.OnDismissDeleteClicked -> _state.update {
                it?.copy(
                    showDeleteDialog = false
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
                            appNavigator.navigateTo(
                                route = Route.Assets(
                                    floorMapId = currentState.floorMap.id,
                                    floorMapName = currentState.floorMap.name,
                                ),
                                removeRoutes = listOf(
                                    Route.CreateEditAsset(
                                        floorMapId = currentState.floorMap.id,
                                        assetId = currentState.asset.id
                                    ),
                                    Route.AssetDetail(assetId = currentState.asset.id),
                                    Route.Assets(
                                        floorMapId = currentState.floorMap.id,
                                        floorMapName = currentState.floorMap.name,
                                    ),
                                )
                            )
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

    private fun save() {
        _state.value?.let { currentState ->
            if (currentState.isSaving) return

            val fmId = floorMapId
            if (fmId == null) {
                _state.update { it?.copy(errorResource = R.string.generic_error_message) }
                return
            }

            val name = currentState.name.trim()
            if (name.isBlank()) {
                _state.update { it?.copy(errorResource = R.string.generic_error_message) }
                return
            }

            val color = currentState.colorHex.trim().takeIf { it.isNotBlank() }

            val request = CreateAssetRequestDto(
                name = name,
                x = null,
                y = null,
                floorMapId = fmId,
                active = true,
                color = color
            )

            _state.update { it?.copy(isSaving = true, errorResource = null) }

            viewModelScope.launch(Dispatchers.IO) {
                assetRepository.createAsset(request).fold(
                    onSuccess = {
                        _state.update { it?.copy(isSaving = false) }
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
}
