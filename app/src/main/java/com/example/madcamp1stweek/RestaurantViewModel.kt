package com.example.madcamp1stweek

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.madcamp1stweek.ui.home.HomeFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RestaurantViewModel: ViewModel() {
    // 식당 목록을 저장하는 뮤터블 리스트
    val loadedRestaurants = MutableLiveData<List<HomeFragment.Restaurant>>()
    fun loadRestaurantsFromJSON(context: Context) {
        // JSON 파일에서 식당 데이터 로드
        val jsonString = context.assets.open("restaurants.json").bufferedReader().use { it.readText() }
        val restaurantListType = object : TypeToken<List<HomeFragment.Restaurant>>() {}.type
        loadedRestaurants.value = Gson().fromJson(jsonString, restaurantListType)
    }
}
