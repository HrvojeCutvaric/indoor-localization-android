package co.be4you.core.domain.use_case

import co.be4you.core.data.repositories.AuthRepository
import co.be4you.core.domain.utils.Constants.MIN_PASSWORD_LENGTH
import co.be4you.core.domain.utils.RegisterThrowable
import co.be4you.core.domain.validators.EmailValidator

class RegisterUseCase(
    private val authRepository: AuthRepository,
) {

    suspend fun execute(
        email: String,
        password: String,
        confirmPassword: String,
    ): Result<Unit> {
        if (EmailValidator.isEmailValid(email).not()) {
            return Result.failure(RegisterThrowable.InvalidEmail)
        }

        if (password.length < MIN_PASSWORD_LENGTH) {
            return Result.failure(RegisterThrowable.WeakPassword)
        }

        if (password != confirmPassword) {
            return Result.failure(RegisterThrowable.ConfirmPasswordNotMatch)
        }

        return authRepository.register(
            email = email,
            password = password
        )
    }
}
