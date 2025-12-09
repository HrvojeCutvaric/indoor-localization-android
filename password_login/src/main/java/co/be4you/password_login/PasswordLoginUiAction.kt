package co.be4you.password_login

import co.be4you.core.domain.utils.login.LoginUiAction

sealed interface PasswordLoginUiAction : LoginUiAction {
    data class OnUsernameChanged(val username: String) : PasswordLoginUiAction
    data class OnPasswordChanged(val password: String) : PasswordLoginUiAction
    data object OnPasswordVisibilityChanged : PasswordLoginUiAction
    data object OnLoginClicked : PasswordLoginUiAction
    data object OnRegisterClicked : PasswordLoginUiAction
}
