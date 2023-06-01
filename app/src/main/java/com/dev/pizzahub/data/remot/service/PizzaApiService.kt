package com.dev.pizzahub.data.remot.service

import com.dev.pizzahub.data.remot.dto.PizzaFlavor
import retrofit2.http.GET

interface PizzaApiService {

    @GET("mobile/tests/pizzas.json")
    suspend fun getPizzaFlavors(): List<PizzaFlavor>?
}
