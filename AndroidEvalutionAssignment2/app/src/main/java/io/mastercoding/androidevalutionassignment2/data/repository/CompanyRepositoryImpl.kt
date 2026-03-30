package io.mastercoding.androidevalutionassignment2.data.repository

import io.mastercoding.androidevalutionassignment2.data.remote.RetrofitClient

class CompanyRepositoryImpl : CompanyRepository {

    override suspend fun getCompanies(): Result<List<String>> {
        return try {
            val users = RetrofitClient.apiService.getUsers()
            val companies = users.map { it.company.name }
            Result.success(companies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}