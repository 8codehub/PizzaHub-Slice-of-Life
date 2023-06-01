package com.dev.pizzahub.presentation.menu_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.pizzahub.R
import com.dev.pizzahub.domain.model.MenuListItem

// Adapter class for the RecyclerView of menu items.
class MenuListAdapter(
    // Callback function to be invoked when a menu item is clicked.
    private val onItemClicked: (MenuListItem) -> Unit
) : RecyclerView.Adapter<MenuListAdapter.MenuListViewHolder>() {

    // List of menu items to be displayed.
    private var menuItems: List<MenuListItem> = emptyList()

    // List of selected items.
    private val selectedItems = mutableListOf<MenuListItem>()

    // Create new views (invoked by the layout manager).
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_list, parent, false)
        return MenuListViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager).
    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int) {
        val menuItem = menuItems[position]
        holder.bind(menuItem)
    }

    // Return the size of your dataset (invoked by the layout manager).
    override fun getItemCount(): Int = menuItems.size

    // Update the list of menu items and notify the adapter to refresh the RecyclerView.
    fun setItems(items: List<MenuListItem>) {
        menuItems = items
        notifyDataSetChanged()
    }

    // Provide a reference to the views for each data item.
    inner class MenuListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.itemPriceTextView)

        init {
            itemView.setOnClickListener {
                val menuItem = menuItems[adapterPosition]
                if (selectedItems.contains(menuItem)) {
                    selectedItems.remove(menuItem)
                    itemView.isSelected = false
                    onItemClicked(menuItem)
                } else if (selectedItems.size < 2) {
                    selectedItems.add(menuItem)
                    itemView.isSelected = true
                    onItemClicked(menuItem)
                }
            }
        }

        // Bind a menu item to this ViewHolder.
        fun bind(menuItem: MenuListItem) {
            nameTextView.text = menuItem.name
            priceTextView.text = "$${menuItem.price}"
            itemView.isSelected = selectedItems.contains(menuItem)
        }
    }
}