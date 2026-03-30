package io.mastercoding.androidevalutionassignment2.data.remote.model



data class UserResponse(
    val id: Int,
    val company: CompanyResponse
)

data class CompanyResponse(
    val name: String
)