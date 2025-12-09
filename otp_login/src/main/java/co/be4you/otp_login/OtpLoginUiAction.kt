package co.be4you.otp_login

import co.be4you.core.domain.utils.login.LoginUiAction

sealed interface OtpLoginUiAction : LoginUiAction {
    data class OnEmailChanged(val email: String) : OtpLoginUiAction
    data class OnCodeChanged(val code: String) : OtpLoginUiAction
    data object OnRequestOtpClicked : OtpLoginUiAction
    data object OnVerifyOtpClicked : OtpLoginUiAction
    data object OnRegisterClicked : OtpLoginUiAction
}
