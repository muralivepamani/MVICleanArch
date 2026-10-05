package com.appwise.mvicleanarch.di

import com.appwise.mvicleanarch.data.repo.ProductRepoImpl
import com.appwise.mvicleanarch.domain.repo.ProductRepo
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
    abstract fun bindProductRepo(
        productRepoImpl: ProductRepoImpl
    ): ProductRepo
}
