package com.dev.pizzahub.di

import com.dev.pizzahub.data.remot.service.PizzaApiService
import com.dev.pizzahub.data.repository.PizzaDataRepository
import com.dev.pizzahub.domain.repository.IPizzaDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun providePizzaDataRepository(networkService: PizzaApiService): IPizzaDataRepository {
        return PizzaDataRepository(networkService)
    }
}
