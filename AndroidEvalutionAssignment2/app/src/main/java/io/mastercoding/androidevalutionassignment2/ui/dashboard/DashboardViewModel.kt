package io.mastercoding.androidevalutionassignment2.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mastercoding.androidevalutionassignment2.data.local.entity.UserEntity
import io.mastercoding.androidevalutionassignment2.data.local.entity.UserRole
import io.mastercoding.androidevalutionassignment2.data.remote.AuthRepository
import io.mastercoding.androidevalutionassignment2.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val loggedInUserEmail: String
) : ViewModel() {

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    // Mode / Role

    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

    // Search

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Users from Room (single source of truth)

    private val usersFromDb: Flow<List<UserEntity>> =
        userRepository.getAllUsers()

    //Filtered Users (Search applied)

    val users: StateFlow<List<UserEntity>> =
        combine(usersFromDb, _searchQuery) { users, query ->
            if (query.isBlank()) {
                users
            } else {
                users.filter {
                    it.username.contains(query, ignoreCase = true) ||
                            it.email.contains(query, ignoreCase = true) ||
                            it.company.contains(query, ignoreCase = true)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // Init

    init {
        determineUserMode()
    }

    private fun determineUserMode() {
        viewModelScope.launch {
            userRepository.getAllUsers().collect { users ->
                val currentUser = users.find {
                    it.email.trim().equals(
                        loggedInUserEmail.trim(),
                        ignoreCase = true
                    )
                }


                _currentUser.value = currentUser
                _isAdminMode.value =
                    currentUser?.role == UserRole.ADMIN
            }
        }
    }

    // -------------------------------
    // Search Handler
    // -------------------------------

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    // -------------------------------
    // Delete User (Admin only)
    // -------------------------------

    fun deleteUser(userId: Int) {
        if (!_isAdminMode.value) return

        //viewModelScope.launch {

        viewModelScope.launch(Dispatchers.IO) {

        userRepository.deleteUser(userId)
        }
    }

    // -------------------------------
    // Logout
    // -------------------------------

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            //userRepository.clearUsers()
        }
    }
}