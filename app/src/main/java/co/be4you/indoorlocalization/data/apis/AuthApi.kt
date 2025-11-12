package co.be4you.indoorlocalization.data.apis

interface AuthApi {

    suspend fun register(email: String, password: String): Result<Unit>
}
