package co.be4you.indoorlocalization.view.createasset

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import co.be4you.core.ui.components.DefaultTextField
import co.be4you.indoorlocalization.R
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Composable
fun ColorPickerSection(
    colorHex: String,
    onColorHexChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val controller = rememberColorPickerController()

    var hexText by remember { mutableStateOf(colorHex) }

    LaunchedEffect(colorHex) {
        if (hexText != colorHex) hexText = colorHex
    }

    Column(modifier = modifier.fillMaxWidth()) {

        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            controller = controller,
            onColorChanged = { envelope: ColorEnvelope ->
                hexText = envelope.hexCode
                onColorHexChanged(envelope.hexCode)
            }
        )

        Spacer(Modifier.height(12.dp))

        Text("Brightness", style = MaterialTheme.typography.bodyMedium)
        BrightnessSlider(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            controller = controller
        )

        Spacer(Modifier.height(12.dp))

        Text("Alpha", style = MaterialTheme.typography.bodyMedium)
        AlphaSlider(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            controller = controller
        )

        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            AlphaTile(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                controller = controller
            )

            Spacer(Modifier.width(12.dp))

            DefaultTextField(
                modifier = Modifier.fillMaxWidth(),
                value = hexText,
                onValueChange = {
                    hexText = it
                    onColorHexChanged(it)
                },
                label = R.string.generic_color_label,
                placeholder = R.string.generic_color_placeholder,
            )
        }
    }
}
