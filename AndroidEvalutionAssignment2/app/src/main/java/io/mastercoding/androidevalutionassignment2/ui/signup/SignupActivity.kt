package io.mastercoding.androidevalutionassignment2.ui.signup

import android.R
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import io.mastercoding.androidevalutionassignment2.data.repository.AuthRepositoryImpl
import io.mastercoding.androidevalutionassignment2.databinding.ActivitySignupBinding
import io.mastercoding.androidevalutionassignment2.ui.login.LoginActivity


class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    private val viewModel: SignupViewModel by viewModels {
        SignupViewModelFactory(AuthRepositoryImpl())


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.init(applicationContext)
        viewModel.fetchCompanies()
        viewModel.companies.observe(this) { companyList ->

            // Safety check
            if (companyList.isNullOrEmpty()) return@observe

            val adapter = ArrayAdapter(
                this,
                R.layout.simple_spinner_item,
                companyList
            )

            adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
            )

            binding.spCompany.adapter = adapter
        }

        binding.spCompany.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedCompany =
                        parent.getItemAtPosition(position).toString()

                    viewModel.onCompanySelected(selectedCompany)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

        setupInputs()
        setupClicks()
        observeViewModel()
    }


    private fun setupInputs() {
        binding.etUsername.addTextChangedListener(watcher {
            viewModel.onUsernameChanged(it)
        })

        binding.etEmail.addTextChangedListener(watcher {
            viewModel.onEmailChanged(it)
        })

        binding.etPassword.addTextChangedListener(watcher {
            viewModel.onPasswordChanged(it)
        })
    }

    private fun setupClicks() {
        binding.btnSave.setOnClickListener {
            viewModel.signup()
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {

        viewModel.isFormValid.observe(this) {
            binding.btnSave.isEnabled = it
        }

        viewModel.isPasswordValid.observe(this) { valid ->
            binding.tvPasswordError.visibility =
                if (valid) View.GONE else View.VISIBLE
        }

        viewModel.signupSuccess.observe(this) { success ->
            if (success) showSuccessDialog()
        }

        viewModel.errorMessage.observe(this) { error ->
            binding.tilPassword.error = error
        }
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Success")
            .setMessage("User Registered Successfully")
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
                startActivity(
                    Intent(this, LoginActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            }
            .show()
    }

    private fun watcher(onChange: (String) -> Unit): TextWatcher =
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onChange(s.toString())
            }
        }
}