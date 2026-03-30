package io.mastercoding.androidevalutionassignment2.data.local


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.mastercoding.androidevalutionassignment2.data.local.converter.Converters
import io.mastercoding.androidevalutionassignment2.data.local.dao.UserDao
import io.mastercoding.androidevalutionassignment2.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}