package co.be4you.indoorlocalization.data.network.ws.mappers

import co.be4you.indoorlocalization.data.network.ws.models.LoginResponseDto
import co.be4you.indoorlocalization.domain.models.LoginResponse

fun LoginResponseDto.toLoginResponse(): LoginResponse =
    LoginResponse(
        accessToken = accessToken.orEmpty(),
        refreshToken = refreshToken.orEmpty(),
        userId = userId ?: 0L,
        username = username.orEmpty(),
        email = email.orEmpty(),
    )
