package com.example.madcamp1stweek.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "리뷰 모음집입니다"
    }
    val text: LiveData<String> = _text
}