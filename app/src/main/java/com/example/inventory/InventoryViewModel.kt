/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item2
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch

/**
 * View Model untuk menyimpan referensi ke repositori Inventaris dan daftar terbaru semua item.
 *
 */
class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    /**
     * memasukan data baru ke databse.
     */
    fun addNewItem(itemName: String, itemPrice: String, itemCount: String, buyPrice: String) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount, buyPrice)
        insertItem(newItem)
    }

    /**
     * Meluncurkan coroutine baru untuk menyisipkan item
     */
    private fun insertItem(item: Item2) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    /**
     * Mengembalikan nilai true jika EditTexts tidak kosong
     */
    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String, buyPrice: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank() || buyPrice.isBlank()) {
            return false
        }
        return true
    }

    /**
     * Mengembalikan instance kelas entitas
     */
    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String, buyPrice: String): Item2 {
        return Item2(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt(),
            buyPrice = buyPrice.toDouble()
        )
    }
}

/**
 * Factory class untuk membuat instance [ViewModel].
 */
class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
