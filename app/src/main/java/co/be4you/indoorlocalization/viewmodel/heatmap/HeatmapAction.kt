package co.be4you.indoorlocalization.viewmodel.heatmap

sealed interface HeatmapAction {
    data object OnFromDateClicked : HeatmapAction

    data object OnToDateClicked : HeatmapAction

    data object OnCloseDateTimePicker : HeatmapAction

    data class OnDateSelected(val date: Long?) : HeatmapAction

    data class OnTimeConfirm(val hour: Int, val minute: Int) : HeatmapAction

    data class OnDateTimeFieldClicked(
        val option: HeatmapState.Companion.DateTimeOption
    ) : HeatmapAction
}
