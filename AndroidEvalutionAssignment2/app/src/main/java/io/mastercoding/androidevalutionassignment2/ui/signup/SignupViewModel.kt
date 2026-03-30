package io.mastercoding.androidevalutionassignment2.ui.signup

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import io.mastercoding.androidevalutionassignment2.data.local.entity.UserEntity
import io.mastercoding.androidevalutionassignment2.data.local.entity.UserRole
import io.mastercoding.androidevalutionassignment2.data.remote.AuthRepository
import io.mastercoding.androidevalutionassignment2.data.repository.AuthRepositoryImpl
import io.mastercoding.androidevalutionassignment2.data.repository.CompanyRepositoryImpl
import io.mastercoding.androidevalutionassignment2.data.repository.UserRepository
import io.mastercoding.androidevalutionassignment2.data.repository.UserRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignupViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val authRepositoryy = AuthRepositoryImpl()
    private lateinit var userRepository: UserRepository

    fun init(context: Context) {
        userRepository = UserRepositoryImpl(context)
    }


    companion object {
        private const val PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{9,}$"
    }

   //Input field
    private val _username = MutableLiveData("")
    val username: LiveData<String> = _username

    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _company = MutableLiveData("")
    val company: LiveData<String> = _company

    //validate state
    private val _isPasswordValid = MutableLiveData(false)
    val isPasswordValid: LiveData<Boolean> = _isPasswordValid

    private val _isFormValid = MutableLiveData(false)
    val isFormValid: LiveData<Boolean> = _isFormValid

   //Result state
    private val _signupSuccess = MutableLiveData<Boolean>()
    val signupSuccess: LiveData<Boolean> = _signupSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    //Input handling
    fun onUsernameChanged(input: String) {
        _username.value = input.trim()
        validateForm()
    }

    fun onEmailChanged(input: String) {
        _email.value = input.trim()
        validateForm()
    }

    fun onPasswordChanged(input: String) {
        _password.value = input
        validatePassword(input)
        validateForm()
    }

    fun onCompanySelected(selected: String) {
        _company.value = selected
        validateForm()
    }

   //validation
    private fun validatePassword(password: String) {
        val isValid = Regex(PASSWORD_REGEX).matches(password)
        _isPasswordValid.value = isValid
    }

    private fun validateForm() {
        val usernameValid = !_username.value.isNullOrEmpty()
        val emailValid = !_email.value.isNullOrEmpty()
        val passwordValid = _isPasswordValid.value == true
        val companyValid = !_company.value.isNullOrEmpty()

        _isFormValid.value =
            usernameValid && emailValid && passwordValid && companyValid
    }

    //signup actin
    fun signup() {
        val emailValue = email.value ?: return
        val passwordValue = password.value ?: return
        val usernameValue = username.value ?: return
        val companyValue = company.value ?: return

        viewModelScope.launch {
            val result = authRepository.signup(emailValue, passwordValue)

            result.fold(
                onSuccess = {
                    val firebaseUser = FirebaseAuth.getInstance().currentUser

                    firebaseUser?.let {

                        val role = if (emailValue == "admin@admin.com") {
                            UserRole.ADMIN
                        } else {
                            UserRole.USER
                        }

                        val userEntity = UserEntity(
                            uid = firebaseUser.uid,
                            username = usernameValue,
                            email = emailValue,
                            company = companyValue,
                            role = role
                        )

//                        val userEntity = UserEntity(
//                            uid = it.uid,
//                            username = usernameValue,
//                            email = emailValue,
//                            company = companyValue,
//                            role = UserRole.USER
 //                       )


                        viewModelScope.launch(Dispatchers.IO) {
                            userRepository.insertUser(userEntity)
                        }

                        _signupSuccess.value = true
                    }
                },
                onFailure = { error ->
                    _errorMessage.value = error.message
                }
            )
        }
    }
    private val companyRepository = CompanyRepositoryImpl()

    private val _companies = MutableLiveData<List<String>>()
    val companies: LiveData<List<String>> = _companies

    private val _companyError = MutableLiveData<String?>()
    val companyError: LiveData<String?> = _companyError

    fun fetchCompanies() {
        viewModelScope.launch {
            val result = companyRepository.getCompanies()
            result.fold(
                onSuccess = {
                    _companies.value = it
                },
                onFailure = {
                    _companyError.value = it.message
                }
            )
        }
    }
}
