package com.eem.androidgraphics.week1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.util.AttributeSet
import android.view.View

class PlottingAndGraphsView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val redLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        redLinePaint.style = Paint.Style.STROKE
        redLinePaint.color = Color.RED
        redLinePaint.strokeWidth = 5f

        canvas?.let {
            generateLinearGraph(it)
        }
    }

    private fun generateLinearGraph(canvas: Canvas) {
        val viewWidth = measuredWidth
        val viewHeight = measuredHeight

        val plotData = listOf(11, 29, 10, 20, 12, 5, 31, 24, 21, 13)

        val lineGraph = createLineGraph(plotData, viewWidth, viewHeight)

        canvas.drawPath(lineGraph, redLinePaint)
    }

    private fun createLineGraph(input: List<Int>, width: Int, height: Int): Path {
        var ptArray = Array(input.size) { Point() }
        val minValue = input.minOf { it }.toDouble()
        val maxValue = input.maxOf { it }.toDouble()

        input.forEachIndexed { index, _ ->
            ptArray[index] = Point(index, input[index])
        }

        ptArray = translate(ptArray, 0.0, -minValue)
        val yScale = height / (maxValue - minValue)
        val xScale = (width / (input.size - 1)).toDouble()

        ptArray = scale(ptArray, xScale, yScale)

        val result = Path()

        result.moveTo(ptArray[0].x.toFloat(), ptArray[0].y.toFloat())

        for (i in 1..ptArray.lastIndex) {
            result.lineTo(ptArray[i].x.toFloat(), ptArray[i].y.toFloat())
        }

        return result
    }

    private fun translate(input: Array<Point>, px: Double, py: Double): Array<Point> {
        val matrix: Array<DoubleArray> = arrayOf(
            doubleArrayOf(1.0, 0.0, px),
            doubleArrayOf(0.0, 1.0, py),
            doubleArrayOf(0.0, 0.0, 1.0)
        )

        return affineTransformation(input, matrix)
    }

    private fun scale(
        input: Array<Point>,
        widthScale: Double,
        heightScale: Double,
    ): Array<Point> {
        val matrix: Array<DoubleArray> = arrayOf(
            doubleArrayOf(widthScale, 0.0, 0.0),
            doubleArrayOf(0.0, heightScale, 0.0),
            doubleArrayOf(0.0, 0.0, 1.0)
        )

        return affineTransformation(input, matrix)
    }

    private fun affineTransformation(
        vertices: Array<Point>,
        matrix: Array<DoubleArray>,
    ): Array<Point> {
        val result = Array(vertices.size) { Point(0, 0) }
        vertices.forEachIndexed { i, _ ->
            val t = matrix[0][0] * (vertices[i].x) +
                    matrix[0][1] * (vertices[i].y) +
                    matrix[0][2]
            val u = matrix[1][0] * (vertices[i].x) +
                    matrix[1][1] * (vertices[i].y) +
                    matrix[1][2]
            result[i] = Point(t.toInt(), u.toInt())
        }
        return result
    }
}