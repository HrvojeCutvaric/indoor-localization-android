package co.be4you.indoorlocalization.domain.utils

sealed class RegisterThrowable() : Throwable() {

    data object ConfirmPasswordNotMatch : RegisterThrowable()

    data object InvalidEmail : RegisterThrowable()

    data object WeakPassword : RegisterThrowable()

    data object EmailExists : RegisterThrowable()
}
