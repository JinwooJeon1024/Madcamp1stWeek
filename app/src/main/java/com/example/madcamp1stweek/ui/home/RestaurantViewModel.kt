package com.example.madcamp1stweek.ui.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RestaurantViewModel: ViewModel() {
    val loadedRestaurants = MutableLiveData<List<Restaurant>>()

    init {
        loadedRestaurants.value = listOf()
    }

    fun loadRestaurantsFromJSON(context: Context) {
        val jsonString = context.assets.open("restaurants.json").bufferedReader().use { it.readText() }
        val tempList = Gson().fromJson(jsonString, object : TypeToken<List<Restaurant>>() {}.type) as List<Restaurant>
        val sortedList = tempList.sortedBy { it.name }
        // LiveData 업데이트
        loadedRestaurants.value = sortedList
    }

    fun addRestaurant(newRestaurant: Restaurant) {
        val updatedList = ArrayList(loadedRestaurants.value ?: emptyList())
        updatedList.add(newRestaurant)
        updatedList.sortBy { it.name } // Or any other sorting if necessary
        loadedRestaurants.value = updatedList
    }
    fun updateRestaurant(updatedRestaurant: Restaurant) {
        val currentList = loadedRestaurants.value ?: return  // 현재 리스트를 가져옴
        val updatedList = currentList.toMutableList()
        // 해당 식당 찾기 및 업데이트
        val index = currentList.indexOfFirst { it.id == updatedRestaurant.id }
        if (index != -1) {
            updatedList[index] = updatedRestaurant
            updatedList.sortBy { it.name } // Or any other sorting if necessary
            loadedRestaurants.value = updatedList  // LiveData 업데이트
        }
    }
    fun deleteRestaurant(restaurantId: String) {
        val currentList = loadedRestaurants.value ?: return
        val updatedList = currentList.filterNot { it.id == restaurantId }
        loadedRestaurants.value = updatedList
    }
}
