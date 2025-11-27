package co.be4you.indoorlocalization.viewmodel.login

sealed interface LoginAction {

    data class OnEmailChanged(val email: String) : LoginAction

    data class OnPasswordChanged(val password: String) : LoginAction

    data object OnPasswordVisibilityChanged : LoginAction

    data object OnLoginClicked : LoginAction

    data object OnRegisterClicked : LoginAction
}
