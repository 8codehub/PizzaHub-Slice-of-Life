package com.dev.pizzahub.presentation.menu_list

import androidx.lifecycle.viewModelScope
import com.dev.pizzahub.domain.helper.NetworkHelper
import com.dev.pizzahub.domain.model.MenuListItem
import com.dev.pizzahub.domain.model.Order
import com.dev.pizzahub.domain.model.OrderItem
import com.dev.pizzahub.domain.use_case.FetchMenuItemsUseCase
import com.dev.pizzahub.domain.use_case.SaveOrderUseCase
import com.dev.pizzahub.presentation.core.BaseViewModel
import com.dev.pizzahub.presentation.core.ButtonState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MenuListViewModel @Inject constructor(
    private val fetchMenuItems: FetchMenuItemsUseCase, private val networkHelper: NetworkHelper, private val saveOrder: SaveOrderUseCase
) : BaseViewModel<MenuListState, MenuListIntent, MenuListSingleEvent>() {

    init {
        sendIntent(MenuListIntent.LoadData)
    }

    override suspend fun handleIntent(intent: MenuListIntent) {
        when (intent) {
            // Handle the intent to load data
            is MenuListIntent.LoadData                 -> handleLoadDataIntent()

            // Handle the intent when a menu item is clicked
            is MenuListIntent.MenuListItemClick        -> {
                sendIntent(MenuListIntent.ValidateConfirmButtonState)
                sendIntent(MenuListIntent.CalculateTotalPizzaPrice)
            }

            // Handle the intent to calculate the total pizza price
            is MenuListIntent.CalculateTotalPizzaPrice -> sendIntent(MenuListIntent.UpdatePreviewPrice)

            // Handle the intent when the confirm button is clicked
            is MenuListIntent.ConfirmButtonClick       -> {
                val order = viewState.value.order
                saveOrder(order)
                triggerSingleEvent(MenuListSingleEvent.NavigateSummaryScreen(order))
            }

            else                                       -> Unit
        }
    }

    override fun reduceIntent(intent: MenuListIntent, state: MenuListState) = when (intent) {
        // Reduce the intent to load data
        is MenuListIntent.LoadData                   -> state.copy(
            confirmButtonState = state.confirmButtonState.copy(enabled = false), isBlockingLoading = true
        )

        // Reduce the intent when the data is ready
        is MenuListIntent.DataReady                  -> state.copy(
            data = intent.menuListItems, isBlockingLoading = false
        )

        // Reduce the intent when there is a data fetch error
        is MenuListIntent.DataFetchError             -> state.copy(
            dataErrorMessage = intent.error, data = listOf(), isBlockingLoading = false
        )

        // Reduce the intent when a menu item is clicked
        is MenuListIntent.MenuListItemClick          -> state.copy(
            selectedMenuItems = updateSelectedMenuItem(state.selectedMenuItems, intent.item)
        )

        // Reduce the intent to validate the confirm button state
        is MenuListIntent.ValidateConfirmButtonState -> state.copy(
            confirmButtonState = state.confirmButtonState.copy(enabled = state.selectedMenuItems.isNotEmpty())
        )

        // Reduce the intent to calculate the total pizza price
        is MenuListIntent.CalculateTotalPizzaPrice   -> state.copy(
            order = calculateTotalPizzaPrice(state.selectedMenuItems)
        )

        else                                         -> state
    }

    private fun calculateTotalPizzaPrice(selectedMenuItems: List<MenuListItem>): Order {

        return when (selectedMenuItems.size) {
            1    -> {
                val item = selectedMenuItems[0]
                val orderItem = OrderItem(id = item.id, name = item.name, price = item.price)
                Order(item.price, listOf(orderItem))
            }
            2    -> {
                val orderItems = mutableListOf<OrderItem>()
                var totalPrice = 0.0
                for (item in selectedMenuItems) {
                    totalPrice += item.price / 2
                    orderItems.add(OrderItem(id = item.id, price = item.price / 2, name = item.name))
                }
                Order(totalPrice, orderItems)
            }
            else -> Order(0.0, listOf())
        }
    }

    private fun updateSelectedMenuItem(selectedMenuItems: List<MenuListItem>, item: MenuListItem): List<MenuListItem> {
        return if (selectedMenuItems.contains(item)) {
            selectedMenuItems.filterNot { it == item }
        } else {
            selectedMenuItems + item
        }
    }

    private fun handleLoadDataIntent() {
        if (networkHelper.isInternetAvailable()) {
            viewModelScope.launch(Dispatchers.IO) {
                fetchMenuItems().collect {
                    if (it.isSuccess) {
                        sendIntent(MenuListIntent.DataReady(it.getOrNull() ?: listOf()))
                    } else {
                        sendIntent(MenuListIntent.DataFetchError(it.exceptionOrNull()?.message.toString()))
                    }
                }
            }
        } else {
            sendIntent(MenuListIntent.DataFetchError("No Internet Connection"))
        }
    }

    override fun initState() = MenuListState(
        data = listOf(), dataErrorMessage = null, confirmButtonState = ButtonState(false), isBlockingLoading = false,
        selectedMenuItems = listOf(), order = Order(0.0, listOf())
    )
}
