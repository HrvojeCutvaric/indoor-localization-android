package co.be4you.indoorlocalization.viewmodel.assetdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.core.data.repositories.AssetRepository
import co.be4you.core.navigation.AppNavigator
import co.be4you.indoorlocalization.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import co.be4you.core.navigation.Route
class AssetDetailViewModel(
    private val repository: AssetRepository,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    private var assetId: Long? = null

    private val _state = MutableStateFlow(AssetDetailState())
    val state = _state.asStateFlow()

    fun execute(action: AssetDetailAction) {
        when (action) {
            AssetDetailAction.OnBackClicked -> {
                appNavigator.navigateBack()
            }

            AssetDetailAction.OnLogoutClicked -> {
                appNavigator.navigateTo(route = Route.Login, clearBackStack = true)
            }
        }
    }

    fun setAssetId(id: Long) {
        if (assetId == id) return
        assetId = id
        loadAsset(id)
    }

    private fun loadAsset(id: Long) {
        _state.update { it.copy(isLoading = true, errorResource = null) }

        viewModelScope.launch(Dispatchers.IO) {
            repository.getAsset(id).fold(
                onSuccess = { asset ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            asset = asset
                        )
                    }
                },
                onFailure = {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorResource = R.string.generic_error_message
                        )
                    }
                }
            )
        }
    }
}
