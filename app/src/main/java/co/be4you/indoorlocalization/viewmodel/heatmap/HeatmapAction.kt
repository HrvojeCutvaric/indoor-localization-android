package co.be4you.indoorlocalization.viewmodel.heatmap

sealed interface HeatmapAction {

    data object OnGenerateClicked : HeatmapAction

    data object OnBackToDashboardClicked : HeatmapAction

    data object OnCloseDateTimePicker : HeatmapAction

    data class OnDateSelected(val date: Long?) : HeatmapAction

    data class OnTimeConfirm(val hour: Int, val minute: Int) : HeatmapAction

    data class OnDateTimeFieldClicked(
        val option: HeatmapState.Companion.DateTimeOption
    ) : HeatmapAction
}
