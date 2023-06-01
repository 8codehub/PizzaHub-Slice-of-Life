package com.dev.pizzahub.domain.use_case

import com.dev.pizzahub.domain.repository.IPizzaDataRepository
import javax.inject.Inject

class GetOrderUseCase @Inject constructor(private val repository: IPizzaDataRepository) {
    operator fun invoke() = repository.getOrder()
}
