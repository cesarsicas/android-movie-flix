package br.com.cesarsicas.androidmovieflix.domain.usecase

import br.com.cesarsicas.androidmovieflix.domain.repository.AuthRepository
import javax.inject.Inject

class AdminLoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.adminLogin(email, password)
}
