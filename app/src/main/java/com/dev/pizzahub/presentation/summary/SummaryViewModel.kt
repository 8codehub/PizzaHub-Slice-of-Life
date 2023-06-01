package com.dev.pizzahub.presentation.summary

import androidx.lifecycle.viewModelScope
import com.dev.pizzahub.domain.model.Order
import com.dev.pizzahub.domain.use_case.GetOrderUseCase
import com.dev.pizzahub.presentation.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Summary screen.
 *
 * @param getOrder The use case for retrieving the order.
 */
@HiltViewModel
internal class SummaryViewModel @Inject constructor(
    private val getOrder: GetOrderUseCase
) : BaseViewModel<SummaryState, SummaryIntent, SummarySingleEvent>() {

    init {
        sendIntent(SummaryIntent.LoadData)
    }

    override suspend fun handleIntent(intent: SummaryIntent) {
        when (intent) {
            is SummaryIntent.LoadData -> handleLoadDataIntent()
            else                      -> Unit
        }
    }

    private fun handleLoadDataIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            val order = getOrder()
            sendIntent(SummaryIntent.DataReady(order))
        }
    }

    override fun reduceIntent(intent: SummaryIntent, state: SummaryState) = when (intent) {
        is SummaryIntent.LoadData  -> state.copy(
            isBlockingLoading = true
        )
        is SummaryIntent.DataReady -> state.copy(
            data = intent.order, isBlockingLoading = false
        )
        else                       -> state
    }

    override fun initState() = SummaryState(
        data = Order(0.0, listOf()), null, false
    )
}