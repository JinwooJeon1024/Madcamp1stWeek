package com.example.madcamp1stweek.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.madcamp1stweek.R

class AddRestaurantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_restaurant)

        // '저장' 버튼을 눌렀을 때의 로직
        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val phoneEditText = findViewById<EditText>(R.id.phoneEditText)
        val imageUrlEditText = findViewById<EditText>(R.id.imageUrlEditText)
        val addressEditText = findViewById<EditText>(R.id.addressEditText)

        saveButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.nameEditText).text.toString()
            val phoneNumber = findViewById<EditText>(R.id.phoneEditText).text.toString()
            val imageUrl = imageUrlEditText.text.toString()
            val address = findViewById<EditText>(R.id.addressEditText).text.toString()

            // 결과를 HomeFragment로 보내기
            val data = Intent()
            data.putExtra("name", name)
            data.putExtra("phoneNumber", phoneNumber)
            data.putExtra("imageUrl", imageUrl)
            data.putExtra("address", address)

            setResult(Activity.RESULT_OK, data)

            val inflater = layoutInflater
            val layout = inflater.inflate(R.layout.custom_toast, null)

            val text: TextView = layout.findViewById(R.id.custom_toast_message)
            text.text = "저장되었습니다 !"

            with(Toast(applicationContext)) {
                setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0, 860)
                duration = Toast.LENGTH_SHORT
                view = layout
                show()
            }
            finish()
        }
    }
}
