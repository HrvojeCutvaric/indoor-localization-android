package co.be4you.indoorlocalization.view.zoneretention

import androidx.compose.foundation.layout.*
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.be4you.core.ui.components.DefaultButton
import co.be4you.core.ui.components.DefaultDropdownSelector
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.utils.formatDate
import co.be4you.indoorlocalization.utils.formatTime
import co.be4you.indoorlocalization.view.heatmap.IndoorLocalizationClickableTextField
import co.be4you.indoorlocalization.viewmodel.zoneretention.ZoneRetentionAction
import co.be4you.indoorlocalization.viewmodel.zoneretention.ZoneRetentionState

@Composable
fun ZoneRetentionFiltersLayout(
    state: ZoneRetentionState,
    onAction: (ZoneRetentionAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {

        state.errorRes?.let {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                text = stringResource(it),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(8.dp))
        }

        DefaultDropdownSelector(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            selectedValue = state.selectedAsset?.name.orEmpty(),
            placeholder = R.string.select_asset,
            isExpanded = state.isAssetDropdownExpanded,
            onExpandedChange = { onAction(ZoneRetentionAction.OnAssetDropdownExpandedChanged) },
            onDismissRequest = { onAction(ZoneRetentionAction.OnDismissAssetDropdown) },
        ) {
            state.assets.forEach { asset ->
                DropdownMenuItem(
                    text = { Text(asset.name) },
                    onClick = { onAction(ZoneRetentionAction.OnAssetSelected(asset)) }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        DefaultDropdownSelector(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            selectedValue = state.selectedZone?.name.orEmpty(),
            placeholder = R.string.select_zone,
            isExpanded = state.isZoneDropdownExpanded,
            onExpandedChange = { onAction(ZoneRetentionAction.OnZoneDropdownExpandedChanged) },
            onDismissRequest = { onAction(ZoneRetentionAction.OnDismissZoneDropdown) },
        ) {
            state.zones.forEach { zone ->
                DropdownMenuItem(
                    text = { Text(zone.name) },
                    onClick = { onAction(ZoneRetentionAction.OnZoneSelected(zone)) }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
        ) {
            IndoorLocalizationClickableTextField(
                modifier = Modifier.weight(2f),
                placeholder = stringResource(R.string.from_date),
                text = state.fromDateTime?.formatDate().orEmpty(),
                onClick = {
                    onAction(
                        ZoneRetentionAction.OnDateTimeFieldClicked(
                            ZoneRetentionState.Companion.DateTimeOption.FROM_DATE
                        )
                    )
                },
            )

            Spacer(Modifier.width(24.dp))

            IndoorLocalizationClickableTextField(
                modifier = Modifier.weight(1f),
                placeholder = stringResource(R.string.from_time),
                text = state.fromDateTime?.formatTime().orEmpty(),
                onClick = {
                    onAction(
                        ZoneRetentionAction.OnDateTimeFieldClicked(
                            ZoneRetentionState.Companion.DateTimeOption.FROM_TIME
                        )
                    )
                },
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
        ) {
            IndoorLocalizationClickableTextField(
                modifier = Modifier.weight(2f),
                placeholder = stringResource(R.string.to_date),
                text = state.toDateTime?.formatDate().orEmpty(),
                onClick = {
                    onAction(
                        ZoneRetentionAction.OnDateTimeFieldClicked(
                            ZoneRetentionState.Companion.DateTimeOption.TO_DATE
                        )
                    )
                },
            )

            Spacer(Modifier.width(24.dp))

            IndoorLocalizationClickableTextField(
                modifier = Modifier.weight(1f),
                placeholder = stringResource(R.string.to_time),
                text = state.toDateTime?.formatTime().orEmpty(),
                onClick = {
                    onAction(
                        ZoneRetentionAction.OnDateTimeFieldClicked(
                            ZoneRetentionState.Companion.DateTimeOption.TO_TIME
                        )
                    )
                },
            )
        }

        Spacer(Modifier.height(16.dp))

        DefaultButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            label = R.string.generate,
            onButtonClicked = { onAction(ZoneRetentionAction.OnGenerateClicked) },
            isButtonLoading = state.isButtonLoading,
            isButtonEnabled = state.isButtonLoading.not(),
        )
    }
}
