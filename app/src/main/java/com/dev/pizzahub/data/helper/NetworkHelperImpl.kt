package com.dev.pizzahub.data.helper

import android.content.Context
import com.dev.pizzahub.data.remot.util.*
import com.dev.pizzahub.domain.helper.NetworkHelper

class NetworkHelperImpl(private val context: Context) : NetworkHelper {
    override fun isInternetAvailable() = isInternetAvailable(context)
}
