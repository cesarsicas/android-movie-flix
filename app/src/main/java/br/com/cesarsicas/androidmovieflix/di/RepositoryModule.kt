package br.com.cesarsicas.androidmovieflix.di

import br.com.cesarsicas.androidmovieflix.data.repository.AuthRepositoryImpl
import br.com.cesarsicas.androidmovieflix.data.repository.TitleRepositoryImpl
import br.com.cesarsicas.androidmovieflix.data.repository.TransmissionRepositoryImpl
import br.com.cesarsicas.androidmovieflix.data.repository.UserRepositoryImpl
import br.com.cesarsicas.androidmovieflix.domain.repository.AuthRepository
import br.com.cesarsicas.androidmovieflix.domain.repository.TitleRepository
import br.com.cesarsicas.androidmovieflix.domain.repository.TransmissionRepository
import br.com.cesarsicas.androidmovieflix.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTitleRepository(impl: TitleRepositoryImpl): TitleRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindTransmissionRepository(impl: TransmissionRepositoryImpl): TransmissionRepository
}
