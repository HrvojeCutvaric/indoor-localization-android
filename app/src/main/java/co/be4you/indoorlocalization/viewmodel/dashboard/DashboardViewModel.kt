package co.be4you.indoorlocalization.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.core.data.repositories.FloorMapRepository
import co.be4you.core.navigation.AppNavigator
import co.be4you.core.navigation.Route
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class DashboardViewModel(
    private val floorMapRepository: FloorMapRepository,
    private val appNavigator: AppNavigator,
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

            is DashboardAction.OnNavigateToAssets -> {
                appNavigator.navigateTo(
                    Route.Assets(
                        floorMapId = action.floorMapId,
                        floorMapName = action.floorMapName
                    )
                )
            }
        }
    }
}
