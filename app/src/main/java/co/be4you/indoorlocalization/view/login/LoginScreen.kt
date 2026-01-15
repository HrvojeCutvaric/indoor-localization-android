package co.be4you.indoorlocalization.view.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.core.domain.utils.login.LoginHandler
import co.be4you.core.ui.components.DefaultButton
import co.be4you.core.ui.components.LabelWithTextButton
import co.be4you.core.ui.theme.IndoorLocalizationTheme
import co.be4you.indoorlocalization.viewmodel.login.LoginAction
import co.be4you.indoorlocalization.viewmodel.login.LoginState
import co.be4you.indoorlocalization.viewmodel.login.LoginViewModel
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.clickable
import co.be4you.indoorlocalization.view.common.AuthHeader
import co.be4you.core.ui.theme.CommonBlue
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onAction: (MainAction) -> Unit,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest(onAction)
    }

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
    val passwordHandler = state.loginHandlers.firstOrNull {
        it.buttonTextResource == co.be4you.password_login.R.string.login_with_password
    }
    val otpHandler = state.loginHandlers.firstOrNull {
        it.buttonTextResource == co.be4you.otp_login.R.string.login_with_one_time_code
    }

    val activeHandler: LoginHandler<*, *>? = state.loginHandler ?: passwordHandler ?: otpHandler

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
                bottomContent = {
                    LoginToggle(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        isPasswordSelected = activeHandler == passwordHandler,
                        onPasswordClick = { passwordHandler?.let { onAction(LoginAction.OnLoginHandlerClicked(it)) } },
                        isOtpSelected = activeHandler == otpHandler,
                        onOtpClick = { otpHandler?.let { onAction(LoginAction.OnLoginHandlerClicked(it)) } },
                    )
                }
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (activeHandler != null) {
                        activeHandler.LoginLayout(modifier = Modifier.fillMaxWidth())

                        Spacer(modifier = Modifier.height(12.dp))

                        LabelWithTextButton(
                            label = stringResource(co.be4you.core.R.string.dont_have_an_account),
                            buttonLabel = stringResource(co.be4you.core.R.string.register),
                            isButtonLoading = false,
                            onTextButtonClicked = { onAction(LoginAction.OnRegisterClicked) },
                        )
                    } else {
                        Text(
                            text = stringResource(co.be4you.core.R.string.select_a_login_method),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        state.loginHandlers.forEach { handler ->
                            DefaultButton(
                                modifier = Modifier.fillMaxWidth(),
                                label = handler.buttonTextResource,
                                onButtonClicked = { onAction(LoginAction.OnLoginHandlerClicked(handler)) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LabelWithTextButton(
                            label = stringResource(co.be4you.core.R.string.dont_have_an_account),
                            buttonLabel = stringResource(co.be4you.core.R.string.register),
                            isButtonLoading = false,
                            onTextButtonClicked = { onAction(LoginAction.OnRegisterClicked) },
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun LoginToggle(
    modifier: Modifier = Modifier,
    isPasswordSelected: Boolean,
    onPasswordClick: () -> Unit,
    isOtpSelected: Boolean,
    onOtpClick: () -> Unit,
) {
    val shape = RoundedCornerShape(999.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color.White.copy(alpha = 0.25f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        ToggleChip(
            modifier = Modifier.weight(1f),
            selected = isPasswordSelected,
            text = "Account",
            onClick = onPasswordClick
        )
        ToggleChip(
            modifier = Modifier.weight(1f),
            selected = isOtpSelected,
            text = "OTP",
            onClick = onOtpClick
        )
    }
}

@Composable
private fun ToggleChip(
    modifier: Modifier,
    selected: Boolean,
    text: String,
    onClick: () -> Unit,
) {
    val backgroundColor = if(selected) Color.White else Color.White.copy(alpha = 0.18f)
    val textColor = if(selected) CommonBlue else Color.White
    val borderColor = Color.White

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .clickable { onClick() }
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(999.dp)
            )
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    IndoorLocalizationTheme {
        LoginLayout(
            state = LoginState(
                loginHandler = null,
                loginHandlers = emptyList()
            ),
            onAction = {}
        )
    }
}
