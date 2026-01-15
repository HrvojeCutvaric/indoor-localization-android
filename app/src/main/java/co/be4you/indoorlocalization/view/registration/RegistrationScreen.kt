package co.be4you.indoorlocalization.view.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import co.be4you.core.ui.components.DefaultButton
import co.be4you.core.ui.components.DefaultTextField
import co.be4you.core.ui.theme.BrandLightBlue
import co.be4you.core.ui.theme.IndoorLocalizationTheme
import co.be4you.core.ui.theme.LinkText
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.view.common.AuthHeader
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import co.be4you.indoorlocalization.viewmodel.registration.RegistrationAction
import co.be4you.indoorlocalization.viewmodel.registration.RegistrationState
import co.be4you.indoorlocalization.viewmodel.registration.RegistrationViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = koinViewModel(),
    onAction: (MainAction) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest(onAction)
    }

    RegistrationLayout(
        state = state,
        onAction = viewModel::execute,
    )
}

@Composable
private fun RegistrationLayout(
    state: RegistrationState,
    onAction: (RegistrationAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            AuthHeader(
                height = 200.dp,
                logoHeight = 120.dp
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Box(modifier = Modifier.height(36.dp)) {
                        state.error?.let {
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
                        value = state.firstName,
                        onValueChange = { onAction(RegistrationAction.OnFirstNameChanged(it)) },
                        label = R.string.first_name,
                        placeholder = R.string.first_name,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DefaultTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.lastName,
                        onValueChange = { onAction(RegistrationAction.OnLastNameChanged(it)) },
                        label = R.string.last_name,
                        placeholder = R.string.last_name,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DefaultTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.email,
                        onValueChange = { onAction(RegistrationAction.OnEmailChanged(it)) },
                        label = R.string.email,
                        placeholder = R.string.email,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DefaultTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.username,
                        onValueChange = { onAction(RegistrationAction.OnUsernameChanged(it)) },
                        label = R.string.username,
                        placeholder = R.string.username,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DefaultTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.password,
                        onValueChange = { onAction(RegistrationAction.OnPasswordChanged(it)) },
                        label = R.string.password,
                        placeholder = R.string.password,
                        visualTransformation = if (state.isPasswordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = if (state.isPasswordVisible) {
                            co.be4you.core.R.drawable.ic_visibility_off
                        } else {
                            co.be4you.core.R.drawable.ic_visibility
                        },
                        onTrailingIconClicked = { onAction(RegistrationAction.OnPasswordVisibilityChanged) },
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DefaultTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.confirmPassword,
                        onValueChange = { onAction(RegistrationAction.OnConfirmPasswordChanged(it)) },
                        label = R.string.confirm_password,
                        placeholder = R.string.confirm_password,
                        visualTransformation = if (state.isConfirmPasswordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = if (state.isConfirmPasswordVisible) {
                            co.be4you.core.R.drawable.ic_visibility_off
                        } else {
                            co.be4you.core.R.drawable.ic_visibility
                        },
                        onTrailingIconClicked = { onAction(RegistrationAction.OnConfirmPasswordVisibilityChanged) },
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    DefaultButton(
                        modifier = Modifier.fillMaxWidth(),
                        label = R.string.register,
                        onButtonClicked = { onAction(RegistrationAction.OnRegisterClicked) },
                        isButtonLoading = state.isButtonLoading,
                        isButtonEnabled = state.isButtonLoading.not(),
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.already_have_an_account),
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal,
                                color = BrandLightBlue
                            )
                        )

                        TextButton(onClick = { onAction(RegistrationAction.OnLoginClicked) }) {
                            Text(
                                text = stringResource(R.string.login),
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LinkText
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegistrationLayoutPreview() {
    IndoorLocalizationTheme {
        RegistrationLayout(
            state = RegistrationState(
                email = "test123@gmail.com",
                password = "test123",
                confirmPassword = "test123",
                isPasswordVisible = false,
                isConfirmPasswordVisible = false,
                isButtonLoading = false,
                error = R.string.weak_password,
                username = "",
                firstName = "",
                lastName = "",
            ),
            onAction = {},
        )
    }
}
