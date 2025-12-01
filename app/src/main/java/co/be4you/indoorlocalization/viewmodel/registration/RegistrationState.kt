package co.be4you.indoorlocalization.viewmodel.registration

import androidx.annotation.StringRes

data class RegistrationState(
    val firstName: String,
    val lastName: String,
    val email: String,
    val username: String,
    val password: String,
    val confirmPassword: String,
    val isPasswordVisible: Boolean,
    val isConfirmPasswordVisible: Boolean,
    @StringRes val error: Int?,
    val isButtonLoading: Boolean,
)
