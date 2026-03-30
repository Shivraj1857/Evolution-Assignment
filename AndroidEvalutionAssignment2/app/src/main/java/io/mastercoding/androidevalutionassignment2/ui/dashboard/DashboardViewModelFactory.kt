package io.mastercoding.androidevalutionassignment2.ui.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.mastercoding.androidevalutionassignment2.data.repository.AuthRepositoryImpl
import io.mastercoding.androidevalutionassignment2.data.repository.UserRepositoryImpl

class DashboardViewModelFactory(
    private val context: Context,
    private val email: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(
                userRepository = UserRepositoryImpl(context),
                authRepository = AuthRepositoryImpl(),
                loggedInUserEmail = email
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}