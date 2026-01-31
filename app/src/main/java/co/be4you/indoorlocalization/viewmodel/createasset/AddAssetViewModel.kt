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

class AddAssetViewModel(
    private val floorMapRepository: FloorMapRepository,
    private val assetRepository: AssetRepository,
    private val appNavigator: AppNavigator
) : ViewModel() {

    private val floorMapId: Long? = appNavigator.getRouteOrNull<Route.CreateAsset>()?.floorMapId

    private val _state = MutableStateFlow<AddAssetState?>(null)
    val state = _state.asStateFlow()

    init {
        floorMapId?.let { floorMapId ->
            viewModelScope.launch(Dispatchers.IO) {
                floorMapRepository.getFloorMap(floorMapId).fold(
                    onSuccess = { floorMap ->
                        _state.value = AddAssetState(
                            floorMap = floorMap,
                            isSaving = false,
                            name = "",
                            colorHex = "",
                            errorResource = null,
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

    fun execute(action: AddAssetAction) {
        when (action) {
            is AddAssetAction.OnNameChanged -> _state.update {
                it?.copy(
                    name = action.value,
                    errorResource = null
                )
            }

            is AddAssetAction.OnColorChanged -> _state.update {
                it?.copy(
                    colorHex = action.value,
                    errorResource = null
                )
            }

            AddAssetAction.OnBackClicked -> appNavigator.navigateBack()

            AddAssetAction.OnSaveClicked -> save()
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
