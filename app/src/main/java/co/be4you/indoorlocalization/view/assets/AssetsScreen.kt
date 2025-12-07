package co.be4you.indoorlocalization.view.assets

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.be4you.indoorlocalization.view.common.DefaultButton
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import co.be4you.indoorlocalization.viewmodel.assets.AssetsViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import co.be4you.core.domain.models.Asset

@Composable
fun AssetsScreen(
    floorMapId: Long,
    floorMapName: String,
    onAction: (MainAction) -> Unit,
    viewModel: AssetsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(floorMapId) {
        viewModel.loadAssets(floorMapId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color(0xFF2B85ED))
                .padding(top = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Assets",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }

        DefaultButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical=12.dp),
            onButtonClicked = {/*TODO*/},
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
            onQueryChanged = { viewModel.updateSearchQuery(it) }
        )

        val assets = state.filteredAssets

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            if(assets.isEmpty()){
                item{
                    Text(
                        text="No asset matches your search",
                        color = Color.Gray,
                        modifier = Modifier.padding(top=16.dp)
                    )
                }
            } else{
                items(assets) { asset ->
                    AssetRow(asset)
                }
            }
        }



        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Button(
                onClick = { onAction(MainAction.NavigateBack()) },
                colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2B85ED),
                contentColor = Color.White
            )
            ) {
                Text(
                    text = "Back",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                    .padding(8.dp)
                    .clickable{ onAction(MainAction.NavigateBack()) }
                )
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
fun AssetRow(asset: Asset) {
    val color = try {
        Color(android.graphics.Color.parseColor(asset.colorHex ?: "#888888"))
    } catch (e: Exception) {
        Color.Gray
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
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
            Text(asset.name, style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = asset.active,
                    onCheckedChange = { /* TODO */ }
                )
                Text("Active")
            }
        }

        Text(asset.floorMapId.toString(), color = Color.Gray)
    }
}
