package com.dev.pizzahub.domain.model

data class Order(val totalPrice: Double, val items: List<OrderItem>)
