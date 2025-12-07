package co.be4you.indoorlocalization.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.core.data.repositories.FloorMapRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.asStateFlow
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import co.be4you.indoorlocalization.navigation.Route
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


class DashboardViewModel(
    private val floorMapRepository: FloorMapRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<DashboardState?>(null)
    val state = _state.asStateFlow()
    private val _event = MutableSharedFlow<MainAction>()
    val event = _event.asSharedFlow()

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

            is DashboardAction.OnNavigateToAssets -> viewModelScope.launch(Dispatchers.IO){
                _event.emit(
                    MainAction.NavigateTo(
                        route = Route.Assets(
                            floorMapId = action.floorMapId,
                            floorMapName = action.floorMapName
                        )
                    )
                )
            }
        }
    }
}
