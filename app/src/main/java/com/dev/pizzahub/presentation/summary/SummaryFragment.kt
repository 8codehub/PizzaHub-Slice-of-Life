package com.dev.pizzahub.presentation.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.pizzahub.R
import com.dev.pizzahub.databinding.FragmentSummaryBinding
import com.dev.pizzahub.domain.model.Order
import com.dev.pizzahub.presentation.core.BaseBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SummaryFragment : BaseBindingFragment<FragmentSummaryBinding>() {
    private val viewModel: SummaryViewModel by viewModels()
    private val adapter = SummaryAdapter()

    override fun provideBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSummaryBinding {
        return FragmentSummaryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initLiveData()
    }

    private fun initView() {
        // Set up RecyclerView
        binding.rvSummary.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSummary.adapter = adapter
    }

    private fun initLiveData() {
        // Observe viewState and update UI
        lifecycleScope.launch {
            viewModel.viewState.collect { state ->
                updateList(state.data)
                updateLoading(state.isBlockingLoading)
                updateTotalPrice(state.data.totalPrice)
            }
        }
    }

    private fun updateTotalPrice(totalPrice: Double) {
        // Update the total price TextView with the formatted total price
        binding.totalPriceTextView.text = getString(R.string.total_price, totalPrice.toString())
    }

    private fun updateList(data: Order) {
        // Update the adapter with the new list of items
        adapter.setData(data.items)
    }

    private fun updateLoading(blockingLoading: Boolean) {
        // Update the visibility of the loading indicator
        binding.progressBar.visibility = if (blockingLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}