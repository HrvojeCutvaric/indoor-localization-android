package co.be4you.indoorlocalization.view.heatmap

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.be4you.core.domain.models.Asset
import co.be4you.core.ui.components.DefaultButton
import co.be4you.core.ui.theme.BrandLightBlue
import co.be4you.core.ui.theme.CommonBlue
import co.be4you.core.ui.theme.White
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.viewmodel.heatmap.HeatmapAction
import co.be4you.indoorlocalization.viewmodel.heatmap.HeatmapState

@Composable
fun HeatmapSelectAssetsLayout(
    state: HeatmapState,
    onAction: (HeatmapAction) -> Unit,
) {
    Box {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(
                infoText = stringResource(R.string.info_select_assets)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(state.unselectedAssets) {
                    SelectAssetItem(
                        asset = it,
                        isSelectedAsset = state.isSelected(asset = it),
                        onAssetClicked = { onAction(HeatmapAction.OnAssetSelected(it)) }
                    )
                }
            }
        }

        DefaultButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .align(Alignment.BottomCenter),
            label = R.string.generic_save,
            isButtonEnabled = state.isButtonLoading.not(),
            isButtonLoading = state.isButtonLoading,
            onButtonClicked = { onAction(HeatmapAction.OnSaveSelectedAssetsClicked) }
        )
    }
}

@Composable
private fun SelectAssetItem(
    asset: Asset,
    isSelectedAsset: Boolean,
    onAssetClicked: () -> Unit,
) {
    Surface(
        onClick = onAssetClicked,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(asset.color)
                    .border(1.dp, MaterialTheme.colorScheme.surface, CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                modifier = Modifier.weight(1f),
                text = asset.name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.width(8.dp))

            AnimatedVisibility(isSelectedAsset) {
                Icon(
                    painter = painterResource(R.drawable.ic_check_circle),
                    contentDescription = null,
                    tint = CommonBlue,
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    infoText: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(color = BrandLightBlue, shape = CircleShape)
            .border(
                width = 1.dp,
                color = CommonBlue,
                shape = CircleShape
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_info),
            contentDescription = null,
            tint = Color.Black,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = infoText,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color.Black,
            ),
        )
    }
}
