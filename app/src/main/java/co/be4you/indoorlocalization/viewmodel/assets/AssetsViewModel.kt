package co.be4you.indoorlocalization.viewmodel.assets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AssetsViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        AssetsState(
            assets = emptyList(),
            searchQuery = "",
            isLoading = true
        )
    )
    val state: StateFlow<AssetsState> = _state

    fun updateSearchQuery(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
    }

    val filteredAssets: List<AssetUi>
        get() {
            val s = _state.value
            if (s.searchQuery.isBlank()) return s.assets
            return s.assets.filter {
                it.name.contains(s.searchQuery, ignoreCase = true)
            }
        }
}
