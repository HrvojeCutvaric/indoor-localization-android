package co.be4you.indoorlocalization.viewmodel.login

import androidx.lifecycle.ViewModel
import co.be4you.core.domain.utils.login.LoginHandler
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
    private val loginHandlers: List<LoginHandler<*, *>>
) : ViewModel() {

    private val _state = MutableStateFlow(
        LoginState(
            loginHandlers = loginHandlers,
            loginHandler = null,
        )
    )
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<MainAction>()
    val event = _event.asSharedFlow()

    fun execute(action: LoginAction) {
        when (action) {
            is LoginAction.OnLoginHandlerClicked -> {
                _state.value = _state.value.copy(loginHandler = action.loginHandler)
            }
        }
    }
}
