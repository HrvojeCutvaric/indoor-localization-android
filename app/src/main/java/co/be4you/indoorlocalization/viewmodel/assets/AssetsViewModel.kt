package co.be4you.indoorlocalization.viewmodel.assets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.core.data.repositories.AssetRepository
import co.be4you.core.domain.models.Asset
import co.be4you.indoorlocalization.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
            filteredAssets = emptyList(),
            errorResource = null
        )
    )
    val state = _state.asStateFlow()

    fun setFloorMapId(id: Long) {
        loadAssets(id)
    }

    fun execute(action: AssetAction) {
        when(action) {
            is AssetAction.OnSearchChanged -> updateSearchQuery(action.query)
        }
    }
    private fun loadAssets(floorMapId: Long){

        _state.update { it.copy(isLoading = true, errorResource = null) }

        viewModelScope.launch{
            repository.getAssetsByFloorMap(floorMapId).fold(
                onSuccess = { assets ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            assets = assets,
                            filteredAssets = applyFilter(assets, it.searchQuery)
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

    private fun updateSearchQuery(query: String) {
        _state.update {
            it.copy(
                searchQuery = query,
                filteredAssets = applyFilter(it.assets, query)
            )
        }
    }
    private fun applyFilter(assets: List<Asset>, query: String): List<Asset>{
        if(query.isBlank()) return assets
        return assets.filter { it.name.contains(query, ignoreCase = true) }
    }

}