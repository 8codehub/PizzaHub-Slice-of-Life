package com.dev.pizzahub.data.repository

import com.dev.pizzahub.data.remot.dto.PizzaFlavor
import com.dev.pizzahub.data.remot.service.PizzaApiService
import com.dev.pizzahub.domain.model.Order
import com.dev.pizzahub.domain.repository.IPizzaDataRepository
import java.io.IOException

/**
 * PizzaDataRepository is the implementation of IPizzaDataRepository. It fetches data from the remote API
 * and saves and retrieves an order in local storage.
 *
 * @property pizzaApiService the API service to fetch the data.
 */
class PizzaDataRepository(private val pizzaApiService: PizzaApiService) : IPizzaDataRepository {

    // This variable stores the current order.
    private var order = Order(0.0, listOf())

    /**
     * This method fetches the data from the remote API service.
     *
     * @return a Result containing a list of PizzaFlavor or an exception if something goes wrong.
     */
    override suspend fun fetchData(): Result<List<PizzaFlavor>> {
        return try {
            val result = pizzaApiService.getPizzaFlavors() // Fetch the data from the API.

            // If the result is empty, throw an exception. Otherwise, return the result.
            if (result.isNullOrEmpty()) {
                Result.failure(Exception("Something went wrong"))
            } else {
                Result.success(result)
            }
        } catch (io: IOException) {
            // If there is an IOException, return it as the Result.
            Result.failure(io)
        }
    }

    /**
     * This method saves an order.
     *
     * @param order the order to be saved.
     */
    override fun saveOrder(order: Order) {
        this.order = order // Save the order.
    }

    /**
     * This method gets the current order.
     *
     * @return the current order.
     */
    override fun getOrder() = order
}