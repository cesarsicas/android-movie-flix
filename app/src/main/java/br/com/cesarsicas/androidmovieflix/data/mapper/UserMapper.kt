package br.com.cesarsicas.androidmovieflix.data.mapper

import br.com.cesarsicas.androidmovieflix.data.remote.dto.AuthResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.UserProfileDto
import br.com.cesarsicas.androidmovieflix.domain.model.UserModel

fun AuthResponseDto.toUserModel() = UserModel(
    id = userId,
    name = name,
    email = email,
    avatarUrl = avatarUrl,
    role = role,
)

fun UserProfileDto.toUserModel() = UserModel(
    id = id,
    name = name,
    email = email,
    avatarUrl = avatarUrl,
    role = role,
)
