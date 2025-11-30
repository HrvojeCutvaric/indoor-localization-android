package co.be4you.indoorlocalization.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.data.apis.AuthApi
import co.be4you.indoorlocalization.domain.utils.LoginThrowable
import co.be4you.indoorlocalization.navigation.Route
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authApi: AuthApi,
) : ViewModel() {

    private val _state = MutableStateFlow(
        LoginState(
            email = "",
            password = "",
            isPasswordVisible = false,
            errorResource = null,
            isButtonLoading = false,
        )
    )
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<MainAction>()
    val event = _event.asSharedFlow()

    fun execute(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChanged -> {
                _state.value = _state.value.copy(email = action.email)
            }

            LoginAction.OnLoginClicked -> viewModelScope.launch(Dispatchers.IO) {
                _state.value = _state.value.copy(isButtonLoading = true)
                authApi.login(
                    email = _state.value.email,
                    password = _state.value.password
                ).fold(
                    onSuccess = {
                        _event.emit(MainAction.NavigateTo(Route.Dashboard))
                    },
                    onFailure = {
                        val errorMessageResource = when (it) {
                            is LoginThrowable.IncorrectEmailPassword -> R.string.invalid_email_password
                            else -> R.string.generic_error_message
                        }
                        _state.update { currentState ->
                            currentState.copy(
                                errorResource = errorMessageResource,
                                isButtonLoading = false
                            )
                        }
                    }
                )
            }

            is LoginAction.OnPasswordChanged -> {
                _state.value = _state.value.copy(password = action.password)
            }

            LoginAction.OnPasswordVisibilityChanged -> {
                _state.value =
                    _state.value.copy(isPasswordVisible = _state.value.isPasswordVisible.not())
            }

            LoginAction.OnRegisterClicked -> viewModelScope.launch(Dispatchers.IO) {
                _event.emit(MainAction.NavigateTo(Route.Registration))
            }
        }
    }
}
