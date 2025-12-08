package co.be4you.indoorlocalization.view.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.core.ui.components.DefaultButton
import co.be4you.core.ui.theme.IndoorLocalizationTheme
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
    state.loginHandler?.LoginLayout() ?: run {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(co.be4you.core.R.string.select_a_login_method),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.loginHandlers) { loginHandler ->
                    DefaultButton(
                        modifier = Modifier.fillMaxWidth(),
                        label = loginHandler.buttonTextResource,
                        onButtonClicked = { onAction(LoginAction.OnLoginHandlerClicked(loginHandler)) }
                    )
                }
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
                loginHandler = null,
                loginHandlers = emptyList()
            ),
            onAction = {}
        )
    }
}
