package com.example.madcamp1stweek.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "맛집 추천해드립니다"
    }
    val text: LiveData<String> = _text
}