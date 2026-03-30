package io.mastercoding.androidevalutionassignment2.ui.login


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import io.mastercoding.androidevalutionassignment2.data.repository.AuthRepositoryImpl
import io.mastercoding.androidevalutionassignment2.databinding.ActivityLoginBinding
import io.mastercoding.androidevalutionassignment2.ui.dashboard.DashboardActivity
import io.mastercoding.androidevalutionassignment2.ui.signup.SignupActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(AuthRepositoryImpl())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInputListeners()
        observeViewModel()
        setupClicks()
    }

    // Input listeners → ViewModel
    private fun setupInputListeners() {
        binding.etEmail.addTextChangedListener(simpleWatcher {
            viewModel.onEmailChanged(it)
        })

        binding.etPassword.addTextChangedListener(simpleWatcher {
            viewModel.onPasswordChanged(it)
        })
    }

    // Click listeners
    private fun setupClicks() {
        binding.btnLogin.setOnClickListener {
            viewModel.login()

        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    // LiveData observers
    private fun observeViewModel() {

        // Enable / disable login button
        viewModel.isFormValid.observe(this) { isValid ->
            binding.btnLogin.isEnabled = isValid
        }

        // Password validation UI
        viewModel.isPasswordValid.observe(this) { isValid ->
            if (isValid) {
                clearPasswordError()
            } else {

                showPasswordError()
            }
        }

        // Login success → Dashboard
//        viewModel.loginSuccess.observe(this) { success ->
//            if (success) {
//                startActivity(Intent(this, DashboardActivity::class.java))
//                finish()
//            }
//        }

        viewModel.loginSuccess.observe(this) { success ->
            if (success) {
                val email = viewModel.email.value ?: ""

                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("email", email)

                startActivity(intent)
                finish()
            }
        }


        // Error message (Firebase failure)
        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                binding.tilPassword.error = it
            }
        }
    }

    //UI helpers
    private fun showPasswordError() {
        binding.tilPassword.error =
            "Password must match: >8 chars, 1 Upper, 1 Lower, 1 Number, 1 Special!"
        binding.tvPasswordError.visibility = View.VISIBLE
        setStrokeColor(binding.tilPassword, Color.RED)
    }

    private fun clearPasswordError() {
        binding.tilPassword.error = null
        binding.tvPasswordError.visibility = View.GONE
        setStrokeColor(binding.tilPassword, Color.GRAY)
    }

    private fun setStrokeColor(
        layout: TextInputLayout,
        color: Int
    ) {
        layout.boxStrokeColor = color
    }

    //Utility watcher
    private fun simpleWatcher(onTextChanged: (String) -> Unit): TextWatcher =
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged(s.toString())
            }
        }
}