package io.mastercoding.androidevalutionassignment2

import android.app.Application
import androidx.room.Room
import io.mastercoding.androidevalutionassignment2.data.local.AppDatabase

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp

class MyApplication : Application() {

    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
