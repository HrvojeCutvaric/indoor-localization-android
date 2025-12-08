package co.be4you.indoorlocalization.view.assetdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.indoorlocalization.view.common.DefaultButton
import co.be4you.indoorlocalization.view.common.DefaultTopBar
import co.be4you.indoorlocalization.viewmodel.assetdetail.AssetDetailAction
import co.be4you.indoorlocalization.viewmodel.assetdetail.AssetDetailViewModel
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import org.koin.androidx.compose.koinViewModel

@Composable
fun AssetDetailScreen(
    assetId: Long,
    onAction: (MainAction) -> Unit,
    viewModel: AssetDetailViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(assetId) {
        viewModel.execute(AssetDetailAction.Load(assetId))
    }

    Column(modifier = Modifier.fillMaxSize()) {

        DefaultTopBar(
            title = "Asset Details",
            onBack = {onAction(MainAction.NavigateBack())}
        )

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            state.errorResource != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = state.errorResource),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }


            state.asset != null -> {
                val asset = state.asset

                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = asset.name,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(Modifier.height(20.dp))

                    InfoItem("Status", if (asset.active) "Active" else "Inactive")
                    InfoItem("Last Known Position", "(${asset.x}, ${asset.y})")
                    InfoItem("Floor Map", asset.floorMapId.toString())
                    InfoItem("Last Sync", asset.lastSync?.toString() ?: "Unknown")

                    Spacer(Modifier.height(40.dp))

                    DefaultButton(
                        modifier = Modifier.fillMaxWidth(),
                        label = null,
                        onButtonClicked = { /* TODO EDIT */ },
                        content = { Text("Edit", color = Color.White) }
                    )

                    Spacer(Modifier.height(16.dp))

                    DefaultButton(
                        modifier = Modifier.fillMaxWidth(),
                        label = null,
                        onButtonClicked = { /* TODO DELETE */ },
                        content = { Text("Delete", color = Color.White) }
                    )
                }
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = label,
            color = Color(0xFF75AEE8),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}
