package co.be4you.core.domain.validators

private const val REGEX_PASSWORD = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$"

object PasswordValidator {
    fun isPasswordValid(password: String): Boolean =
        Regex(REGEX_PASSWORD).matches(password)
}
