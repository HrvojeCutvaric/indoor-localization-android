package co.be4you.indoorlocalization.viewmodel.registration

sealed interface RegistrationAction {

    data class OnEmailChanged(val newEmail: String) : RegistrationAction

    data class OnPasswordChanged(val newPassword: String) : RegistrationAction

    data class OnConfirmPasswordChanged(val newConfirmPassword: String) : RegistrationAction

    data object OnPasswordVisibilityChanged : RegistrationAction

    data object OnConfirmPasswordVisibilityChanged : RegistrationAction

    data object OnRegisterClicked : RegistrationAction

    data object OnLoginClicked : RegistrationAction
}
