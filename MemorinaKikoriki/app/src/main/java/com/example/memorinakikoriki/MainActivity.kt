package com.example.memorinakikoriki

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val images = listOf(
        R.drawable.barash, R.drawable.nyusha, R.drawable.karkarych, R.drawable.yozhik,
        R.drawable.pin, R.drawable.losyash, R.drawable.kopatych, R.drawable.sovuniya
    )
    private var firstCard: ImageView? = null
    private var secondCard: ImageView? = null
    private var isClickable = true
    private val openedPairs = mutableListOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.weight = 1f

        val shuffledImages = (images + images).shuffled()
        val catViews = shuffledImages.mapIndexed { _, imageRes ->
            ImageView(this).apply {
                setImageResource(R.drawable.card_back)
                layoutParams = params

                val border = GradientDrawable().apply {
                    setColor(0xFFFFFFFF.toInt())
                    setStroke(3, 0xFF808080.toInt())
                    cornerRadius = 8f
                }
                background = border

                tag = imageRes
                setOnClickListener(cardClickListener)

                setPadding(8, 8, 8, 8)
            }
        }

        val rows = Array(4) { LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER } }
        for ((index, view) in catViews.withIndex()) {
            rows[index / 4].addView(view)
        }
        for (row in rows) {
            layout.addView(row)
        }

        setContentView(layout)
    }

    private val cardClickListener = View.OnClickListener { clickedView ->
        if (!isClickable || openedPairs.contains(clickedView)) return@OnClickListener

        val clickedCard = clickedView as ImageView
        val imageRes = clickedCard.tag as Int

        GlobalScope.launch(Dispatchers.Main) {
            clickedCard.setImageResource(imageRes)

            if (firstCard == null) {
                firstCard = clickedCard
            } else if (secondCard == null) {
                secondCard = clickedCard
                isClickable = false
                delay(900)
                checkPair()
            }
        }
    }

    private fun checkPair() {
        if (firstCard?.tag == secondCard?.tag) {
            firstCard?.visibility = View.INVISIBLE
            secondCard?.visibility = View.INVISIBLE
            openedPairs.add(firstCard!!)
            openedPairs.add(secondCard!!)
        } else {
            firstCard?.setImageResource(R.drawable.card_back)

            secondCard?.setImageResource(R.drawable.card_back)
        }

        firstCard = null
        secondCard = null
        isClickable = true

        if (openedPairs.size == images.size * 2) {
            showWinMessage()
        }
    }

    private fun showWinMessage() {
        Toast.makeText(this, "Ты победил, молодец!", Toast.LENGTH_SHORT).show()
    }
}