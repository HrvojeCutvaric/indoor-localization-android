package co.be4you.indoorlocalization.view.zoneretention

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.utils.ZoneRetentionScreenMode
import co.be4you.indoorlocalization.view.common.DefaultTopBar
import co.be4you.indoorlocalization.view.components.IndoorLocalizationDatePickerDialog
import co.be4you.indoorlocalization.view.components.IndoorLocalizationTimePicker
import co.be4you.indoorlocalization.viewmodel.zoneretention.ZoneRetentionAction
import co.be4you.indoorlocalization.viewmodel.zoneretention.ZoneRetentionState
import co.be4you.indoorlocalization.viewmodel.zoneretention.ZoneRetentionViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ZoneRetentionScreen(
    viewModel: ZoneRetentionViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    state?.let { s ->

        val activeMillis = when (s.activeDateTimeOption) {
            ZoneRetentionState.Companion.DateTimeOption.FROM_DATE,
            ZoneRetentionState.Companion.DateTimeOption.FROM_TIME -> s.fromDateTime

            ZoneRetentionState.Companion.DateTimeOption.TO_DATE,
            ZoneRetentionState.Companion.DateTimeOption.TO_TIME -> s.toDateTime

            null -> null
        }

        val showDatePicker =
            s.activeDateTimeOption == ZoneRetentionState.Companion.DateTimeOption.FROM_DATE ||
                    s.activeDateTimeOption == ZoneRetentionState.Companion.DateTimeOption.TO_DATE

        val showTimePicker =
            s.activeDateTimeOption == ZoneRetentionState.Companion.DateTimeOption.FROM_TIME ||
                    s.activeDateTimeOption == ZoneRetentionState.Companion.DateTimeOption.TO_TIME

        IndoorLocalizationDatePickerDialog(
            dateTime = activeMillis,
            isDatePickerVisible = showDatePicker,
            onConfirmClicked = { viewModel.execute(ZoneRetentionAction.OnDateSelected(it)) },
            onDismissRequested = { viewModel.execute(ZoneRetentionAction.OnCloseDateTimePicker) }
        )

        IndoorLocalizationTimePicker(
            dateTime = activeMillis,
            isTimePickerVisible = showTimePicker,
            onTimeConfirm = { h, m -> viewModel.execute(ZoneRetentionAction.OnTimeConfirm(h, m)) },
            onDismiss = { viewModel.execute(ZoneRetentionAction.OnCloseDateTimePicker) }
        )

        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = stringResource(R.string.zone_retention),
                    onBack = { viewModel.execute(ZoneRetentionAction.OnBackClicked) },
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when (s.mode) {
                    ZoneRetentionScreenMode.FILTERS ->
                        ZoneRetentionFiltersLayout(state = s, onAction = viewModel::execute)

                    ZoneRetentionScreenMode.REPORT ->
                        ZoneRetentionReportLayout(state = s, onAction = viewModel::execute)
                }
            }
        }
    } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
