package co.be4you.core.domain.use_case

import co.be4you.core.data.repositories.AuthRepository
import co.be4you.core.domain.utils.RegisterThrowable
import co.be4you.core.domain.validators.EmailValidator
import co.be4you.core.domain.validators.PasswordValidator

class RegisterUseCase(
    private val authRepository: AuthRepository,
) {

    suspend fun execute(
        firstName: String,
        lastName: String,
        email: String,
        username: String,
        password: String,
        confirmPassword: String,
    ): Result<Unit> {
        if (EmailValidator.isEmailValid(email).not()) {
            return Result.failure(RegisterThrowable.InvalidEmail)
        }

        if (PasswordValidator.isPasswordValid(password).not()) {
            return Result.failure(RegisterThrowable.WeakPassword)
        }

        if (password != confirmPassword) {
            return Result.failure(RegisterThrowable.ConfirmPasswordNotMatch)
        }

        return authRepository.register(
            firstName = firstName,
            lastName = lastName,
            email = email,
            username = username,
            password = password
        )
    }
}
