package co.be4you.core.data.network.ws.api.utils

import co.be4you.core.data.network.ws.api.models.utils.ApiResponse

suspend inline fun <T> apiCall(
    crossinline call: suspend () -> ApiResponse<T>,
    errorMessage: String,
    nullDataMessage: String = "Data is null",
    crossinline onFailure: (ApiResponse<T>) -> Throwable = { Throwable(it.message ?: errorMessage) }
): Result<T> {
    try {
        val response = call()
        if (response.success.not()) return Result.failure(onFailure(response))
        val data = response.data ?: return Result.failure(Throwable(nullDataMessage))
        return Result.success(data)
    } catch (e: Exception) {
        return Result.failure(e)
    }
}

suspend inline fun apiCallUnit(
    crossinline call: suspend () -> ApiResponse<Unit>,
    errorMessage: String,
    crossinline onFailure: (ApiResponse<*>) -> Throwable = { Throwable(it.message ?: errorMessage) }
): Result<Unit> {
    try {
        val response = call()
        if (response.success.not()) return Result.failure(onFailure(response))
        return Result.success(Unit)
    } catch (e: Exception) {
        return Result.failure(e)
    }
}

suspend inline fun <T, R> apiCallMap(
    crossinline call: suspend () -> ApiResponse<T>,
    errorMessage: String,
    nullDataMessage: String = "Data is null",
    crossinline onFailure: (ApiResponse<T>) -> Throwable = {
        Throwable(it.message ?: errorMessage)
    },
    crossinline mapper: (T) -> R
): Result<R> = apiCall(call, errorMessage, nullDataMessage, onFailure).map(mapper)

suspend inline fun <T, R> apiCallListMap(
    crossinline call: suspend () -> ApiResponse<List<T>>,
    errorMessage: String,
    crossinline mapper: (T) -> R
): Result<List<R>> = apiCall(call, errorMessage).map { list -> list.map(mapper) }

