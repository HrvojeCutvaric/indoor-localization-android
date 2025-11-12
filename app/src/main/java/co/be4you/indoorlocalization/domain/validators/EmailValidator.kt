package co.be4you.indoorlocalization.domain.validators

private const val REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"

object EmailValidator {
    fun isEmailValid(email: String): Boolean =
        Regex(REGEX_EMAIL).matches(email)
}
