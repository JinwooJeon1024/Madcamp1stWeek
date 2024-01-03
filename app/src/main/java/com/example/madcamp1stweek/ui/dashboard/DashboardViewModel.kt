package com.example.madcamp1stweek.ui.dashboard

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DashboardViewModel : ViewModel() {
    val loadedReviews = MutableLiveData<MutableList<ItemData>>()

    init {
        loadedReviews.value = mutableListOf()
    }

    fun addReview(newReview: ItemData) {
        val updatedList = ArrayList(loadedReviews.value ?: mutableListOf())
        updatedList.add(newReview)
        updatedList.sortBy { it.name }
        loadedReviews.value = updatedList
    }

    fun loadReviews(context: Context) {
        val reviews = loadRestaurantsFromAssets(context)
        loadedReviews.value = reviews.toMutableList()
    }

    private fun loadRestaurantsFromAssets(context: Context): List<ItemData> {
        val jsonString = context.assets.open("review.json").bufferedReader().use { it.readText() }
        return Gson().fromJson(jsonString, object : TypeToken<List<ItemData>>() {}.type)
    }
}
