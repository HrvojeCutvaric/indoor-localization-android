package co.be4you.core.domain.utils

sealed class VerifyOtpThrowable : Throwable() {

    data object InvalidOtp : VerifyOtpThrowable()
}
