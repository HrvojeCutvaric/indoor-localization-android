package co.be4you.indoorlocalization.viewmodel.assets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.core.data.repositories.AssetRepository
import co.be4you.core.domain.models.Asset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.http.Query

class AssetsViewModel(
    private val repository: AssetRepository
): ViewModel(){

    private val _state = MutableStateFlow(
        AssetsState(
            isLoading = false,
            searchQuery = "",
            assets = emptyList(),
            errorMessage = null
        )
    )
    val state = _state.asStateFlow()

    fun loadAssets(floorMapId: Long){

        _state.value =_state.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch{
            repository.getAssetsByFloorMap(floorMapId).fold(
                onSuccess = { assets ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        assets = assets,
                        filteredAssets = applyFilter(assets, _state.value.searchQuery)
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

    private fun applyFilter(assets: List<Asset>, query: String): List<Asset>{
        if(query.isBlank()) return assets
        return assets.filter { it.name.contains(query, ignoreCase = true) }
    }

    fun updateSearchQuery(query: String){
        _state.value = _state.value.copy(
            searchQuery = query,
            filteredAssets = applyFilter(_state.value.assets, query)
        )
    }
}