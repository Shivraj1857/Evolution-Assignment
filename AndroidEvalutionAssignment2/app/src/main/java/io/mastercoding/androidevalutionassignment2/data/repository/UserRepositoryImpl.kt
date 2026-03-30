package io.mastercoding.androidevalutionassignment2.data.repository

import android.content.Context
import androidx.room.Room
import io.mastercoding.androidevalutionassignment2.MyApplication
import io.mastercoding.androidevalutionassignment2.data.local.AppDatabase
import io.mastercoding.androidevalutionassignment2.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(context: Context) : UserRepository {

//    private val database = Room.databaseBuilder(
//        context.applicationContext,
//        AppDatabase::class.java,
//        "app_database"
//    ).build()

   // private val userDao = database.userDao()
   val userDao = MyApplication.database.userDao()

    override suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    override fun getAllUsers(): Flow<List<UserEntity>> {
        return userDao.getAllUsers()
    }

    override suspend fun deleteUser(userId: Int) {
        userDao.deleteUserById(userId)
    }

    override suspend fun clearUsers() {
        userDao.clearUsers()
    }
}