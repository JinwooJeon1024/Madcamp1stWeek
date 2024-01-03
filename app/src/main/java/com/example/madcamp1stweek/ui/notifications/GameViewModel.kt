package com.example.madcamp1stweek.ui.notifications

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class GameViewModel : ViewModel() {
    val restaurants = MutableLiveData<List<Restaurant>>()

    fun loadRestaurantsFromJSON(context: Context, fileName: String) {
        val jsonString = getJsonDataFromAsset(context, fileName)
        val typeToken = object : TypeToken<List<Restaurant>>() {}.type
        restaurants.value = Gson().fromJson(jsonString, typeToken)
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            null
        }
    }
}
