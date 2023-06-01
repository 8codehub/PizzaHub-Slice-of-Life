package com.dev.pizzahub.domain.mapper

import com.dev.pizzahub.data.remot.dto.PizzaFlavor
import com.dev.pizzahub.domain.model.MenuListItem

object PizzaFlavorToMenuListItemMapper {

    fun map(pizzaFlavors: List<PizzaFlavor>?): List<MenuListItem> {
        if (pizzaFlavors.isNullOrEmpty()) {
            return listOf()
        }

        return pizzaFlavors.mapIndexed { index, pizzaFlavor ->
            MenuListItem(id = index, price = pizzaFlavor.price, name = pizzaFlavor.name)
        }
    }
}
