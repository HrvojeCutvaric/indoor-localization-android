package co.be4you.password_login

import co.be4you.core.domain.utils.login.LoginUiState

data class PasswordLoginUiState(
    val username: String,
    val password: String,
    val isPasswordVisible: Boolean,
    val isButtonLoading: Boolean,
    val errorResource: Int?,
) : LoginUiState
