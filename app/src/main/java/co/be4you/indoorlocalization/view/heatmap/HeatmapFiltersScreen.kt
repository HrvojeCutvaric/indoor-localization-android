package co.be4you.indoorlocalization.view.heatmap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.be4you.core.ui.theme.IndoorLocalizationTheme
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.utils.formatDate
import co.be4you.indoorlocalization.utils.formatTime
import co.be4you.indoorlocalization.view.components.IndoorLocalizationDatePickerDialog
import co.be4you.indoorlocalization.view.components.IndoorLocalizationTimePicker
import co.be4you.indoorlocalization.viewmodel.heatmap.HeatmapAction
import co.be4you.indoorlocalization.viewmodel.heatmap.HeatmapState

@Composable
fun HeatmapFiltersScreen(
    state: HeatmapState,
    onAction: (HeatmapAction) -> Unit
) {
    val dateTime = when (state.activeDateTimeOption) {
        HeatmapState.Companion.DateTimeOption.FROM_DATE,
        HeatmapState.Companion.DateTimeOption.FROM_TIME -> state.fromDateTime

        HeatmapState.Companion.DateTimeOption.TO_DATE,
        HeatmapState.Companion.DateTimeOption.TO_TIME -> state.toDateTime

        null -> null
    }

    val isDatePickerVisible =
        state.activeDateTimeOption == HeatmapState.Companion.DateTimeOption.FROM_DATE || state.activeDateTimeOption == HeatmapState.Companion.DateTimeOption.TO_DATE

    val isTimePickerVisible =
        state.activeDateTimeOption == HeatmapState.Companion.DateTimeOption.FROM_TIME || state.activeDateTimeOption == HeatmapState.Companion.DateTimeOption.TO_TIME

    IndoorLocalizationDatePickerDialog(
        dateTime = dateTime,
        isDatePickerVisible = isDatePickerVisible,
        onConfirmClicked = { onAction(HeatmapAction.OnDateSelected(it)) },
        onDismissRequested = { onAction(HeatmapAction.OnCloseDateTimePicker) }
    )

    IndoorLocalizationTimePicker(
        dateTime = dateTime,
        isTimePickerVisible = isTimePickerVisible,
        onTimeConfirm = { hour, minute ->
            onAction(HeatmapAction.OnTimeConfirm(hour = hour, minute = minute))
        },
        onDismiss = { onAction(HeatmapAction.OnCloseDateTimePicker) }
    )

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
    ) {
        DateTimeRowSection(
            dateText = state.fromDateTime?.formatDate().orEmpty(),
            timeText = state.fromDateTime?.formatTime().orEmpty(),
            datePlaceholder = stringResource(R.string.from_date),
            timePlaceholder = stringResource(R.string.from_time),
            onDateClick = {
                onAction(
                    HeatmapAction.OnDateTimeFieldClicked(
                        HeatmapState.Companion.DateTimeOption.FROM_DATE,
                    )
                )
            },
            onTimeClick = {
                onAction(
                    HeatmapAction.OnDateTimeFieldClicked(
                        HeatmapState.Companion.DateTimeOption.FROM_TIME,
                    )
                )
            },
        )

        Spacer(modifier = Modifier.height(12.dp))

        DateTimeRowSection(
            dateText = state.toDateTime?.formatDate().orEmpty(),
            timeText = state.toDateTime?.formatTime().orEmpty(),
            datePlaceholder = stringResource(R.string.to_date),
            timePlaceholder = stringResource(R.string.to_time),
            onDateClick = {
                onAction(
                    HeatmapAction.OnDateTimeFieldClicked(
                        HeatmapState.Companion.DateTimeOption.TO_DATE,
                    )
                )
            },
            onTimeClick = {
                onAction(
                    HeatmapAction.OnDateTimeFieldClicked(
                        HeatmapState.Companion.DateTimeOption.TO_TIME,
                    )
                )
            },
        )

    }
}

@Composable
fun DateTimeRowSection(
    dateText: String,
    timeText: String,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    datePlaceholder: String = stringResource(R.string.date),
    timePlaceholder: String = stringResource(R.string.time),
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        IndoorLocalizationClickableTextField(
            modifier = Modifier.weight(2f),
            placeholder = datePlaceholder,
            text = dateText,
            onClick = onDateClick,
        )

        Spacer(modifier = Modifier.width(24.dp))

        IndoorLocalizationClickableTextField(
            modifier = Modifier.weight(1f),
            placeholder = timePlaceholder,
            text = timeText,
            onClick = onTimeClick,
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun HeatmapFiltersScreenPreview() {
    IndoorLocalizationTheme {
        HeatmapFiltersScreen(
            state = HeatmapState(),
            onAction = { }
        )
    }
}

