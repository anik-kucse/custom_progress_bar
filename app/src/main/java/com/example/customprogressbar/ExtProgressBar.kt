package com.example.customprogressbar

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import kotlin.math.roundToInt

private val Int.dp: Int
    get() {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }

private val Float.sp: Float
    get() {
        return this * Resources.getSystem().displayMetrics.scaledDensity
    }

class ExtProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val trackDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.track_drawable, null)
    private val targetDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_target_end_light, null)
    private val progressTrackDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.progress_track, null)
    private val currentProgressDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_target_progress, null)
    private val textPaint = Paint()
    private val currentTextPaint = Paint()

    private var endTextSize: Float = 0f
    private var startOfEndText: Int = 0
    private var currentTextSize: Float = 0f
    private var itemSize: Int = 0
    private var targetItemSize: Int = 0

    private var progress = 1
    private var segment = 7

    init {
        textPaint.apply {
            color = Color.parseColor("#B3B3B3")
            isAntiAlias = true
            textSize = 12f.sp
        }
        currentTextPaint.apply {
            color = Color.parseColor("#1D1D1D")
            isAntiAlias = true
            textSize = 12f.sp
        }
    }

    fun inCreaseProgress() {
        progress++
        if (progress == segment)
            segment = progress + 7
        invalidate()
    }

    fun resetProgress() {
        progress = 1
        segment = 7
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null)
            return


        endTextSize = textPaint.measureText("$segment days")
        startOfEndText = (width - (endTextSize + 8.dp)).toInt()
        currentTextSize = currentTextPaint.measureText("$progress day")
        targetItemSize = (42.dp + endTextSize).roundToInt()
        itemSize = (42.dp + currentTextSize ).roundToInt()

        val remainingSegmentLength = width - (itemSize + targetItemSize)
        val gapSize = remainingSegmentLength / (segment - 2)

        // draw main track
        trackDrawable?.apply {
            setBounds(10.dp, 8.dp, width, 30.dp)
            draw(canvas)
        }

        // draw end text on top of the track
        canvas.drawText(
            "$segment days",
            width - (endTextSize + 8.dp),
            21.dp.toFloat(),
            textPaint
        )

        // draw target image
        targetDrawable?.apply {
            setBounds(
                startOfEndText - (34.dp),
                0,
                startOfEndText,
                34.dp
            )
            draw(canvas)
        }

        //draw progress background
        // todo this should be conditional

        progressTrackDrawable?.apply {
            setBounds(
                10.dp,
                8.dp,
                if (progress == 1)
                    itemSize
                else
                    itemSize + gapSize * (progress - 1),
                27.dp
            )
            draw(canvas)
        }

        // draw current progress text
        canvas.drawText(
            "$progress day",
            if (progress == 1)
                itemSize - (currentTextSize + 8.dp)
            else
                itemSize - (currentTextSize + 8.dp) + gapSize * (progress - 1),
            21.dp.toFloat(),
            currentTextPaint
        )

        // draw current progress icon drawable
        currentProgressDrawable?.apply {
            setBounds(
                if (progress == 1)
                    (itemSize - (currentTextSize + 8.dp) - 34.dp).toInt()
                else
                    (itemSize - (currentTextSize + 8.dp) - 34.dp).toInt() + gapSize * (progress - 1) ,
                0,
                if (progress == 1)
                    34.dp
                else
                    34.dp + gapSize * (progress - 1),
                34.dp)
            draw(canvas)
        }
    }
}