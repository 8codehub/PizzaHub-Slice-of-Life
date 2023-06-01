package com.dev.pizzahub.presentation.summary

import com.dev.pizzahub.domain.model.Order
import com.dev.pizzahub.presentation.core.Intent
import com.dev.pizzahub.presentation.core.SingleEvent
import com.dev.pizzahub.presentation.core.State


data class SummaryState(
    val data: Order,
    val dataErrorMessage: String?,
    val isBlockingLoading: Boolean,
) : State

sealed interface SummaryIntent : Intent {
    object LoadData : SummaryIntent
    data class DataReady(val order: Order) : SummaryIntent
}

sealed interface SummarySingleEvent : SingleEvent
