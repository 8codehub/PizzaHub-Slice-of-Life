package com.dev.pizzahub.presentation.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * BaseBindingFragment is an abstract class for Fragments with ViewBinding.
 *
 * @property T the type of ViewBinding.
 */
abstract class BaseBindingFragment<T : ViewBinding> : Fragment() {

    // Nullable ViewBinding variable.
    private var _binding: T? = null

    // Non-nullable ViewBinding variable. This will always hold a reference to our binding class.
    // It's also protected, which means it can be accessed by subclasses of BaseBindingFragment.
    protected val binding get() = _binding!!

    // Abstract function to provide ViewBinding. This function is implemented in the subclasses.
    abstract fun provideBinding(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Assign ViewBinding instance to _binding.
        _binding = provideBinding(inflater, container)

        // Return the root view of the layout associated with the binding.
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Nullify the ViewBinding reference when Fragment's view gets destroyed to avoid memory leaks.
        _binding = null
    }
}