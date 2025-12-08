package co.be4you.password_login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.be4you.core.ui.components.DefaultButton
import co.be4you.core.ui.components.DefaultTextField
import co.be4you.core.ui.theme.IndoorLocalizationTheme

@Composable
fun PasswordLoginLayout(
    state: PasswordLoginUiState,
    onAction: (PasswordLoginUiAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(co.be4you.core.R.string.login),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(modifier = Modifier.height(24.dp)) {
            state.errorResource?.let {
                Text(
                    text = stringResource(it),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.error,
                    ),
                )
            }
        }

        DefaultTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.username,
            onValueChange = { onAction(PasswordLoginUiAction.OnUsernameChanged(it)) },
            label = co.be4you.core.R.string.username,
            placeholder = co.be4you.core.R.string.username,
        )

        Spacer(modifier = Modifier.height(12.dp))

        DefaultTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.password,
            onValueChange = { onAction(PasswordLoginUiAction.OnPasswordChanged(it)) },
            label = co.be4you.core.R.string.password,
            placeholder = co.be4you.core.R.string.password,
            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = if (state.isPasswordVisible) co.be4you.core.R.drawable.ic_visibility_off else co.be4you.core.R.drawable.ic_visibility,
            onTrailingIconClicked = { onAction(PasswordLoginUiAction.OnPasswordVisibilityChanged) },
        )

        Spacer(modifier = Modifier.height(24.dp))

        DefaultButton(
            modifier = Modifier.fillMaxWidth(),
            label = co.be4you.core.R.string.login,
            onButtonClicked = { onAction(PasswordLoginUiAction.OnLoginClicked) },
            isButtonLoading = state.isButtonLoading,
            isButtonEnabled = state.isButtonLoading.not() && state.username.isNotEmpty() && state.password.isNotEmpty(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(co.be4you.core.R.string.dont_have_an_account),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.outline,
                )
            )

            TextButton(
                onClick = { onAction(PasswordLoginUiAction.OnRegisterClicked) },
                enabled = state.isButtonLoading.not()
            ) {
                Text(
                    text = stringResource(co.be4you.core.R.string.registration),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                )
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
private fun PasswordLayoutPreview() {
    IndoorLocalizationTheme {
        PasswordLoginLayout(
            state = PasswordLoginUiState(
                username = "",
                password = "",
                isPasswordVisible = false,
                errorResource = null,
                isButtonLoading = false,
            ),
            onAction = {},
        )
    }
}
