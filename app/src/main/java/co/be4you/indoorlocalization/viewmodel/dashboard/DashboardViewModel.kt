package co.be4you.indoorlocalization.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.core.data.repositories.FloorMapRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val floorMapRepository: FloorMapRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<DashboardState?>(null)
    val state: StateFlow<DashboardState?> = _state

    init {
        viewModelScope.launch(Dispatchers.IO) {
            floorMapRepository.getFloorMaps().fold(
                onSuccess = { floorMaps ->
                    _state.value = DashboardState(
                        floorMaps = floorMaps,
                        selectedFloorMap = null,
                        isDropdownExpanded = false,
                    )
                },
                onFailure = {
                    _state.value = null
                }
            )
        }
    }

    fun execute(action: DashboardAction) {
        when (action) {
            DashboardAction.OnDismissRequest -> {
                _state.update {
                    it?.copy(
                        isDropdownExpanded = false,
                    )
                }
            }

            DashboardAction.OnDropdownExpandedChanged -> {
                _state.update {
                    it?.copy(
                        isDropdownExpanded = _state.value?.isDropdownExpanded?.not() ?: false,
                    )
                }
            }

            is DashboardAction.OnFloorMapSelected -> {
                _state.update {
                    it?.copy(
                        selectedFloorMap = action.floorMap,
                        isDropdownExpanded = false,
                    )
                }
            }

            is DashboardAction.OnNavigateToAssets -> {
            }
        }
    }
}
