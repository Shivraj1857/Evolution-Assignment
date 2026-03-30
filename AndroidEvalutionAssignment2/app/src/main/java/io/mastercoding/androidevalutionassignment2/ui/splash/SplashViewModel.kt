package io.mastercoding.androidevalutionassignment2.ui.splash

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth



class SplashViewModel : ViewModel() {

    fun isUserLoggedIn(): Boolean {

        return FirebaseAuth.getInstance().currentUser != null
    }
}