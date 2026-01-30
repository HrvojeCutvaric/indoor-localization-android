package co.be4you.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.be4you.core.R
import co.be4you.core.ui.theme.BrandLightBlue
import co.be4you.core.ui.theme.CommonBlue
import co.be4you.core.ui.theme.IndoorLocalizationTheme

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    @StringRes label: Int? = null,
    isButtonEnabled: Boolean = true,
    isButtonLoading: Boolean = false,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = CommonBlue,
        contentColor = Color.White,
        disabledContainerColor = CommonBlue.copy(alpha = 0.35f),
        disabledContentColor = Color.White.copy(alpha = 0.7f),
    ),
    onButtonClicked: () -> Unit,
    content: (@Composable () -> Unit)? = null,
) {
    Button(
        modifier = modifier,
        enabled = isButtonEnabled && !isButtonLoading,
        shape = ShapeDefaults.Small,
        contentPadding = PaddingValues(16.dp),
        onClick = onButtonClicked,
        colors = buttonColors
    ) {
        if (isButtonLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(ButtonDefaults.IconSize),
                strokeWidth = 1.dp,
                color = Color.White
            )
        } else {
            when {
                content != null -> content()

                label != null -> Text(
                    text = stringResource(label),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultButtonPreview() {
    IndoorLocalizationTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                label = R.string.registration,
                isButtonLoading = true,
                onButtonClicked = {},
            )
        }
    }
}

@Composable
fun LabelWithTextButton(
    modifier: Modifier = Modifier,
    label: String,
    buttonLabel: String,
    isButtonLoading: Boolean,
    onTextButtonClicked: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = BrandLightBlue
            )
        )

        TextButton(
            onClick = onTextButtonClicked,
            enabled = isButtonLoading.not()
        ) {
            Text(
                text = buttonLabel,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            )
        }
    }
}

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    label: String,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.primary,
    ),
    borderStroke: BorderStroke = BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outlineVariant
    ),
    onButtonClicked: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier,
        colors = colors,
        border = borderStroke,
        shape = ShapeDefaults.Small,
        enabled = isEnabled,
        contentPadding = PaddingValues(16.dp),
        onClick = onButtonClicked,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(ButtonDefaults.IconSize),
                strokeWidth = 1.dp,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                ),
            )
        }
    }
}
