package io.mastercoding.androidevalutionassignment2.data.repository

interface CompanyRepository {
    suspend fun getCompanies(): Result<List<String>>
}