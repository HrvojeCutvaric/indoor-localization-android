package co.be4you.indoorlocalization.viewmodel.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.indoorlocalization.navigation.Route
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(
        RegistrationState(
            email = "",
            password = "",
            confirmPassword = "",
            isPasswordVisible = false,
            isConfirmPasswordVisible = false,
            isButtonLoading = false,
            error = null,
        )
    )
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<MainAction>()
    val event = _event.asSharedFlow()

    fun execute(action: RegistrationAction) {
        when (action) {
            is RegistrationAction.OnConfirmPasswordChanged -> {
                _state.update {
                    it.copy(confirmPassword = action.newConfirmPassword)
                }
            }

            RegistrationAction.OnConfirmPasswordVisibilityChanged -> {
                _state.update {
                    it.copy(isConfirmPasswordVisible = it.isConfirmPasswordVisible.not())
                }
            }

            is RegistrationAction.OnEmailChanged -> {
                _state.update {
                    it.copy(email = action.newEmail)
                }
            }

            is RegistrationAction.OnPasswordChanged -> {
                _state.update {
                    it.copy(password = action.newPassword)
                }
            }

            RegistrationAction.OnPasswordVisibilityChanged -> {
                _state.update {
                    it.copy(isPasswordVisible = it.isPasswordVisible.not())
                }
            }

            RegistrationAction.OnRegisterClicked -> viewModelScope.launch(Dispatchers.IO) {
                _state.update {
                    it.copy(isButtonLoading = true)
                }

                val currentState = _state.value

                registerUseCase.execute(
                    email = currentState.email,
                    password = currentState.password,
                    confirmPassword = currentState.confirmPassword
                ).fold(
                    onSuccess = {
                        _state.update {
                            it.copy(
                                isButtonLoading = false,
                                error = null,
                            )
                        }
                        _event.emit(MainAction.NavigateTo(Route.Home))
                    },
                    onFailure = { throwable ->
                        _state.update {
                            it.copy(
                                isButtonLoading = false,
                                error = when (throwable) {
                                    RegisterThrowable.ConfirmPasswordNotMatch -> R.string.confirm_password_not_match
                                    RegisterThrowable.InvalidEmail -> R.string.invalid_email
                                    RegisterThrowable.EmailExists -> R.string.email_exists
                                    RegisterThrowable.WeakPassword -> R.string.weak_password
                                    else -> R.string.generic_error_message
                                }
                            )
                        }
                    }
                )
            }

            RegistrationAction.OnLoginClicked -> viewModelScope.launch {
                _event.emit(MainAction.NavigateTo(Route.Login))
            }
        }
    }
}
