package com.dev.pizzahub.presentation.menu_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.pizzahub.R
import com.dev.pizzahub.databinding.FragmentMenuListBinding
import com.dev.pizzahub.domain.model.MenuListItem
import com.dev.pizzahub.domain.model.Order
import com.dev.pizzahub.presentation.core.BaseBindingFragment
import com.dev.pizzahub.presentation.core.ButtonState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
// This is a Fragment that uses Dagger-Hilt and ViewBinding.
@AndroidEntryPoint
class MenuListFragment : BaseBindingFragment<FragmentMenuListBinding>() {

    // This ViewModel is provided by Dagger-Hilt.
    private val viewModel: MenuListViewModel by viewModels()

    // Create an instance of MenuListAdapter to manage the items in the RecyclerView.
    private val menuListAdapter = MenuListAdapter { menuListItem ->
        // Send clicked item to ViewModel.
        viewModel.sendIntent(MenuListIntent.MenuListItemClick(menuListItem))
    }

    // This function provides the ViewBinding object to the base class.
    override fun provideBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMenuListBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize the view and live data observers.
        initView()
        initLiveData()
    }

    private fun initView() {
        // Configure the RecyclerView and set its adapter.
        binding.rvPizzaList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPizzaList.adapter = menuListAdapter

        // Handle the click event on the confirm order button.
        binding.btnConfirmOrder.setOnClickListener {
            viewModel.sendIntent(MenuListIntent.ConfirmButtonClick)
        }
    }

    private fun initLiveData() {
        // Observe state from ViewModel and react accordingly.
        lifecycleScope.launch {
            viewModel.viewState.collect { state ->
                updateMenuItemList(state.data)
                updateConfirmButtonState(state.confirmButtonState)
                updateLoading(state.isBlockingLoading)
                updatePreviewTotalPrice(state.order)
                updateErrorMessage(state.dataErrorMessage)
            }
        }

        // Observe single intent events from ViewModel and react accordingly.
        lifecycleScope.launch {
            viewModel.singleIntent.collect {
                when (it) {
                    is MenuListSingleEvent.NavigateSummaryScreen -> {
                        // Navigate to the summary screen.
                        findNavController().navigate(R.id.action_MenuListFragment_to_SummaryFragment)
                    }
                }
            }
        }
    }

    // Function to display error message using Toast.
    private fun updateErrorMessage(message: String?) {
        message?.let {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    // Update total price in the UI.
    private fun updatePreviewTotalPrice(order: Order) {
        binding.tvTotalPrice.text = getString(R.string.total_price, order.totalPrice.toString())
    }

    // Update confirm button state (enabled/disabled).
    private fun updateConfirmButtonState(confirmButtonState: ButtonState) {
        binding.btnConfirmOrder.isEnabled = confirmButtonState.enabled
    }

    // Update loading spinner visibility.
    private fun updateLoading(blockingLoading: Boolean) {
        binding.progressBar.visibility = if (blockingLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    // Update the list of menu items in the RecyclerView.
    private fun updateMenuItemList(data: List<MenuListItem>) {
        menuListAdapter.setItems(data)
    }
}
