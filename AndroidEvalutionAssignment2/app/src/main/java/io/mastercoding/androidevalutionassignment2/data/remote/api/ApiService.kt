package io.mastercoding.androidevalutionassignment2.data.remote.api


import io.mastercoding.androidevalutionassignment2.data.remote.model.UserResponse
import retrofit2.http.GET

interface ApiService {

    @GET("users")
    suspend fun getUsers(): List<UserResponse>
}