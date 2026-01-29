package co.be4you.indoorlocalization.view.heatmap

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IndoorLocalizationClickableTextField(
    modifier: Modifier = Modifier,
    text: String,
    placeholder: String,
    trailingIcon: Painter? = null,
    onClick: () -> Unit,
    onTrailingIconClicked: () -> Unit = {},
) {
    val textColor =
        if (text.isNotEmpty()) MaterialTheme.colorScheme.onSurface
        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)

    Box(
        modifier = modifier
            .height(62.dp)
            .clip(shape = OutlinedTextFieldDefaults.shape)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(
                    color = MaterialTheme.colorScheme.outline,
                    shape = OutlinedTextFieldDefaults.shape,
                    width = 1.dp,
                )
                .align(Alignment.BottomStart),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = text.ifEmpty { placeholder },
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                if (trailingIcon != null) {
                    AnimatedVisibility(visible = text.isNotEmpty()) {
                        Icon(
                            modifier = Modifier.clickable(onClick = onTrailingIconClicked),
                            painter = trailingIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        AnimatedVisibility(visible = text.isNotEmpty()) {
            Box(
                modifier = Modifier.padding(start = 12.dp).align(Alignment.TopStart)
            ) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = placeholder,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}
