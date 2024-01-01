package com.example.madcamp1stweek

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation

class GameActivity : AppCompatActivity() {
    private val balls = mutableListOf<ImageView>()
    private val animXList = mutableListOf<FlingAnimation>()
    private val animYList = mutableListOf<FlingAnimation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)  // 레이아웃 파일 설정

        // 공을 담을 통이 될 레이아웃
        val container = findViewById<ViewGroup>(R.id.containerBalls)
        val buttonDraw = findViewById<Button>(R.id.buttonDraw)

        buttonDraw.setOnClickListener {
            // 모든 애니메이션을 멈춥니다.
            animXList.forEach { it.cancel() }
            animYList.forEach { it.cancel() }

            // 랜덤하게 하나의 공을 선택합니다.
            val selectedBallIndex = balls.indices.random()
            val selectedBall = balls[selectedBallIndex]

            // 선택된 공을 강조 표시합니다 (예: 크기를 키웁니다).
            selectedBall.animate()
                .scaleX(1.5f)
                .scaleY(1.5f)
                .setDuration(300)
                .start()
        }

        // 10개의 주황색 공 생성
        for (i in 1..10) {
            val ball = ImageView(this).apply {
                // 주황색 원 모양의 ShapeDrawable 생성
                val shape = ShapeDrawable(OvalShape()).apply {
                    paint.color = 0xFFFFA500.toInt()  // 주황색 설정
                    setIntrinsicWidth(100)  // 공의 가로 크기를 설정합니다.
                    setIntrinsicHeight(100)  // 공의 세로 크기를 설정합니다.
                }

                // 레이아웃에 공 추가
                container.addView(this, ViewGroup.LayoutParams(100, 100))
            }

            // 공에 플링 애니메이션 적용
            val animX = FlingAnimation(ball, DynamicAnimation.X).apply {
                setStartVelocity(100f)  // 시작 속도
                friction = 1.1f  // 마찰 (값이 낮을수록 더 오래 튕김)
            }
            val animY = FlingAnimation(ball, DynamicAnimation.Y).apply {
                setStartVelocity(100f)  // 시작 속도
                friction = 1.1f  // 마찰
            }

            animXList.add(animX)
            animYList.add(animY)
            // 애니메이션 시작
            animX.start()
            animY.start()
        }

    }
}
