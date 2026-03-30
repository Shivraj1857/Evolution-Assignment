package io.mastercoding.androidevalutionassignment2.data.remote

interface AuthRepository {

    suspend fun login(
        email: String,
        password: String
    ): Result<Unit>


    suspend fun signup(
        email: String,
        password: String
    ): Result<Unit>


    suspend fun logout()
}