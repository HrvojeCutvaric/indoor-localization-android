package co.be4you.indoorlocalization.view.login

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.ui.theme.IndoorLocalizationTheme
import co.be4you.indoorlocalization.view.common.DefaultButton
import co.be4you.indoorlocalization.view.common.DefaultTextField
import co.be4you.indoorlocalization.viewmodel.login.LoginAction
import co.be4you.indoorlocalization.viewmodel.login.LoginState
import co.be4you.indoorlocalization.viewmodel.login.LoginViewModel
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onAction: (MainAction) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.event.collectLatest(onAction) }

    LoginLayout(
        state = state,
        onAction = viewModel::execute
    )
}

@Composable
private fun LoginLayout(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
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
            text = stringResource(R.string.login),
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
            value = state.email,
            onValueChange = { onAction(LoginAction.OnEmailChanged(it)) },
            label = R.string.email,
            placeholder = R.string.email,
        )

        Spacer(modifier = Modifier.height(12.dp))

        DefaultTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.password,
            onValueChange = { onAction(LoginAction.OnPasswordChanged(it)) },
            label = R.string.password,
            placeholder = R.string.password,
            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = if (state.isPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility,
            onTrailingIconClicked = { onAction(LoginAction.OnPasswordVisibilityChanged) },
        )

        Spacer(modifier = Modifier.height(24.dp))

        DefaultButton(
            modifier = Modifier.fillMaxWidth(),
            label = R.string.login,
            onButtonClicked = { onAction(LoginAction.OnLoginClicked) },
            isButtonLoading = state.isButtonLoading,
            isButtonEnabled = state.isButtonLoading.not() && state.email.isNotEmpty() && state.password.isNotEmpty(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.dont_have_an_account),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.outline,
                )
            )

            TextButton(
                onClick = { onAction(LoginAction.OnLoginClicked) }
            ) {
                Text(
                    text = stringResource(R.string.registration),
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
private fun LoginScreenPreview() {
    IndoorLocalizationTheme {
        LoginLayout(
            state = LoginState(
                email = "",
                password = "",
                isPasswordVisible = false,
                errorResource = null,
                isButtonLoading = false,
            ),
            onAction = {}
        )
    }
}
