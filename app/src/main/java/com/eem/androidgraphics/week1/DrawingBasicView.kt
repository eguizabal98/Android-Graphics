package com.eem.androidgraphics.week1

import android.content.Context
import android.graphics.*
import android.view.View


class DrawingBasicView(context: Context) : View(context) {

    private val redLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val greenSolidPaint = Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        redLinePaint.style = Paint.Style.STROKE
        redLinePaint.color = Color.RED
        redLinePaint.strokeWidth = 5f

        greenSolidPaint.style = Paint.Style.FILL
        greenSolidPaint.color = Color.GREEN

        canvas?.let {
            generateSquare(it)
            generateCircle(it)
            generateArc(it)
            generateOval(it)
            generatePath(it)
            generateGradientFill(it)
        }
    }

    private fun generateSquare(canvas: Canvas) {
        canvas.drawRect(10f, 30f, 200f, 100f, redLinePaint)
    }

    private fun generateCircle(canvas: Canvas) {
        canvas.drawCircle(400f, 200f, 150f, redLinePaint)
    }

    private fun generateArc(canvas: Canvas) {
        canvas.drawArc(RectF(600f, 50f, 800f, 250f), 0f, 350f, true, redLinePaint)
        canvas.drawArc(RectF(850f, 50f, 1050f, 250f), 0f, 350f, false, redLinePaint)
        canvas.drawArc(10f, 400f, 210f, 600f, 0f, 350f, true, redLinePaint)
    }

    private fun generateOval(canvas: Canvas) {
        canvas.drawOval(10f, 650f, 410f, 870f, redLinePaint)
    }

    private fun generatePath(canvas: Canvas) {
        val path = Path()
        path.moveTo(10f, 900f)
        path.lineTo(200f, 1200f)
        path.lineTo(400f, 900f)
        path.lineTo(600f, 1200f)
        path.lineTo(800f, 880f)
        path.close()
        canvas.drawPath(path, redLinePaint)
        canvas.drawPath(path, greenSolidPaint)
    }

    private fun generateGradientFill(canvas: Canvas) {
        val path = Path()
        path.moveTo(10f, 1300f)
        path.lineTo(200f, 1600f)
        path.lineTo(400f, 1300f)
        path.lineTo(600f, 1600f)
        path.lineTo(800f, 1280f)
        path.close()

        val linearGradient =
            LinearGradient(10f, 900f, 800f, 880f, Color.RED, Color.BLUE, Shader.TileMode.MIRROR)
        val gradientPaint = Paint()
        gradientPaint.style = Paint.Style.FILL
        gradientPaint.shader = linearGradient
        canvas.drawPath(path, gradientPaint)
    }
}