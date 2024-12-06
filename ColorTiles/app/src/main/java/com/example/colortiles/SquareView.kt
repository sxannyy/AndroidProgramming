package com.example.colortiles

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlin.math.min

class SquareView(ctx: Context) : View(ctx) {
    private var h = 0f
    private var w = 0f
    private var rectSize = 0f
    private val rectMargin = 50f
    private val field: Array<BooleanArray> = Array(4) { BooleanArray(4) { false } }
    private val paint = Paint()

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        h = (bottom - top).toFloat()
        w = (right - left).toFloat()

        val availableSize = min(h, w)
        rectSize = (availableSize - (rectMargin * (field.size + 1))) / field.size

        paint.color = Color.BLACK
        paint.strokeWidth = 3f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)

        val lenRow = field.size
        val lenCol = field[0].size
        var fl = true

        for (row in 0 until lenRow) {
            for (col in 0 until lenCol) {
                paint.color = if (field[row][col]) Color.GREEN else Color.RED
                if (!field[row][col]) {
                    fl = false
                }
                canvas.drawRect(
                    rectMargin + col * (rectSize + rectMargin),
                    rectMargin + row * (2 * rectSize + rectMargin),
                    rectMargin + col * (rectSize + rectMargin) + rectSize,
                    rectMargin + row * (2 * rectSize + rectMargin) + 2 * rectSize,
                    paint
                )
            }
        }
        if (fl) {
            Toast.makeText(context, "Вы победили!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x ?: return false
        val y = event?.y ?: return false

        if (event.action == MotionEvent.ACTION_DOWN) {
            val lenRow = field.size
            val lenCol = field[0].size

            for (row in 0 until lenRow) {
                for (col in 0 until lenCol) {
                    val left = rectMargin + col * (rectSize + rectMargin)
                    val top = rectMargin + row * (2 * rectSize + rectMargin)
                    val right = left + rectSize
                    val bottom = top + 2 * rectSize

                    if (x in left..right && y in top..bottom) {
                        Log.i("Нажатие", "Нажали на плитку: row=$row, col=$col")
                        for (i in 0 until lenRow) {
                            field[i][col] = !field[i][col]
                        }
                        for (j in 0 until lenCol) {
                            field[row][j] = !field[row][j]
                        }
                        invalidate()
                        return true
                    }
                }
            }
        }
        return true
    }
}
