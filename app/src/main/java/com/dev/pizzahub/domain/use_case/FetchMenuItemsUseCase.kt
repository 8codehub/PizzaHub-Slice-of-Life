package com.dev.pizzahub.domain.use_case

import com.dev.pizzahub.domain.mapper.PizzaFlavorToMenuListItemMapper
import com.dev.pizzahub.domain.model.MenuListItem
import com.dev.pizzahub.domain.repository.IPizzaDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchMenuItemsUseCase @Inject constructor(private val repository: IPizzaDataRepository) {

    operator fun invoke(): Flow<Result<List<MenuListItem>>> = flow {
        val result = repository.fetchData()
        if (result.isSuccess) {
            emit(Result.success(PizzaFlavorToMenuListItemMapper.map(result.getOrNull())))
        } else {
            emit(Result.failure<List<MenuListItem>>(result.exceptionOrNull() ?: Exception("Something went wrong")))
        }
    }
}
