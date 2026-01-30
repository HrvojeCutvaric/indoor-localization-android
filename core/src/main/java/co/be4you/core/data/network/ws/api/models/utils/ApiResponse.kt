package co.be4you.core.data.network.ws.api.models.utils

data class ApiResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T?,
    val errorCode: String?,
)
