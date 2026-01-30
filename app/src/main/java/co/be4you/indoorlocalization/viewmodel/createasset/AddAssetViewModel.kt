package co.be4you.indoorlocalization.viewmodel.createasset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.core.data.network.ws.api.models.assets.CreateAssetRequestDto
import co.be4you.core.data.repositories.AssetRepository
import co.be4you.core.navigation.AppNavigator
import co.be4you.core.navigation.Route
import co.be4you.indoorlocalization.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddAssetViewModel(
    private val repository: AssetRepository,
    private val appNavigator: AppNavigator
) : ViewModel(){

    private var floorMapId: Long? = null

    private val _state = MutableStateFlow(AddAssetState())
    val state = _state.asStateFlow()

    fun setFloorMapId(id: Long){
        floorMapId = id
    }

    fun execute(action: AddAssetAction) {
        when (action) {
            is AddAssetAction.OnNameChanged -> _state.update { it.copy(name = action.value, errorResource = null) }
            is AddAssetAction.OnXChanged -> _state.update { it.copy(x = action.value, errorResource = null) }
            is AddAssetAction.OnYChanged -> _state.update { it.copy(y = action.value, errorResource = null) }
            is AddAssetAction.OnColorChanged -> _state.update { it.copy(color = action.value, errorResource = null) }
            is AddAssetAction.OnActiveChanged -> _state.update { it.copy(active = action.value, errorResource = null) }

            AddAssetAction.OnBackClicked -> appNavigator.navigateBack()

            AddAssetAction.OnLogoutClicked ->
                appNavigator.navigateTo(route = Route.Login, clearBackStack = true)

            AddAssetAction.OnSaveClicked -> save()
        }
    }

    private fun save() {
        if (_state.value.isSaving) return

        val fmId = floorMapId
        if (fmId == null) {
            _state.update { it.copy(errorResource = R.string.generic_error_message) }
            return
        }

        val name = _state.value.name.trim()
        if (name.isBlank()) {
            _state.update { it.copy(errorResource = R.string.generic_error_message) }
            return
        }

        val color = _state.value.color.trim().takeIf { it.isNotBlank() }

        val request = CreateAssetRequestDto(
            name = name,
            x = null,
            y = null,
            floorMapId = fmId,
            active = _state.value.active,
            color = color
        )

        _state.update { it.copy(isSaving = true, errorResource = null) }

        viewModelScope.launch(Dispatchers.IO) {
            repository.createAsset(request).fold(
                onSuccess = {
                    _state.update { it.copy(isSaving = false) }
                    appNavigator.navigateBack()
                },
                onFailure = {
                    _state.update {
                        it.copy(
                            isSaving = false,
                            errorResource = R.string.generic_error_message
                        )
                    }
                }
            )
        }
    }
}