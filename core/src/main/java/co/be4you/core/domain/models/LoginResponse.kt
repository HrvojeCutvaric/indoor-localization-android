package co.be4you.core.domain.models

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: Long,
    val username: String,
    val email: String,
)
