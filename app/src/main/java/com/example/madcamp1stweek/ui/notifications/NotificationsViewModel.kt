package com.example.madcamp1stweek.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "식당 고르기 힘드시죠??\n" +
                "고민될 땐 !\n" +
                "뽑아드립니다 !\n\n" +
                "이 버튼을 눌러주세요"
    }
    val text: LiveData<String> = _text
}