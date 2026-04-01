package io.mastercoding.androidevalutionassignment2.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mastercoding.androidevalutionassignment2.data.remote.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val  authRepository: AuthRepository
) : ViewModel() {

    companion object {
        private const val ADMIN_EMAIL = "admin@admin.com"
        private const val ADMIN_PASSWORD = "Admin@123"
        private const val PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{9,}$"
    }

    private var isPasswordTouched = false
    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError

    //Input State
    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    //Validation State

    private val _isFormValid = MutableLiveData(false)
    val isFormValid: LiveData<Boolean> = _isFormValid

    // -------- Login Result --------
    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    //Input Handlers
    fun onEmailChanged(input: String) {
        _email.value = input.trim()
        validateEmail(input)
    }

    fun onPasswordChanged(input: String) {
        _password.value = input
        isPasswordTouched = true

        validatePassword(input)
        validateForm()
    }



    //Validation Logic

    private fun validateEmail(email: String) {
        if (email.isBlank()) {
            _emailError.value = "Email is required"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailError.value = "The email address is badly formatted."
        } else {
            _emailError.value = null
        }
    }

    private fun validatePassword(password: String) {
        val regex =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$"

        if (!isPasswordTouched) {
            _passwordError.value = null
            return
        }

        if (password.isBlank()) {
            _passwordError.value = null
        } else if (!Regex(regex).matches(password)) {
            _passwordError.value =
                "Password must match:\n• 8 chars\n• 1 Upper\n• 1 Lower\n• 1 Number\n• 1 Special"
        } else {
            _passwordError.value = null
        }
    }

    private fun validateForm() {
        val emailValid = !_email.value.isNullOrEmpty()
        val passwordValid =! _password.value.isNullOrEmpty()
        _isFormValid.value = emailValid && passwordValid
    }

    //Login Action
    fun login() {
        val emailValue = _email.value ?: return
        val passwordValue = _password.value ?: return


        // Admin bypass
        if (emailValue == ADMIN_EMAIL && passwordValue == ADMIN_PASSWORD) {
            _loginSuccess.value = true
            return
        }


        // Firebase Authentication (via Repository)
        viewModelScope.launch {
            val result = authRepository.login(emailValue, passwordValue)
            result.fold(
                onSuccess = {
                    _loginSuccess.value = true
                },
                onFailure = { error ->
                    _errorMessage.value = error.message
                }
            )
        }
    }
}
