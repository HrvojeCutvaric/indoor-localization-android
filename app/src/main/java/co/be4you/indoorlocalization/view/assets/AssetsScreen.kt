package co.be4you.indoorlocalization.view.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.core.domain.models.Asset
import co.be4you.core.ui.components.DefaultButton
import co.be4you.indoorlocalization.view.common.DefaultTopBar
import co.be4you.indoorlocalization.viewmodel.assets.AssetsAction
import co.be4you.indoorlocalization.viewmodel.assets.AssetsViewModel
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun AssetsScreen(
    floorMapId: Long,
    floorMapName: String,
    viewModel: AssetsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(floorMapId) {
        viewModel.setFloorMapId(floorMapId)
    }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, floorMapId) {
        val observer = LifecycleEventObserver  {_, event ->
            if(event == Lifecycle.Event.ON_RESUME){
                viewModel.setFloorMapId(floorMapId)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DefaultTopBar(
            title = "Assets",
            onBack = { viewModel.execute(AssetsAction.OnBackClicked) },
            onLogout = { viewModel.execute(AssetsAction.OnLogoutClicked) }
        )

        DefaultButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            onButtonClicked = {/*TODO*/ },
            label = null,
            content = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Asset",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add Asset",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                    )
                }
            }

        )

        AssetSearchBar(
            query = state.searchQuery,
            onQueryChanged = { viewModel.execute(AssetsAction.OnSearchChanged(it)) }
        )

        val assets = state.filteredAssets

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            if (assets.isEmpty()) {
                item {
                    Text(
                        text = "No asset matches your search",
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            } else {
                items(assets) { asset ->
                    AssetRow(
                        asset = asset,
                        floorMapName = floorMapName,
                        onClick = { id ->
                            viewModel.execute(AssetsAction.OnAssetClicked(id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AssetSearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = { Text("Search assets...") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}


@Composable
fun AssetRow(
    asset: Asset,
    floorMapName: String,
    onClick: (Long) -> Unit
) {
    val color = asset.colorHex?.let { colorHex ->
        try {
            Color(colorHex.toColorInt())
        } catch (_: Exception) {
            Color.Gray
        }
    } ?: Color.Gray

    val statusText = if (asset.active) "Active" else "Inactive"
    val statusColor = if (asset.active) Color(0xFF2ECC71) else Color(0xFFE74C3C)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
            .clickable { onClick(asset.id) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(color, RoundedCornerShape(4.dp))
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                asset.name,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = statusText,
                color = statusColor,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }

        Text(
            text = floorMapName,
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
