package io.mastercoding.androidevalutionassignment2.data.local.converter

import androidx.room.TypeConverter
import io.mastercoding.androidevalutionassignment2.data.local.entity.UserRole

class Converters {

    @TypeConverter
    fun fromUserRole(role: UserRole): String {
        return role.name
    }

    @TypeConverter
    fun toUserRole(role: String): UserRole {
        return UserRole.valueOf(role)
    }
}