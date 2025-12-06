package co.be4you.indoorlocalization.viewmodel.assets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.core.data.repositories.AssetRepository
import co.be4you.core.domain.models.Asset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AssetsViewModel(
    private val repository: AssetRepository
): ViewModel(){

    private val _state = MutableStateFlow(AssetsState())
    val state: StateFlow<AssetsState> = _state

    fun loadAssets(floorMapId: Long){

        _state.value =_state.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch{
            repository.getAssetsByFloorMap(floorMapId).fold(
                onSuccess = { assets ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        assets = assets
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Unknown error"
                    )
                }
            )
        }
    }


    fun updateSearchQuery(query: String){
        _state.value = _state.value.copy(searchQuery = query)
    }

    val filteredAssets: List<Asset>
        get() {
            val s = _state.value
            if (s.searchQuery.isBlank()) return s.assets
            return s.assets.filter { it.name.contains(s.searchQuery, ignoreCase = true) }
        }

}