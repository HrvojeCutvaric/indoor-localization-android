package co.be4you.indoorlocalization.view.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.ui.theme.IndoorLocalizationTheme

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    isButtonEnabled: Boolean = true,
    isButtonLoading: Boolean = false,
    onButtonClicked: () -> Unit,
) {
    Button(
        modifier = modifier,
        enabled = isButtonEnabled,
        shape = ShapeDefaults.Small,
        contentPadding = PaddingValues(16.dp),
        onClick = onButtonClicked,
    ) {
        if (isButtonLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(ButtonDefaults.IconSize),
                strokeWidth = 1.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text(
                text = stringResource(label),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
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
