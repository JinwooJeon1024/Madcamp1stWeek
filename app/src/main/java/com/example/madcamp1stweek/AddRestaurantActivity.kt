package com.example.madcamp1stweek

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddRestaurantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_restaurant)

        // '저장' 버튼을 눌렀을 때의 로직
        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val phoneEditText = findViewById<EditText>(R.id.phoneEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val imageUrlEditText = findViewById<EditText>(R.id.imageUrlEditText)

        saveButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.nameEditText).text.toString()
            val phoneNumber = findViewById<EditText>(R.id.phoneEditText).text.toString()
            val description = descriptionEditText.text.toString()
            val imageUrl = imageUrlEditText.text.toString()

            // 결과를 HomeFragment로 보내기
            val data = Intent()
            data.putExtra("name", name)
            data.putExtra("phoneNumber", phoneNumber)
            data.putExtra("description", description)
            data.putExtra("imageUrl", imageUrl)

            setResult(Activity.RESULT_OK, data)

            Toast.makeText(this, "저장되었습니다 !", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
