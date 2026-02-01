package co.be4you.indoorlocalization.view.zoneretention

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.be4you.core.ui.theme.CommonBlue
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.utils.formatDate
import co.be4you.indoorlocalization.utils.formatTime
import co.be4you.indoorlocalization.viewmodel.zoneretention.ZoneRetentionAction
import co.be4you.indoorlocalization.viewmodel.zoneretention.ZoneRetentionState

@Composable
fun ZoneRetentionReportLayout(
    state: ZoneRetentionState,
    onAction: (ZoneRetentionAction) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (state.reportRows.isEmpty()) {
            Text(
                text = stringResource(R.string.no_data),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
            ) {
                items(state.reportRows) { row ->
                    Column(Modifier.fillMaxWidth()) {
                        Text(text = "Enter: ${row.enterDateTime.formatDate()} ${row.enterDateTime.formatTime()}")
                        Text(text = "Exit: ${row.exitDateTime.formatDate()} ${row.exitDateTime.formatTime()}")
                        Text(text = "Retention (ms): ${row.retentionTimeMillis}")
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }

        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            onClick = { onAction(ZoneRetentionAction.OnBackToFiltersClicked) },
            colors = IconButtonDefaults.iconButtonColors(contentColor = CommonBlue),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = stringResource(R.string.back),
            )
        }
    }
}
