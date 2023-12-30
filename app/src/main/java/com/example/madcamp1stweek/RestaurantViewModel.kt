package com.example.madcamp1stweek

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.madcamp1stweek.ui.home.HomeFragment

class RestaurantViewModel: ViewModel() {
    // 식당 목록을 저장하는 뮤터블 리스트
    val loadedRestaurants = mutableListOf<HomeFragment.Restaurant>()

    // JSON 파일 또는 다른 데이터 소스로부터 식당 데이터를 로드하는 함수
    fun loadRestaurants(context: Context) {
        // 데이터 로딩 로직 (예: JSON 파일에서 식당 목록 로드)
    }
}
