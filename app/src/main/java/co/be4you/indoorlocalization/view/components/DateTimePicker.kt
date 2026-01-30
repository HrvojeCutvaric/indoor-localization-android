@file:OptIn(ExperimentalTime::class)

package co.be4you.indoorlocalization.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import co.be4you.indoorlocalization.R
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndoorLocalizationDatePickerDialog(
    dateTime: Long?,
    isDatePickerVisible: Boolean,
    onConfirmClicked: (Long?) -> Unit,
    onDismissRequested: () -> Unit
) {
    val initial = dateTime ?: kotlin.time.Clock.System.now().toEpochMilliseconds()

    key(initial) {
        val datePickerState = rememberDatePickerState(
            initialDisplayedMonthMillis = initial,
            initialSelectedDateMillis = initial,
        )

        AnimatedVisibility(isDatePickerVisible) {
            DatePickerDialog(
                onDismissRequest = onDismissRequested,
                confirmButton = {
                    TextButton(
                        onClick = { onConfirmClicked(datePickerState.selectedDateMillis) },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary,
                        ),
                    ) {
                        Text(
                            text = stringResource(R.string.ok),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = onDismissRequested,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    ) {
                        Text(
                            stringResource(R.string.cancel),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                },
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface,
                )
            ) {
                DatePicker(
                    state = datePickerState,
                    title = {
                        Text(
                            modifier = Modifier
                                .padding(PaddingValues(start = 24.dp, end = 12.dp, top = 16.dp)),
                            text = stringResource(R.string.date).uppercase(),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                        )
                    },
                    colors = DatePickerDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surface,

                        selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
                        selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                        currentYearContentColor = MaterialTheme.colorScheme.primary,
                        selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                        selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                        todayContentColor = MaterialTheme.colorScheme.onSurface,
                        todayDateBorderColor = MaterialTheme.colorScheme.onSurface,
                        dateTextFieldColors = TextFieldDefaults.colors(
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        )
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndoorLocalizationTimePicker(
    dateTime: Long?,
    isTimePickerVisible: Boolean,
    onTimeConfirm: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val instant =
        Instant.fromEpochMilliseconds(
            dateTime ?: kotlin.time.Clock.System.now().toEpochMilliseconds()
        )
    val localDateTime: LocalDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    key(
        localDateTime.hour,
        localDateTime.minute,
    ) {
        val timePickerState = rememberTimePickerState(
            initialHour = localDateTime.hour,
            initialMinute = localDateTime.minute,
            is24Hour = true,
        )

        var isTimeInputVisible by remember { mutableStateOf(true) }

        AnimatedVisibility(isTimePickerVisible) {
            AdvancedTimePickerDialog(
                onDismissRequested = onDismiss,
                onConfirmClicked = { onTimeConfirm(timePickerState.hour, timePickerState.minute) },
                toggle = {
                    IconButton(
                        onClick = { isTimeInputVisible = isTimeInputVisible.not() },
                    ) {
                        Icon(
                            painter = painterResource(if (isTimeInputVisible) R.drawable.ic_keyboard else R.drawable.ic_time),
                            contentDescription = null,
                        )
                    }
                },
            ) {
                if (isTimeInputVisible) {
                    TimeInput(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.onPrimary,
                        )
                    )
                } else {
                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            selectorColor = MaterialTheme.colorScheme.primary,
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun AdvancedTimePickerDialog(
    onDismissRequested: () -> Unit,
    onConfirmClicked: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequested,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(10),
            tonalElevation = 8.dp,
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    text = stringResource(R.string.time).uppercase(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                )

                content()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    toggle()

                    Spacer(modifier = Modifier.weight(1f))

                    TextButton(
                        onClick = onDismissRequested,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    ) {
                        Text(
                            text = stringResource(R.string.cancel).uppercase(),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }

                    TextButton(
                        onClick = onConfirmClicked,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary,
                        ),
                    ) {
                        Text(
                            text = stringResource(R.string.ok).uppercase(),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                }
            }
        }
    }
}
