package com.example.madcamp1stweek

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import kotlin.math.max
import kotlin.math.min

class GameActivity : AppCompatActivity() {
    private val balls = mutableListOf<ImageView>()
    private val animXList = mutableListOf<ValueAnimator>()
    private val animYList = mutableListOf<ValueAnimator>()
    private var isAnimationRunning = false  // 애니메이션이 실행 중인지 추적합니다.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val container = findViewById<FrameLayout>(R.id.containerBalls)
        val buttonDraw = findViewById<Button>(R.id.buttonDraw)

        val globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (container.width > 0 && container.height > 0) {
                    // 10개의 주황색 공 생성
                    for (i in 1..10) {
                        val ball = ImageView(this@GameActivity).apply {
                            val fillDrawable = ShapeDrawable(OvalShape()).apply {
                                paint.color = 0xFFFFA500.toInt()  // 주황색 설정
                            }

                            // 검정색 테두리를 위한 동그란 ShapeDrawable 생성
                            val strokeDrawable = ShapeDrawable(OvalShape()).apply {
                                paint.color = Color.BLACK  // 검정색 설정
                                paint.style = Paint.Style.STROKE  // 테두리 스타일
                                paint.strokeWidth = 2f  // 테두리 두께
                            }

                            // 두 Drawable을 겹쳐서 LayerDrawable로 만듭니다.
                            val layers = arrayOf(fillDrawable, strokeDrawable)
                            background = LayerDrawable(layers)

                            container.addView(this, ViewGroup.LayoutParams(100, 100))
                            post {
                                val maxX = container.width - width.toFloat()
                                val maxY = container.height - height.toFloat()
                                translationX = (Math.random() * maxX).toFloat()
                                translationY = (Math.random() * maxY).toFloat()
                            }
                        }


                        // 애니메이션 생성 및 리스트에 추가
                        val maxX = container.width - ball.width.toFloat()
                        val maxY = container.height - ball.height.toFloat()
                        val startX = Math.random().toFloat() * maxX
                        val startY = Math.random().toFloat() * maxY

                        var initialVelocityX = ((Math.random() - 0.5) * 20).toFloat()
                        var initialVelocityY = ((Math.random() - 0.5) * 20).toFloat()

                        val animX = ValueAnimator.ofFloat(startX, Math.random().toFloat() * maxX).apply {
                            duration = 100
                            repeatCount = ValueAnimator.INFINITE
                            addUpdateListener {
                                if (ball.translationX <= 0f || ball.translationX >= maxX) {
                                    initialVelocityX *= -1
                                }
                                ball.translationX += initialVelocityX
                            }
                        }
                        val animY = ValueAnimator.ofFloat(startY, Math.random().toFloat() * maxY).apply {
                            duration = 100
                            repeatCount = ValueAnimator.INFINITE
                            addUpdateListener {
                                if (ball.translationY <= 0f || ball.translationY >= maxY) {
                                    initialVelocityY *= -1
                                }
                                ball.translationY += initialVelocityY
                            }
                        }

                        animXList.add(animX)
                        animYList.add(animY)
                        balls.add(ball)
                    }

                    container.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        }
        container.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)

        buttonDraw.setOnClickListener {
            if (!isAnimationRunning) {
                animXList.forEach { it.start() }
                animYList.forEach { it.start() }
                isAnimationRunning = true
            } else {
                animXList.forEach { it.cancel() }
                animYList.forEach { it.cancel() }
                isAnimationRunning = false

                balls.forEach { ball ->
                    ball.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
            }
        }
    }
}