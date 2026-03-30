package io.mastercoding.androidevalutionassignment2.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val uid: String,          // Firebase UID
    val username: String,
    val email: String,
    val company: String,

    val role: UserRole        // ADMIN or USER
)