package co.be4you.indoorlocalization.view.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.ui.theme.IndoorLocalizationTheme

@Composable
fun DefaultTextField(
    modifier: Modifier,
    value: String,
    @StringRes label: Int,
    @StringRes placeholder: Int,
    isSingleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    @DrawableRes trailingIcon: Int? = null,
    onValueChange: (String) -> Unit,
    onTrailingIconClicked: () -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        placeholder = { Text(stringResource(placeholder)) },
        singleLine = isSingleLine,
        visualTransformation = visualTransformation,
        trailingIcon = {
            trailingIcon?.let {
                IconButton(
                    onClick = onTrailingIconClicked,
                ) {
                    Icon(
                        painter = painterResource(it),
                        contentDescription = null,
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun DefaultTextFieldPreview() {
    IndoorLocalizationTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            val isTextVisible = false
            DefaultTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {},
                label = R.string.confirm_password,
                placeholder = R.string.confirm_password,
                visualTransformation = if (isTextVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = if (isTextVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility,
                onTrailingIconClicked = {},
            )
        }
    }
}
