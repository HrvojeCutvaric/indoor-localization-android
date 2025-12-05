package co.be4you.core.domain.utils

sealed class LoginThrowable : Throwable() {

    data object IncorrectEmailPassword : LoginThrowable() {
        private fun readResolve(): Any = IncorrectEmailPassword
    }

    data object Generic : LoginThrowable() {
        private fun readResolve(): Any = Generic
    }

}
