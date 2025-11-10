package co.be4you.indoorlocalization.viewmodel.registration

data class RegistrationState(
    val email: String,
    val password: String,
    val confirmPassword: String,
    val isPasswordVisible: Boolean,
    val isConfirmPasswordVisible: Boolean,
    val isButtonLoading: Boolean,
)
