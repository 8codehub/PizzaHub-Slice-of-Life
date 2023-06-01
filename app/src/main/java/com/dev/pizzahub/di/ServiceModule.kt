package com.dev.pizzahub.di

import com.dev.pizzahub.BuildConfig
import com.dev.pizzahub.data.remot.service.PizzaApiService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun providesRetrofit(): PizzaApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(PizzaApiService::class.java)
    }
}
