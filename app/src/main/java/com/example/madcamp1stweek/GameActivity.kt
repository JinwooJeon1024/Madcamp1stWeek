package com.example.madcamp1stweek

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import com.bumptech.glide.Glide
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.madcamp1stweek.databinding.ActivityGameBinding
import com.example.madcamp1stweek.ui.dashboard.DashboardViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import kotlin.math.max
import kotlin.math.min

data class Restaurant(
    val name: String,
    val imageUrl: String,
    val category: String
)

class GameActivity : AppCompatActivity() {
    private val balls = mutableListOf<ImageView>()
    private val animXList = mutableListOf<ValueAnimator>()
    private val animYList = mutableListOf<ValueAnimator>()
    private var isAnimationRunning = false  // 애니메이션이 실행 중인지 추적합니다.
    private lateinit var binding: ActivityGameBinding
    private val isSelected = BooleanArray(10) { false } // Initialize all as unselected
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loadRestaurantsFromJSON(this, "lotto.json")
        initializeImageButtons()
        setupDrawButton()
        setupBallsAnimation()
    }

    private fun initializeImageButtons() {
        val imageButtons = listOf(
            binding.imageButton1, // Replace with actual IDs from your layout
            binding.imageButton2, // ...
            binding.imageButton3,
            binding.imageButton4,
            binding.imageButton5,
            binding.imageButton6,
            binding.imageButton7,
            binding.imageButton8,
            binding.imageButton9,
            binding.imageButton10
        )

        imageButtons.forEachIndexed { index, imageButton ->
            imageButton.alpha = if(isSelected[index]) 1.0f else 0.5f
            imageButton.setOnClickListener {
                isSelected[index] = !isSelected[index]
                imageButton.alpha = if (isSelected[index]) 1.0f else 0.5f
            }
        }
    }

    private fun setupDrawButton() {
        binding.buttonDraw.setOnClickListener {
            if (!isAnimationRunning) {
                startBallsAnimation()
            } else {
                stopBallsAnimation()
                randomlySelectRestaurant()
            }
        }
    }
    private fun setupBallsAnimation() {
        val container = binding.containerBalls
        // Setup balls and their animations
        for (i in 1..10) {
            val ball = ImageView(this@GameActivity).apply {
                val gradientDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    colors = intArrayOf(0xFFE68A00.toInt(), 0xFFFFFFFF.toInt())  // 오렌지색 그라데이션
                    gradientType = GradientDrawable.RADIAL_GRADIENT  // 원형 그라데이션
                    gradientRadius = 200f  // 그라데이션 반경
                }

                // 검정색 테두리를 위한 동그란 ShapeDrawable 생성
                val strokeDrawable = ShapeDrawable(OvalShape()).apply {
                    paint.color = Color.GRAY // 검정색 설정
                    paint.style = Paint.Style.STROKE  // 테두리 스타일
                    paint.strokeWidth = 2f  // 테두리 두께
                }

                // 두 Drawable을 겹쳐서 LayerDrawable로 만듭니다.
                val layers = arrayOf<Drawable>(gradientDrawable, strokeDrawable)
                background = LayerDrawable(layers)

                post {
                    val maxX = container.width - width.toFloat()
                    val maxY = container.height - height.toFloat()
                    translationX = (Math.random() * maxX).toFloat()
                    translationY = (Math.random() * maxY).toFloat()
                }
                container.addView(this, ViewGroup.LayoutParams(dpToPx(50), dpToPx(50)))

            }
            setupAnimationForBall(ball, container)
            balls.add(ball)
        }
    }
    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
    // 애니메이션 생성 및 리스트에 추가
    private fun setupAnimationForBall(ball: ImageView, container: FrameLayout) {
        // Setup animations for ball and add to animXList and animYList
        val maxX = container.width - ball.width.toFloat()
        val maxY = container.height - ball.height.toFloat()

        var initialVelocityX = ((Math.random() - 0.5) * 20).toFloat()
        var initialVelocityY = ((Math.random() - 0.5) * 20).toFloat()

        val animX = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 50
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                var newX = ball.translationX + initialVelocityX

                if (newX <= 0f || newX >= maxX) {
                    initialVelocityX *= -1
                }
                ball.translationX += initialVelocityX
            }
        }
        val animY = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 50
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                var newY = ball.translationY + initialVelocityY

                if (newY <= 0f || newY >= maxY) {
                    initialVelocityY *= -1
                }
                ball.translationY = newY
            }
        }
        animXList.add(animX)
        animYList.add(animY)
    }
    private fun startBallsAnimation() {
        animXList.forEach { it.start() }
        animYList.forEach { it.start() }
        isAnimationRunning = true
    }

    private fun stopBallsAnimation() {
        animXList.forEach { it.cancel() }
        animYList.forEach { it.cancel() }
        balls.forEach { ball ->
            ball.animate().scaleX(1f).scaleY(1f).setDuration(300).start()
        }
        isAnimationRunning = false
    }
    private fun randomlySelectRestaurant() {
        val restaurants = viewModel.restaurants.value ?: return

        val selectedIndices = isSelected
            .mapIndexed { index, isSelected -> if (isSelected) index+1 else null }
            .filterNotNull() // This will remove any null entries, effectively mimicking mapIndexedNotNull

        if (selectedIndices.isNotEmpty()) {
            val selectedCategory = selectedIndices.random().toString()
            val filteredRestaurants = restaurants.filter { it.category == selectedCategory }

            if (filteredRestaurants.isNotEmpty()) {
                val selectedRestaurant = filteredRestaurants.random()
                showRestaurantDialog(selectedRestaurant)
            } else {
                Toast.makeText(this, "No restaurants found for the selected category.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please select at least one category.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showRestaurantDialog(restaurant: Restaurant) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_lotto, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.dialog_image)
        val nameTextView = dialogView.findViewById<TextView>(R.id.dialog_name)
        val categoryTextView = dialogView.findViewById<TextView>(R.id.dialog_category)

        nameTextView.text = restaurant.name
        categoryTextView.text = "Category: ${restaurant.category}"

        Glide.with(this)
            .load(restaurant.imageUrl)
            .error(R.drawable.imagenotfound)
            .into(imageView)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}

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