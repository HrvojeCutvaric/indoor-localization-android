package co.be4you.indoorlocalization.viewmodel.zoneretention

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.core.data.repositories.AssetRepository
import co.be4you.core.data.repositories.ZoneRepository
import co.be4you.core.data.repositories.ZoneRetentionHistoryRepository
import co.be4you.core.navigation.AppNavigator
import co.be4you.core.navigation.Route
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.utils.ZoneRetentionScreenMode
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


@OptIn(ExperimentalTime::class)
class ZoneRetentionViewModel(
    private val appNavigator: AppNavigator,
    private val assetRepository: AssetRepository,
    private val zoneRepository: ZoneRepository,
    private val zoneRetentionHistoryRepository: ZoneRetentionHistoryRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<ZoneRetentionState?>(null)
    val state: StateFlow<ZoneRetentionState?> = _state

    private val floorMapId: Long =
        (appNavigator.backStack.last() as Route.ZoneRetention).floorMapId

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val assetsResult = assetRepository.getAssetsByFloorMap(floorMapId)
            val zonesResult = zoneRepository.getZones(floorMapId)

            assetsResult.fold(
                onSuccess = { assets ->
                    zonesResult.fold(
                        onSuccess = { zones ->
                            _state.value = ZoneRetentionState(
                                assets = assets,
                                zones = zones,
                            )
                        },
                        onFailure = {
                            _state.value = ZoneRetentionState(
                                assets = assets,
                                zones = emptyList(),
                                errorRes = R.string.generic_error_message,
                            )
                        }
                    )
                },
                onFailure = {
                    _state.value = ZoneRetentionState(
                        assets = emptyList(),
                        zones = emptyList(),
                        errorRes = R.string.generic_error_message,
                    )
                }
            )
        }
    }

    fun execute(action: ZoneRetentionAction) {
        when (action) {
            ZoneRetentionAction.OnBackClicked -> appNavigator.navigateBack()

            ZoneRetentionAction.OnAssetDropdownExpandedChanged ->
                _state.update { it?.copy(isAssetDropdownExpanded = it.isAssetDropdownExpanded.not()) }

            ZoneRetentionAction.OnZoneDropdownExpandedChanged ->
                _state.update { it?.copy(isZoneDropdownExpanded = it.isZoneDropdownExpanded.not()) }

            ZoneRetentionAction.OnDismissAssetDropdown ->
                _state.update { it?.copy(isAssetDropdownExpanded = false) }

            ZoneRetentionAction.OnDismissZoneDropdown ->
                _state.update { it?.copy(isZoneDropdownExpanded = false) }

            ZoneRetentionAction.OnBackToFiltersClicked -> {
                _state.update { it?.copy(mode = ZoneRetentionScreenMode.FILTERS) }
            }

            is ZoneRetentionAction.OnAssetSelected ->
                _state.update {
                    it?.copy(
                        selectedAsset = action.asset,
                        isAssetDropdownExpanded = false,
                    )
                }

            is ZoneRetentionAction.OnZoneSelected ->
                _state.update {
                    it?.copy(
                        selectedZone = action.zone,
                        isZoneDropdownExpanded = false,
                    )
                }

            is ZoneRetentionAction.OnDateTimeFieldClicked ->
                _state.update { it?.copy(activeDateTimeOption = action.option) }

            ZoneRetentionAction.OnCloseDateTimePicker ->
                _state.update { it?.copy(activeDateTimeOption = null) }

            is ZoneRetentionAction.OnDateSelected -> action.date?.let { selectedMillis ->
                _state.value?.let { currentState ->
                    val tz = TimeZone.currentSystemDefault()

                    val selectedDate = Instant
                        .fromEpochMilliseconds(selectedMillis)
                        .toLocalDateTime(tz)

                    val baseMillis = when (currentState.activeDateTimeOption) {
                        ZoneRetentionState.Companion.DateTimeOption.FROM_DATE,
                        ZoneRetentionState.Companion.DateTimeOption.FROM_TIME -> currentState.fromDateTime

                        ZoneRetentionState.Companion.DateTimeOption.TO_DATE,
                        ZoneRetentionState.Companion.DateTimeOption.TO_TIME -> currentState.toDateTime

                        null -> Clock.System.now().toEpochMilliseconds()
                    } ?: Clock.System.now().toEpochMilliseconds()

                    val baseDateTime = Instant
                        .fromEpochMilliseconds(baseMillis)
                        .toLocalDateTime(tz)

                    val updated = LocalDateTime(
                        year = selectedDate.year,
                        month = selectedDate.month,
                        dayOfMonth = selectedDate.dayOfMonth,
                        hour = baseDateTime.hour,
                        minute = baseDateTime.minute,
                    ).toInstant(tz).toEpochMilliseconds()

                    _state.update {
                        when (it?.activeDateTimeOption) {
                            ZoneRetentionState.Companion.DateTimeOption.FROM_DATE ->
                                it.copy(fromDateTime = updated, activeDateTimeOption = null)

                            ZoneRetentionState.Companion.DateTimeOption.TO_DATE ->
                                it.copy(toDateTime = updated, activeDateTimeOption = null)

                            else -> it
                        }
                    }
                }
            }

            is ZoneRetentionAction.OnTimeConfirm -> _state.value?.let { currentState ->
                val tz = TimeZone.currentSystemDefault()

                val baseMillis = when (currentState.activeDateTimeOption) {
                    ZoneRetentionState.Companion.DateTimeOption.FROM_DATE,
                    ZoneRetentionState.Companion.DateTimeOption.FROM_TIME -> currentState.fromDateTime

                    ZoneRetentionState.Companion.DateTimeOption.TO_DATE,
                    ZoneRetentionState.Companion.DateTimeOption.TO_TIME -> currentState.toDateTime

                    null -> Clock.System.now().toEpochMilliseconds()
                } ?: Clock.System.now().toEpochMilliseconds()

                val baseDateTime = Instant
                    .fromEpochMilliseconds(baseMillis)
                    .toLocalDateTime(tz)

                val updated = LocalDateTime(
                    year = baseDateTime.year,
                    month = baseDateTime.month,
                    dayOfMonth = baseDateTime.dayOfMonth,
                    hour = action.hour,
                    minute = action.minute,
                ).toInstant(tz).toEpochMilliseconds()

                _state.update {
                    when (it?.activeDateTimeOption) {
                        ZoneRetentionState.Companion.DateTimeOption.FROM_TIME ->
                            it.copy(fromDateTime = updated, activeDateTimeOption = null)

                        ZoneRetentionState.Companion.DateTimeOption.TO_TIME ->
                            it.copy(toDateTime = updated, activeDateTimeOption = null)

                        else -> it
                    }
                }
            }

            ZoneRetentionAction.OnGenerateClicked -> {
                val s = _state.value ?: return
                val asset = s.selectedAsset ?: return _state.update { it?.copy(errorRes = R.string.generic_error_message) }
                val zone = s.selectedZone ?: return _state.update { it?.copy(errorRes = R.string.generic_error_message) }
                val from = s.fromDateTime ?: return _state.update { it?.copy(errorRes = R.string.heatmap_error_from_required) }
                val to = s.toDateTime ?: return _state.update { it?.copy(errorRes = R.string.heatmap_error_to_required) }

                if (from >= to) {
                    _state.update { it?.copy(errorRes = R.string.heatmap_error_invalid_time_range) }
                    return
                }

                _state.update { it?.copy(isButtonLoading = true, errorRes = null) }

                viewModelScope.launch(Dispatchers.IO) {
                    zoneRetentionHistoryRepository.getZoneRetentionHistory(
                        assetId = asset.id,
                        zoneId = zone.id,
                        from = from,
                        to = to,
                    ).fold(
                        onSuccess = { rows ->
                            _state.update {
                                it?.copy(
                                    isButtonLoading = false,
                                    reportRows = rows,
                                    mode = ZoneRetentionScreenMode.REPORT
                                )
                            }
                        },
                        onFailure = {
                            _state.update {
                                it?.copy(
                                    isButtonLoading = false,
                                    errorRes = R.string.generic_error_message
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
