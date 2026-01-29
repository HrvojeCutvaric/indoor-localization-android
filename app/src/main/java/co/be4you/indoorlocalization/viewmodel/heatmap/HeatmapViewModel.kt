package co.be4you.indoorlocalization.viewmodel.heatmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.core.data.repositories.AssetRepository
import co.be4you.core.data.repositories.FloorMapRepository
import co.be4you.core.navigation.AppNavigator
import co.be4you.core.navigation.Route
import co.be4you.indoorlocalization.utils.HeatmapScreenMode
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalTime::class)
class HeatmapViewModel(
    private val appNavigator: AppNavigator,
    private val floorMapRepository: FloorMapRepository,
    private val assetRepository: AssetRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<HeatmapState?>(null)
    val state: StateFlow<HeatmapState?> = _state
    private val _event = MutableSharedFlow<MainAction>()
    val event = _event.asSharedFlow()

    private val floorMapId: Long = (appNavigator.backStack.last() as Route.Heatmap).floorMapId

    init {
        viewModelScope.launch(Dispatchers.IO) {
            floorMapRepository.getFloorMap(floorMapId).fold(
                onSuccess = { floorMap ->
                    assetRepository.getAssetsByFloorMap(floorMap.id).fold(
                        onSuccess = { assets ->
                            _state.value = HeatmapState(
                                mode = HeatmapScreenMode.FILTERS,
                                floorMap = floorMap,
                                assets = assets,
                                selectedAssets = emptyList(),
                                showZones = false,
                                fromDateTime = null,
                                toDateTime = null,
                                errorRes = null,
                                zones = emptyList(),
                                activeDateTimeOption = null,
                                unselectedAssets = assets,
                                preselectedAssets = emptyList(),
                            )
                        },
                        onFailure = {
                            _state.value = null
                        }
                    )
                },
                onFailure = {
                    _state.value = null
                }
            )
        }
    }

    fun execute(action: HeatmapAction) {
        when (action) {
            HeatmapAction.OnCloseDateTimePicker -> {
                _state.update { it?.copy(activeDateTimeOption = null) }
            }

            is HeatmapAction.OnDateSelected -> action.date?.let { selectedMillis ->
                _state.value?.let { currentState ->
                    val systemTimeZone = TimeZone.currentSystemDefault()

                    val selectedDateTime = Instant
                        .fromEpochMilliseconds(selectedMillis)
                        .toLocalDateTime(systemTimeZone)

                    val stateMillis = when (currentState.activeDateTimeOption) {
                        HeatmapState.Companion.DateTimeOption.FROM_DATE,
                        HeatmapState.Companion.DateTimeOption.FROM_TIME -> currentState.fromDateTime

                        HeatmapState.Companion.DateTimeOption.TO_DATE,
                        HeatmapState.Companion.DateTimeOption.TO_TIME -> currentState.toDateTime

                        null -> Clock.System.now().toEpochMilliseconds()
                    } ?: Clock.System.now().toEpochMilliseconds()

                    val stateDateTime = Instant
                        .fromEpochMilliseconds(stateMillis)
                        .toLocalDateTime(systemTimeZone)


                    val updatedDateTime = LocalDateTime(
                        year = selectedDateTime.year,
                        month = selectedDateTime.month,
                        dayOfMonth = selectedDateTime.dayOfMonth,
                        hour = stateDateTime.hour,
                        minute = stateDateTime.minute
                    )

                    val updatedMillis = updatedDateTime
                        .toInstant(systemTimeZone)
                        .toEpochMilliseconds()

                    _state.update {
                        when (it?.activeDateTimeOption) {
                            HeatmapState.Companion.DateTimeOption.FROM_DATE -> {
                                it.copy(
                                    fromDateTime = updatedMillis,
                                    activeDateTimeOption = null,
                                )
                            }

                            HeatmapState.Companion.DateTimeOption.TO_DATE -> {
                                it.copy(
                                    toDateTime = updatedMillis,
                                    activeDateTimeOption = null,
                                )
                            }

                            else -> it
                        }
                    }
                }
            }

            is HeatmapAction.OnDateTimeFieldClicked -> {
                _state.update { it?.copy(activeDateTimeOption = action.option) }
            }

            is HeatmapAction.OnTimeConfirm -> _state.value?.let { currentState ->
                val systemTimeZone = TimeZone.currentSystemDefault()

                val stateMillis = when (currentState.activeDateTimeOption) {
                    HeatmapState.Companion.DateTimeOption.FROM_DATE, HeatmapState.Companion.DateTimeOption.FROM_TIME -> currentState.fromDateTime
                    HeatmapState.Companion.DateTimeOption.TO_DATE, HeatmapState.Companion.DateTimeOption.TO_TIME -> currentState.toDateTime
                    null -> Clock.System.now().toEpochMilliseconds()
                } ?: Clock.System.now().toEpochMilliseconds()

                val stateDateTime = Instant
                    .fromEpochMilliseconds(stateMillis)
                    .toLocalDateTime(systemTimeZone)

                val updatedDateTime = LocalDateTime(
                    year = stateDateTime.year,
                    month = stateDateTime.month,
                    dayOfMonth = stateDateTime.dayOfMonth,
                    hour = action.hour,
                    minute = action.minute
                )

                val updatedMillis = updatedDateTime
                    .toInstant(systemTimeZone)
                    .toEpochMilliseconds()

                _state.update {
                    when (it?.activeDateTimeOption) {
                        HeatmapState.Companion.DateTimeOption.FROM_TIME -> it.copy(
                            fromDateTime = updatedMillis,
                            activeDateTimeOption = null,
                        )

                        HeatmapState.Companion.DateTimeOption.TO_TIME -> it.copy(
                            toDateTime = updatedMillis,
                            activeDateTimeOption = null,
                        )

                        else -> it
                    }
                }
            }

            HeatmapAction.OnBackToDashboardClicked -> {
                appNavigator.navigateBack()
            }

            HeatmapAction.OnGenerateClicked -> {
                // TODO: implement on generate clicked
            }

            is HeatmapAction.OnRemoveAssetClicked -> {
                _state.update {
                    it?.copy(
                        selectedAssets = it.selectedAssets - action.asset,
                        unselectedAssets = it.unselectedAssets + action.asset,
                    )
                }
            }

            HeatmapAction.OnAddAssetClicked -> {
                _state.update { it?.copy(mode = HeatmapScreenMode.SELECT_ASSETS) }
            }

            is HeatmapAction.OnAssetSelected -> {
                _state.update { it?.copy(preselectedAssets = it.preselectedAssets + action.asset) }
            }

            HeatmapAction.OnSaveSelectedAssetsClicked -> {
                if (_state.value?.preselectedAssets.isNullOrEmpty()) {
                    _state.update { it?.copy(mode = HeatmapScreenMode.FILTERS) }
                } else {
                    _state.update {
                        it?.copy(
                            unselectedAssets = it.unselectedAssets - it.preselectedAssets.toSet(),
                            selectedAssets = it.selectedAssets + it.preselectedAssets.toSet(),
                            preselectedAssets = emptyList(),
                            mode = HeatmapScreenMode.FILTERS,
                        )
                    }
                }
            }
        }
    }
}
