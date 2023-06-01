package com.dev.pizzahub.domain.repository

import com.dev.pizzahub.data.remot.dto.PizzaFlavor
import com.dev.pizzahub.domain.model.Order

interface IPizzaDataRepository {
    suspend fun fetchData(): Result<List<PizzaFlavor>>
    fun saveOrder(order: Order)
    fun getOrder(): Order
}
