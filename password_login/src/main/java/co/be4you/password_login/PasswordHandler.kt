package co.be4you.password_login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.core.data.repositories.AuthRepository
import co.be4you.core.domain.utils.LoginThrowable
import co.be4you.core.domain.utils.login.LoginHandler
import co.be4you.core.navigation.AppNavigator
import co.be4you.core.navigation.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PasswordHandler(
    private val authRepository: AuthRepository,
    private val appNavigator: AppNavigator,
) : LoginHandler<PasswordLoginUiState, PasswordLoginUiAction> {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override var state = MutableStateFlow(
        PasswordLoginUiState(
            username = "",
            password = "",
            isPasswordVisible = false,
            isButtonLoading = false,
            errorResource = null
        )
    )

    override fun execute(action: PasswordLoginUiAction) {
        when (action) {
            PasswordLoginUiAction.OnLoginClicked -> scope.launch {
                state.update { it.copy(isButtonLoading = true) }
                authRepository.login(
                    username = state.value.username,
                    password = state.value.password,
                ).fold(
                    onSuccess = {
                        appNavigator.navigateTo(
                            route = Route.Dashboard,
                            removeRoutes = listOf(Route.Login),
                        )
                        state.update {
                            it.copy(
                                errorResource = null,
                                isButtonLoading = false,
                            )
                        }
                    },
                    onFailure = { error ->
                        val errorMessageResource = when (error) {
                            is LoginThrowable.IncorrectEmailPassword -> co.be4you.core.R.string.invalid_email_password
                            else -> co.be4you.core.R.string.generic_error_message
                        }
                        state.update {
                            it.copy(
                                errorResource = errorMessageResource,
                                isButtonLoading = false,
                            )
                        }
                    }
                )
            }

            is PasswordLoginUiAction.OnPasswordChanged -> {
                state.update { it.copy(password = action.password) }
            }

            PasswordLoginUiAction.OnPasswordVisibilityChanged -> {
                state.update { it.copy(isPasswordVisible = state.value.isPasswordVisible.not()) }
            }

            PasswordLoginUiAction.OnRegisterClicked -> {
                appNavigator.navigateTo(
                    route = Route.Registration,
                    removeRoutes = listOf(Route.Login),
                )
            }

            is PasswordLoginUiAction.OnUsernameChanged -> {
                state.update { it.copy(username = action.username) }
            }
        }
    }

    @Composable
    override fun LoginLayout(
        modifier: Modifier,
    ) {
        val state by state.collectAsStateWithLifecycle()
        PasswordLoginLayout(
            modifier = modifier,
            state = state,
            onAction = ::execute
        )
    }

    override
    val buttonTextResource: Int
        get() = R.string.login_with_password
    override val titleTextResource: Int
        get() = co.be4you.core.R.string.login
}
