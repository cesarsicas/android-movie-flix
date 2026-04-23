package br.com.cesarsicas.androidmovieflix.domain.usecase

import br.com.cesarsicas.androidmovieflix.domain.model.UserModel
import br.com.cesarsicas.androidmovieflix.domain.repository.UserRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(name: String, avatarUrl: String?): UserModel =
        userRepository.updateProfile(name, avatarUrl)
}
