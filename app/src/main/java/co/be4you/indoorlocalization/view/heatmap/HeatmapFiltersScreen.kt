package co.be4you.indoorlocalization.view.heatmap

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.be4you.core.domain.models.Asset
import co.be4you.core.ui.components.DefaultButton
import co.be4you.core.ui.components.SecondaryButton
import co.be4you.core.ui.theme.BrandLightBlue
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
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            state.selectedAssets.forEach { asset ->
                AssetTag(
                    asset = asset,
                    onRemove = { onAction(HeatmapAction.OnRemoveAssetClicked(asset)) },
                )
            }

            AddAssetTag(
                label = stringResource(R.string.add_asset),
                onClick = { onAction(HeatmapAction.OnAddAssetClicked) },
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

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

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.show_zone),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                ),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Switch(
                checked = state.showZones,
                onCheckedChange = { onAction(HeatmapAction.OnShowZonesClicked) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = BrandLightBlue,
                    checkedTrackColor = BrandLightBlue.copy(alpha = 0.3f),
                    uncheckedBorderColor = Color.Transparent
                ),
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        DefaultButton(
            modifier = Modifier.fillMaxWidth(),
            label = R.string.generate,
            onButtonClicked = { onAction(HeatmapAction.OnGenerateClicked) },
            isButtonLoading = state.isButtonLoading,
            isButtonEnabled = state.isButtonLoading.not(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        SecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(R.string.back_to_dashboard),
            onButtonClicked = { onAction(HeatmapAction.OnBackToDashboardClicked) },
            isLoading = state.isButtonLoading,
            isEnabled = state.isButtonLoading.not(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.outline,
            ),
            borderStroke = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline
            ),
        )
    }
}

@Composable
fun AssetTag(
    asset: Asset,
    onRemove: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface, CircleShape)
            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
            .clickable(onClick = onRemove)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(asset.color)
                .border(1.dp, MaterialTheme.colorScheme.surface, CircleShape)
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = asset.name,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            maxLines = 1
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = "×",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
fun AddAssetTag(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "+",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            maxLines = 1
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
            state = HeatmapState(
                searchQuery = ""
            ),
            onAction = { }
        )
    }
}

