package co.be4you.indoorlocalization.view.login

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.core.R
import co.be4you.core.ui.components.LabelWithTextButton
import co.be4you.core.ui.theme.IndoorLocalizationTheme
import co.be4you.indoorlocalization.view.common.AuthHeader
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
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            AuthHeader(
                bottomContent = {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        items(state.loginHandlers) {
                            val isSelected = it == state.loginHandler
                            val contentColor =
                                if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            val containerColor =
                                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                            val borderColor =
                                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            IconButton(
                                modifier = Modifier
                                    .size(52.dp)
                                    .border(
                                        width = 1.dp,
                                        color = borderColor,
                                        shape = ShapeDefaults.Small,
                                    ),
                                onClick = { onAction(LoginAction.OnLoginHandlerClicked(it)) },
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = contentColor,
                                    containerColor = containerColor,
                                ),
                                shape = ShapeDefaults.Small,
                            ) {
                                Icon(
                                    modifier = Modifier.aspectRatio(0.6f),
                                    painter = painterResource(it.iconResource),
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                }
            )

            state.loginHandler?.let { loginHandler ->
                loginHandler.LoginLayout(modifier = Modifier.padding(horizontal = 12.dp))

                Spacer(modifier = Modifier.height(12.dp))

                LabelWithTextButton(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.dont_have_an_account),
                    buttonLabel = stringResource(R.string.register),
                    isButtonLoading = false,
                    onTextButtonClicked = { onAction(LoginAction.OnRegisterClicked) },
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
                loginHandler = null,
                loginHandlers = emptyList()
            ),
            onAction = {}
        )
    }
}
