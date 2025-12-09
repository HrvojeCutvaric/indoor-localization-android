package co.be4you.otp_login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.core.data.repositories.AuthRepository
import co.be4you.core.domain.utils.VerifyOtpThrowable
import co.be4you.core.domain.utils.login.LoginHandler
import co.be4you.core.domain.validators.EmailValidator
import co.be4you.core.navigation.AppNavigator
import co.be4you.core.navigation.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OtpHandler(
    private val authRepository: AuthRepository,
    private val appNavigator: AppNavigator,
) : LoginHandler<OtpLoginUiState, OtpLoginUiAction> {
    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override var state = MutableStateFlow(
        OtpLoginUiState(
            email = "",
            code = "",
            isOtpSend = false,
            isButtonLoading = false,
            errorResource = null,
        )
    )

    override fun execute(action: OtpLoginUiAction) {
        when (action) {
            is OtpLoginUiAction.OnCodeChanged -> {
                state.update { it.copy(code = action.code) }
            }

            is OtpLoginUiAction.OnEmailChanged -> {
                state.update { it.copy(email = action.email) }
            }

            OtpLoginUiAction.OnVerifyOtpClicked -> scope.launch {
                state.update { it.copy(isButtonLoading = true) }
                authRepository.verifyOtp(
                    email = state.value.email,
                    code = state.value.code,
                ).fold(
                    onSuccess = {
                        state.update { it.copy(isButtonLoading = false, errorResource = null) }
                        appNavigator.navigateTo(Route.Dashboard)
                    },
                    onFailure = { error ->
                        val errorMessage = when (error) {
                            VerifyOtpThrowable.InvalidOtp -> R.string.invalid_otp_code
                            else -> co.be4you.core.R.string.generic_error_message
                        }

                        state.update {
                            it.copy(
                                isButtonLoading = false,
                                errorResource = errorMessage,
                            )
                        }
                    }
                )
            }

            OtpLoginUiAction.OnRegisterClicked -> {
                appNavigator.navigateTo(Route.Registration)
            }

            OtpLoginUiAction.OnRequestOtpClicked -> scope.launch {
                state.update { it.copy(isButtonLoading = true) }

                if (EmailValidator.isEmailValid(state.value.email).not()) {
                    state.update {
                        it.copy(
                            email = "",
                            isButtonLoading = false,
                            errorResource = co.be4you.core.R.string.invalid_email
                        )
                    }
                    return@launch
                }

                authRepository.requestOtp(
                    email = state.value.email,
                ).fold(
                    onSuccess = {
                        state.update {
                            it.copy(
                                isOtpSend = true,
                                isButtonLoading = false,
                                errorResource = null,
                            )
                        }
                    },
                    onFailure = {
                        state.update {
                            it.copy(
                                isOtpSend = false,
                                isButtonLoading = false,
                                errorResource = R.string.failed_to_send_otp_code,
                            )
                        }
                    }
                )
            }

            OtpLoginUiAction.OnChangeEmailClicked -> {
                state.update {
                    it.copy(
                        email = "",
                        code = "",
                        isOtpSend = false,
                        errorResource = null,
                    )
                }
            }
        }
    }

    @Composable
    override fun LoginLayout() {
        val state by state.collectAsStateWithLifecycle()
        OtpLayout(
            state = state,
            onAction = ::execute
        )
    }

    override val buttonTextResource: Int
        get() = R.string.login_with_one_time_code
}
