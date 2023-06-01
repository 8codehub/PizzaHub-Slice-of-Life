package com.dev.pizzahub.presentation.summary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.pizzahub.R
import com.dev.pizzahub.domain.model.OrderItem

class SummaryAdapter(private var items: List<OrderItem> = listOf()) : RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder>() {

    // ViewHolder class to hold the views for each item
    class SummaryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val priceTextView: TextView = view.findViewById(R.id.priceTextView)
    }

    // Create a new ViewHolder for the item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_summary, parent, false)
        return SummaryViewHolder(view)
    }

    // Bind data to the views in the ViewHolder
    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        val item = items[position]
        holder.nameTextView.text = item.name
        holder.priceTextView.text = "$${item.price}"
    }

    // Set the data for the adapter
    fun setData(items: List<OrderItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    // Return the number of items in the adapter
    override fun getItemCount() = items.size
}