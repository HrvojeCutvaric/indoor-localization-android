package co.be4you.indoorlocalization.viewmodel.assets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AssetsViewModel: ViewModel() {
    private val _state = MutableStateFlow(AssetsState())
    val state: StateFlow<AssetsState> = _state

    init {
        loadAssets()
    }

    private fun loadAssets() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)

            val dummy = listOf(
                AssetUi(1, "Forklift 1", true, "Warehouse 1", 0xFF8BC6FF), // baby blue
                AssetUi(2, "Box 1", true, "Warehouse 1", 0xFFE6A8FF),       // light purple
                AssetUi(3, "Device A", true, "Warehouse 1", 0xFFFFD27F),   // soft yellow
            )

            _state.value = AssetsState(
                assets = dummy,
                isLoading = false
            )
        }
    }

    fun updateSearchQuery(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
    }
}
