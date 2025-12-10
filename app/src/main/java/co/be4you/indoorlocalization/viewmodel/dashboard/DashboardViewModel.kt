package co.be4you.indoorlocalization.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.core.data.repositories.AssetTrackingRepository
import co.be4you.core.data.repositories.FloorMapRepository
import co.be4you.core.navigation.AppNavigator
import co.be4you.core.navigation.Route
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class DashboardViewModel(
    private val floorMapRepository: FloorMapRepository,
    private val appNavigator: AppNavigator,
    private val assetTrackingRepository: AssetTrackingRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<DashboardState?>(null)
    val state = _state.asStateFlow()
    private val _event = MutableSharedFlow<MainAction>()
    val event = _event.asSharedFlow()

    private var assetsJob: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            floorMapRepository.getFloorMaps().fold(
                onSuccess = { floorMaps ->
                    floorMaps.firstOrNull()?.let { firstFloorMap ->
                        val firstFloorMap = floorMaps.firstOrNull()

                        _state.value = DashboardState(
                            floorMaps = floorMaps,
                            selectedFloorMap = firstFloorMap,
                            isDropdownExpanded = false,
                            floorMapAssets = emptyList(),
                        )

                        firstFloorMap?.let { observeAssets(it.id) }
                    } ?: run {
                        _state.value = DashboardState(
                            floorMaps = floorMaps,
                            selectedFloorMap = null,
                            isDropdownExpanded = false,
                            floorMapAssets = emptyList(),
                        )
                    }
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

            is DashboardAction.OnFloorMapSelected -> viewModelScope.launch(Dispatchers.IO) {
                _state.update {
                    it?.copy(
                        selectedFloorMap = action.floorMap,
                        isDropdownExpanded = false,
                    )
                }

                observeAssets(action.floorMap.id)
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

    private fun observeAssets(floorMapId: Long) {
        assetsJob?.cancel()

        assetsJob = viewModelScope.launch(Dispatchers.IO) {
            assetTrackingRepository.assetsPosition(floorMapId)
                .collectLatest { floorMapAssets ->
                    _state.update {
                        it?.copy(
                            floorMapAssets = floorMapAssets
                        )
                    }
                }
        }
    }
}
