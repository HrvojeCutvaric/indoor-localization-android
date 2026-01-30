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

            AssetDetailAction.OnDeleteClicked -> {
                val id = assetId ?: return
                deleteAsset(id)
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

    private fun deleteAsset(id: Long) {

        if (_state.value.isDeleting) return

        _state.update { it.copy(isDeleting = true, errorResource = null) }

        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAsset(id).fold(
                onSuccess = {
                    _state.update { it.copy(isDeleting = false) }
                    appNavigator.navigateBack()
                },
                onFailure = {
                    _state.update {
                        it.copy(
                            isDeleting = false,
                            errorResource = R.string.error_deleting_asset
                        )
                    }
                }
            )
        }
    }
}
