package co.be4you.indoorlocalization.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.indoorlocalization.navigation.Route
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

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

            LoginAction.OnLoginClicked -> {
                // TODO: implement on login clicked
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
