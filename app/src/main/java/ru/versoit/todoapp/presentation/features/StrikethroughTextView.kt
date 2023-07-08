package ru.versoit.todoapp.presentation.features

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView
import kotlin.math.max
import kotlin.math.min

/**
 * Custom view with strikethrough option
 *
 * @property isAlphaAnimate Allows enable alpha animation.
 */
class StrikethroughTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialTextView(context, attrs, defStyleAttr) {
    private var lineFraction = 0f
    private val linePaint: Paint = Paint()

    private var isIncreasing = false
    private var isDecreasing = false

    var isAlphaAnimate = false

    init {
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = STROKE_WIDTH
        linePaint.isAntiAlias = true
        linePaint.color = currentTextColor
    }

    companion object {

        const val STROKE_WIDTH = 3f

        private const val HEIGHT_DIVIDER = 1.8f

        private const val APPEARANCE_DURATION = 480

        private const val DISAPPEARANCE_DURATION = 420

        private const val MIN_FRACTION = 0.2f
    }

    private val textHeight get() = height / lineCount

    private val textWidth get() = min(paint.measureText(text.toString()), width.toFloat())

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var lineToDrawHeight = textHeight / HEIGHT_DIVIDER

        repeat(lineCount) {

            val startX = 0f
            val startY = lineToDrawHeight
            val endX = textWidth * lineFraction
            val endY = lineToDrawHeight

            lineToDrawHeight += textHeight

            canvas.drawLine(startX, startY, endX, endY, linePaint)
        }
    }

    /**
     * Animates a left-to-right strikethrough animation on top of body text
     */
    fun animateStrikeThrough() {
        isIncreasing = true
        isDecreasing = false

        val animator = ValueAnimator.ofFloat(lineFraction, 1f)
        animator.duration = (textWidth / width * APPEARANCE_DURATION).toLong()

        animator.addUpdateListener { valueAnimator ->
            if (!isIncreasing) {
                animator.cancel()
                return@addUpdateListener
            }
            lineFraction = valueAnimator.animatedValue as Float
            if (isAlphaAnimate)
                alpha = max(1f - lineFraction, MIN_FRACTION)
            invalidate()
        }

        animator.start()
    }

    /**
     * Animates a right-to-left strikethrough remove animation of body text
     */
    fun animateRemoveStrikeThrough() {
        isIncreasing = false
        isDecreasing = true

        val animator = ValueAnimator.ofFloat(lineFraction, 0f)
        animator.startDelay = 0
        animator.duration = (textWidth / width * DISAPPEARANCE_DURATION).toLong()

        animator.addUpdateListener { valueAnimator ->
            if (!isDecreasing) {
                animator.cancel()
                return@addUpdateListener
            }
            lineFraction = valueAnimator.animatedValue as Float
            if (isAlphaAnimate)
                alpha = max(1f - lineFraction, MIN_FRACTION)
            invalidate()
        }

        animator.start()
    }
}