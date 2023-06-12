package ru.versoit.todoapp.presentation.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView

class CustomTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialTextView(context, attrs, defStyleAttr) {
    private var lineFraction = 0f
    private val linePaint: Paint = Paint()

    private var isIncreasing = false
    private var isDecreasing = false

    init {
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 3f
        linePaint.isAntiAlias = true
        linePaint.color = currentTextColor
    }

    companion object {

        private const val HEIGHT_DIVIDER = 1.8f

        private const val DURATION_APPEAR_COEFFICIENT = 0.27886224f

        private const val DURATION_DISAPPEAR_COEFFICIENT = 0.05577f
    }

    private val textHeight get() = height / lineCount

    private val textWidth get() = paint.measureText(text.toString())

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var lineToDrawHeight = textHeight / HEIGHT_DIVIDER

        for (line in 1..lineCount) {

            val startX = 0f
            val startY = lineToDrawHeight
            val endX = textWidth * lineFraction
            val endY = lineToDrawHeight

            lineToDrawHeight += textHeight

            canvas.drawLine(startX, startY, endX, endY, linePaint)
        }
    }

    fun animateStrikeThrough() {
        isIncreasing = true
        isDecreasing = false

        val animator = ValueAnimator.ofFloat(lineFraction, 1f)
        animator.duration = (textWidth * DURATION_APPEAR_COEFFICIENT).toLong()

        animator.addUpdateListener { valueAnimator ->
            if (!isIncreasing) {
                animator.cancel()
                return@addUpdateListener
            }
            lineFraction = valueAnimator.animatedValue as Float
            invalidate()
        }

        animator.start()
    }

    fun animateRemoveStrikeThrough() {
        isIncreasing = false
        isDecreasing = true

        val animator = ValueAnimator.ofFloat(lineFraction, 0f)
        animator.startDelay = 0
        animator.duration = (textWidth * DURATION_DISAPPEAR_COEFFICIENT).toLong()

        animator.addUpdateListener { valueAnimator ->
            if (!isDecreasing) {
                animator.cancel()
                return@addUpdateListener
            }
            lineFraction = valueAnimator.animatedValue as Float
            invalidate()
        }

        animator.start()
    }
}