package com.eem.androidgraphics.week1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class CircularGraphView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val redLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            val degreeList = generateCircularGraph(listOf(20, 15, 34, 19, 38))
            generateArc(it, degreeList)
        }
    }

    private fun generateArc(canvas: Canvas, degreeList: FloatArray) {
        val viewWidth = measuredWidth
        val viewHeight = measuredHeight
        val center = (viewWidth / 2).toFloat()
        val left = center.minus(viewHeight.div(2))
        val right = center.plus(viewHeight.div(2))
        var lastAngle = 0f
        degreeList.forEachIndexed { index, fl ->
            redLinePaint.color = generateColor(index)
            canvas.drawArc(
                RectF(left, 0f, right, viewHeight.toFloat()),
                lastAngle,
                fl,
                true,
                redLinePaint)
            lastAngle += fl
        }
    }

    private fun generateCircularGraph(dataInput: List<Int>): FloatArray {
        val sum = dataInput.sumOf { it }.toFloat()
        val degreeList = FloatArray(dataInput.size)
        dataInput.forEachIndexed { index, value ->
            degreeList[index] = value.times(360).div(sum)
        }
        return degreeList
    }

    private fun generateColor(i: Int): Int {
        val listColor = listOf(
            Color.BLACK,
            Color.BLUE,
            Color.CYAN,
            Color.GRAY,
            Color.RED,
            Color.MAGENTA,
            Color.LTGRAY,
            Color.GREEN,
            Color.YELLOW
        )
        return listColor[i]
    }
}