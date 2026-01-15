package co.be4you.indoorlocalization.view.assetdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.core.ui.components.DefaultButton
import co.be4you.indoorlocalization.view.common.DefaultTopBar
import co.be4you.indoorlocalization.viewmodel.assetdetail.AssetDetailAction
import co.be4you.indoorlocalization.viewmodel.assetdetail.AssetDetailViewModel
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import org.koin.androidx.compose.koinViewModel

@Composable
fun AssetDetailScreen(
    assetId: Long,
    viewModel: AssetDetailViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(assetId) {
        viewModel.setAssetId(assetId)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        DefaultTopBar(
            title = "Asset Details",
            onBack = { viewModel.execute(AssetDetailAction.OnBackClicked) },
            onLogout = {viewModel.execute(AssetDetailAction.OnLogoutClicked)}
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

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(
                                    color = asset.colorHex?.let { colorHex ->
                                        try {
                                            Color(colorHex.toColorInt())
                                        } catch (_: Exception) {
                                            Color.Gray
                                        }
                                    } ?: Color.Gray,
                                    shape = MaterialTheme.shapes.medium
                                )
                        )

                        Spacer(Modifier.width(15.dp))

                        Text(
                            text = asset.name,
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(Modifier.height(15.dp))

                    InfoItem("Status", if (asset.active) "Active" else "Inactive")
                    InfoItem("Last Known Position", "(${asset.x}, ${asset.y})")
                    InfoItem("Floor Map", asset.floorMapId.toString())
                    InfoItem("Last Sync", asset.lastSync?.toString() ?: "Unknown")
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                label = null,
                onButtonClicked = { /* TODO EDIT */ },
                content = { Text("Edit", color = Color.White) }
            )

            Spacer(Modifier.height(16.dp))

            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                buttonColors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC54B3C),
                    contentColor = Color.White
                ),
                onButtonClicked = { /* TODO DELETE */ },
                content = { Text("Delete") }
            )
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 10.dp)) {

        Text(
            text = label,
            color = Color(0xFF75AEE8),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Light)
        )
        Spacer(Modifier.height(12.dp))
    }
}
