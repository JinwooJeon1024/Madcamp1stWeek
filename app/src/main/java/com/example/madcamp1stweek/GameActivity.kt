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
import androidx.core.content.ContextCompat
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
                    colors = intArrayOf(0xFFFF982E.toInt(), 0xFFFFFFFF.toInt())  // 오렌지색 그라데이션
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
            }
            container.addView(ball, ViewGroup.LayoutParams(dpToPx(50), dpToPx(50)))

            ball.post {
                val maxX = container.width - ball.width.toFloat()
                val maxY = container.height - ball.height.toFloat()
                ball.translationX = (Math.random() * maxX).toFloat()
                ball.translationY = (Math.random() * maxY).toFloat()
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

        val animX = ValueAnimator.ofFloat().apply {
            duration = 100
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                var newX = ball.translationX + initialVelocityX

                if (newX <= 0f || newX >= maxX) {
                    initialVelocityX *= -1
                    newX = max(0f, min(newX, maxX))
                }
                ball.translationX = newX
            }
        }
        val animY = ValueAnimator.ofFloat().apply {
            duration = 100
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                var newY = ball.translationY + initialVelocityY

                if (newY <= 0f || newY >= maxY) {
                    initialVelocityY *= -1
                    newY = max(0f, min(newY, maxY))
                }
                ball.translationY = newY
            }
        }
        animXList.add(animX)
        animYList.add(animY)

    }
    private fun startBallsAnimation() {
        val container = binding.containerBalls
        balls.forEach { ball ->
            val maxX = container.width - ball.width.toFloat()
            val maxY = container.height - ball.height.toFloat()

            var velocityX = ((Math.random() - 0.5) * 80).toFloat() // 속도를 조절합니다.
            var velocityY = ((Math.random() - 0.5) * 80).toFloat() // 속도를 조절합니다.

            // X 방향 애니메이션 설정
            val animX = ValueAnimator.ofFloat(0f, 1f).apply {
                duration = 50 // 반응 속도를 빠르게 조절합니다.
                repeatCount = ValueAnimator.INFINITE
                addUpdateListener {
                    ball.translationX += velocityX
                    if (ball.translationX <= 0 || ball.translationX >= maxX) {
                        velocityX *= -1 // 방향 전환
                    }
                }
            }
            val animY = ValueAnimator.ofFloat(0f, 1f).apply {
                duration = 50 // 반응 속도를 빠르게 조절합니다.
                repeatCount = ValueAnimator.INFINITE
                addUpdateListener {
                    ball.translationY += velocityY
                    if (ball.translationY <= 0 || ball.translationY >= maxY) {
                        velocityY *= -1 // 방향 전환
                    }
                }
            }
            animX.start()
            animY.start()
            animXList.add(animX)
            animYList.add(animY)
        }
        isAnimationRunning = true
    }

    private fun stopBallsAnimation() {
        animXList.forEach { it.cancel() }
        animYList.forEach { it.cancel() }

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

    private fun getCategoryName(categoryNumber: String): String {
        return when (categoryNumber) {
            "1" -> "버거"
            "2" -> "회, 해물"
            "3" -> "피자"
            "4" -> "양식"
            "5" -> "중식"
            "6" -> "치킨"
            "7" -> "한식"
            "8" -> "일식"
            "9" -> "돈까스"
            "10" -> "분식"
            else -> "알 수 없는 카테고리"
        }
    }
    private fun showRestaurantDialog(restaurant: Restaurant) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_lotto, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.dialog_image)
        val nameTextView = dialogView.findViewById<TextView>(R.id.dialog_name)
        val categoryTextView = dialogView.findViewById<TextView>(R.id.dialog_category)

        nameTextView.text = "식당 이름 : ${(restaurant.name)}"
        categoryTextView.text = "음식 카테고리 : ${getCategoryName(restaurant.category)}"

        Glide.with(this)
            .load(restaurant.imageUrl)
            .error(R.drawable.imagenotfound)
            .into(imageView)

        // AlertDialog를 생성하고 설정합니다.
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // AlertDialog의 배경을 커스텀 drawable로 설정합니다.
        val background = ContextCompat.getDrawable(this, R.drawable.rounded_dialog)
        alertDialog.window?.apply{
            setBackgroundDrawable(background)
            attributes.windowAnimations = R.style.DialogAnimation
        }

        // AlertDialog를 표시합니다.
        alertDialog.show()

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