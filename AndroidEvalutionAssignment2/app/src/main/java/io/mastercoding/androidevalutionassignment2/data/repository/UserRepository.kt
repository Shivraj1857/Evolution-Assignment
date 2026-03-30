package io.mastercoding.androidevalutionassignment2.data.repository

import io.mastercoding.androidevalutionassignment2.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun insertUser(user: UserEntity)

    fun getAllUsers(): Flow<List<UserEntity>>

    suspend fun deleteUser(userId: Int)

    suspend fun clearUsers()
}