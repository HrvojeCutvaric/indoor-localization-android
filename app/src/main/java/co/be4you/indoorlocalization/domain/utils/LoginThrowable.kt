package co.be4you.indoorlocalization.domain.utils

sealed class LoginThrowable : Throwable() {

    data object IncorrectEmailPassword : LoginThrowable() {
        private fun readResolve(): Any = IncorrectEmailPassword
    }
}
