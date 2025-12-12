package co.be4you.core.data.network.ws.api.mappers

import co.be4you.core.data.network.ws.api.models.auth.LoginResponseDto
import co.be4you.core.domain.models.LoginResponse

fun LoginResponseDto.toLoginResponse(): LoginResponse =
    LoginResponse(
        accessToken = accessToken.orEmpty(),
        refreshToken = refreshToken.orEmpty(),
        userId = userId ?: 0L,
        username = username.orEmpty(),
        email = email.orEmpty(),
    )
