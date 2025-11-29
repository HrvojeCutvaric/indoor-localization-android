package co.be4you.indoorlocalization.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.indoorlocalization.data.apis.test.FLOOR_MAP_ID
import co.be4you.indoorlocalization.data.repositories.FloorMapRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val floorMapRepository: FloorMapRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<DashboardState?>(null)
    val state: StateFlow<DashboardState?> = _state

    init {
        viewModelScope.launch(Dispatchers.IO) {
            floorMapRepository.getFloorMap(FLOOR_MAP_ID).fold(
                onSuccess = { floorMap ->
                    _state.value = DashboardState(
                        floorMap = floorMap,
                    )
                },
                onFailure = {
                    _state.value = DashboardState(
                        floorMap = null,
                    )
                }
            )
        }
    }
}
