package com.dev.pizzahub.domain.use_case

import com.dev.pizzahub.domain.model.Order
import com.dev.pizzahub.domain.repository.IPizzaDataRepository
import javax.inject.Inject

class SaveOrderUseCase @Inject constructor(private val repository: IPizzaDataRepository) {
    operator fun invoke(order: Order) = repository.saveOrder(order)
}
