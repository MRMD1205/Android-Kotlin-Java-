package com.dynasty.interfaces

import android.view.View

/**
 * returns clicked view, item and its position in the list when clicked
 *
 * @param <T> the item class
 */
interface ItemClickCallback<T> {
    fun onItemClick(view: View, selectedItem: T, position: Int)
}