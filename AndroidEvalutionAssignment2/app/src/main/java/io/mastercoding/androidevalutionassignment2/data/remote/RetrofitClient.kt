package io.mastercoding.androidevalutionassignment2.data.remote

import io.mastercoding.androidevalutionassignment2.BuildConfig
import io.mastercoding.androidevalutionassignment2.data.remote.api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    //private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    //https://jsonplaceholder.typicode.com/users

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}