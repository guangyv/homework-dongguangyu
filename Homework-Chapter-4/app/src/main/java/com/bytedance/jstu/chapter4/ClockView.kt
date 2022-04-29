package com.bytedance.jstu.chapter4

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 *  author : neo
 *  time   : 2021/10/25
 *  desc   :
 */
class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        val edgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            strokeWidth = 3f
            style = Paint.Style.STROKE
        }
        val numPaint = Paint(Paint.SUBPIXEL_TEXT_FLAG).apply {
            textSize = 72f
            color = Color.WHITE
        }
        val thickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            strokeWidth = 6f
        }
        val thinPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            strokeWidth = 3f
        }
        val hourPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            strokeWidth = 12f
        }
        val minPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            strokeWidth = 6f
        }
        val secPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            strokeWidth = 3f
        }
    }

    init {
        val tickHandler = Handler(Looper.getMainLooper())
        val runnable : Runnable = object: Runnable {
            override fun run() {
//                postInvalidate()
                invalidate()
                tickHandler.postDelayed(this, 1000)
            }
        }
        tickHandler.post(runnable)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        drawClockScale(canvas)
        drawClockNumber(canvas)
        drawClockPoints(canvas)
    }

    private fun drawClockScale(canvas: Canvas) {
        canvas.drawCircle(width / 2f, height / 2f, min(width, height) / 2f, edgePaint)
        for (i in 0..59) {
            if (i % 5 == 0) {
                canvas.drawLine(width / 2f, 0f, width / 2f, 30f, thickPaint)
            } else {
                canvas.drawLine(width / 2f, 0f, width / 2f, 15f, thinPaint)
            }
            canvas.rotate(6f, width / 2f, height / 2f) //以圆中心进行旋转
        }
    }

    private fun drawClockNumber(canvas: Canvas) {
        val clockNumbers = arrayOf("12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11")
        val degree:Double = 2*PI / clockNumbers.size
        val textBounds = Rect()
        for (i in clockNumbers.indices) {
            val num = clockNumbers[i]
            numPaint.getTextBounds(num, 0, num.length, textBounds)
            val x = width/2*(1+0.8*sin(degree*i)) - numPaint.measureText(num)/2
            val y = height/2*(1-0.8*cos(degree*i)) + textBounds.height()/2
            canvas.drawText(num, x.toFloat(), y.toFloat(), numPaint)
        }
    }

    private fun drawClockPoints(canvas: Canvas) {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR)
        val min = cal.get(Calendar.MINUTE)
        val second = cal.get(Calendar.SECOND)
        Log.d("TAG", "drawClockPoints: $hour $min $second")

        val hourAngle = hour / 12f * 360 + min / 60f * 30
        val minAngle = min / 60f * 360
        val secAngle = second / 60f * 360

        canvas.save()
        canvas.rotate(hourAngle, width / 2f, height / 2f)
        canvas.drawLine(width / 2f, height / 2f, width / 2f, height / 2f - 300, hourPaint)
        canvas.restore()

        canvas.save()
        canvas.rotate(minAngle, width / 2f, height / 2f)
        canvas.drawLine(width / 2f, height / 2f, width / 2f, height / 2f - 400, minPaint)
        canvas.restore()

        canvas.save()
        canvas.rotate(secAngle, width / 2f, height / 2f)
        canvas.drawLine(width / 2f, height / 2f, width / 2f, height / 2f - 470, secPaint)
        canvas.restore()
    }

}