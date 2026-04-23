package br.com.cesarsicas.androidmovieflix.domain.usecase

import br.com.cesarsicas.androidmovieflix.domain.model.UserModel
import br.com.cesarsicas.androidmovieflix.domain.repository.UserRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(): UserModel = userRepository.getProfile()
}
