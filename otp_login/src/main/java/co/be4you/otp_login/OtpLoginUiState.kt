package co.be4you.otp_login

import co.be4you.core.domain.utils.login.LoginUiState

data class OtpLoginUiState(
    val email: String,
    val code: String,
    val isOtpSend: Boolean,
    val isButtonLoading: Boolean,
    val errorResource: Int?,
) : LoginUiState
