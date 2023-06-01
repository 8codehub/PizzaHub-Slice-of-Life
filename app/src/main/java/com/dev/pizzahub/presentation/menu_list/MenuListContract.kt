package com.dev.pizzahub.presentation.menu_list

import com.dev.pizzahub.domain.model.MenuListItem
import com.dev.pizzahub.domain.model.Order
import com.dev.pizzahub.presentation.core.ButtonState
import com.dev.pizzahub.presentation.core.Intent
import com.dev.pizzahub.presentation.core.SingleEvent
import com.dev.pizzahub.presentation.core.State

data class MenuListState(
    val data: List<MenuListItem>,
    val dataErrorMessage: String?,
    val confirmButtonState: ButtonState,
    val isBlockingLoading: Boolean,
    val selectedMenuItems: List<MenuListItem>,
    val order: Order
) : State

sealed interface MenuListIntent : Intent {
    object LoadData : MenuListIntent
    object ValidateConfirmButtonState : MenuListIntent
    object CalculateTotalPizzaPrice : MenuListIntent
    object UpdatePreviewPrice : MenuListIntent
    data class DataReady(val menuListItems: List<MenuListItem>) : MenuListIntent
    data class DataFetchError(val error: String) : MenuListIntent
    data class MenuListItemClick(val item: MenuListItem) : MenuListIntent
    object ConfirmButtonClick : MenuListIntent
}

sealed interface MenuListSingleEvent : SingleEvent {
    data class NavigateSummaryScreen(val order: Order) : MenuListSingleEvent
}
