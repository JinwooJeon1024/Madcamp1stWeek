package com.example.madcamp1stweek.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.view.Gravity
import android.widget.TextView
import com.example.madcamp1stweek.R

class AddReviewActivity: AppCompatActivity() {

    //    private lateinit var imageUrlEditText: EditText
    private var selectedImageUri: String? = null

    // 새로운 방식의 Activity 결과를 처리하기 위한 register
    private val pickImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                // 이미지 URI를 문자열로 저장 (실제 앱에서는 필요에 따라 Uri 객체로 사용할 수 있음)
                selectedImageUri = uri.toString()

                // URI를 EditText에 표시 (또는 이미지를 직접 표시할 수도 있음)
//                imageUrlEditText.setText(selectedImageUri)
            }
        }
    }
    companion object {
        private const val REQUEST_CODE_READ_STORAGE = 101
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_READ_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여되었을 때
            } else {
                // 권한이 거부되었을 때
                Toast.makeText(this, "The app needs to read storage to function properly", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없으면 사용자에게 요청
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_READ_STORAGE
            )
        }
//        imageUrlEditText = findViewById<EditText>(R.id.imageUrlEditText)  // 여기에서 초기화
        val pickImageButton = findViewById<Button>(R.id.pickImageButton)
        pickImageButton.setOnClickListener {
            pickImageFromGallery()
        }

        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.nameEditText).text.toString()
            val rating = findViewById<EditText>(R.id.ratingEditText).text.toString()
            val content = findViewById<EditText>(R.id.contentEditText).text.toString()

            // 결과를 MainActivity로 보내기
            val data = Intent().apply {
                putExtra("name", name)
                putExtra("rating", rating)
                putExtra("content", content)
                putExtra("imageUrl", selectedImageUri ?: "")  // 선택된 이미지의 URI를 보냄
                Log.d("tag", "$selectedImageUri")
            }

            val inflater = layoutInflater
            val layout = inflater.inflate(R.layout.custom_toast, null)  // custom_toast_layout는 커스텀 레이아웃 파일 이름입니다.

            val text: TextView = layout.findViewById(R.id.custom_toast_message)
            text.text = "추가되었습니다 !"

            with (Toast(applicationContext)) {
                setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0, 940)
                duration = Toast.LENGTH_SHORT
                view = layout
                show()
            }
            finish()  // 액티비티 종료
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageResult.launch(intent)  // 이제 이 방식으로 갤러리를 여는 인텐트를 발생시킴
    }
}