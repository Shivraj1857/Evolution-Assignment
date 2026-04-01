package io.mastercoding.androidevalutionassignment2.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.mastercoding.androidevalutionassignment2.ui.login.LoginActivity

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = intent.getStringExtra("username") ?: "User"
        val email = intent.getStringExtra("email") ?: ""
        setContent {
            val dashboardViewModel: DashboardViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = DashboardViewModelFactory(
                    context = applicationContext,
                    email = email
                )
            )

            DashboardScreen(
                viewModel = dashboardViewModel,
                username = username,
                onLogout = {
                    startActivity(
                        Intent(this, LoginActivity::class.java).apply {
                            flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                    )
                }
            )
        }
    }
}