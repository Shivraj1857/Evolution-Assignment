package io.mastercoding.androidevalutionassignment2.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.mastercoding.androidevalutionassignment2.R
import io.mastercoding.androidevalutionassignment2.ui.dashboard.DashboardActivity
import io.mastercoding.androidevalutionassignment2.ui.login.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        Handler(Looper.getMainLooper()).postDelayed({
//            navigateNext()
//        }, 3000)
        lifecycleScope.launch {
            delay(3000)
            navigateNext()
        }
    }

    private fun navigateNext() {
        val intent = if (splashViewModel.isUserLoggedIn()) {
            Intent(this, DashboardActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}