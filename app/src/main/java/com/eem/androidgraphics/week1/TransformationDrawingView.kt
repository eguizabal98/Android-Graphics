package com.eem.androidgraphics.week1

import android.content.Context
import android.graphics.*
import android.view.View
import kotlin.math.cos
import kotlin.math.sin


class TransformationDrawingView(context: Context) : View(context) {

    val myLines = Path()
    val points = arrayListOf(
        Point(250, 500),
        Point(350, 600),
        Point(380, 540),
        Point(440, 620),
        Point(500, 400)
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            generateDraw(it)
        }
    }

    private fun generateDraw(canvas: Canvas) {
        myLines.moveTo(250f, 500f)
        myLines.lineTo(350f, 600f)
        myLines.lineTo(380f, 540f)
        myLines.lineTo(440f, 620f)
        myLines.lineTo(500f, 400f)
        myLines.close()

        val linearGradient =
            LinearGradient(10f, 300f, 300f, 200f, Color.RED, Color.BLUE, Shader.TileMode.MIRROR)
        val gradientPaint = Paint()
        gradientPaint.style = Paint.Style.FILL
        gradientPaint.shader = linearGradient

        val greenLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        greenLinePaint.style = Paint.Style.STROKE
        greenLinePaint.color = Color.GREEN
        greenLinePaint.strokeWidth = 5f

        canvas.drawPath(myLines, gradientPaint)
        val degree = 20.0
        val rad = Math.toRadians(degree)
        val newPoints = rotation(points.toTypedArray(), rad)
        updatePath(newPoints)
        canvas.drawPath(myLines, greenLinePaint)
    }

    private fun updatePath(newPoints: Array<Point>) {
        myLines.reset()
        myLines.moveTo(newPoints[0].x.toFloat(), newPoints[0].y.toFloat())
        for (i in 1..newPoints.size.minus(1)) {
            myLines.lineTo(newPoints[i].x.toFloat(), newPoints[i].y.toFloat())
        }
        myLines.close()
    }

    private fun affineTransformation(
        vertices: Array<Point>,
        matrix: Array<DoubleArray>
    ): Array<Point> {
        val result = Array(vertices.size) { Point(0, 0) }
        val centroid = calculateCentroid(vertices)
        vertices.forEachIndexed { i, _ ->
            val t = matrix[0][0] * (vertices[i].x - centroid.first) +
                    matrix[0][1] * (vertices[i].y - centroid.second) +
                    matrix[0][2]
            val u = matrix[1][0] * (vertices[i].x - centroid.first) +
                    matrix[1][1] * (vertices[i].y - centroid.second) +
                    matrix[1][2]
            result[i] = Point(t.plus(centroid.first).toInt(), u.plus(centroid.second).toInt())
        }
        return result
    }

    private fun affineTransformationTranslation(
        vertices: Array<Point>,
        matrix: Array<IntArray>
    ): Array<Point> {
        val result = Array(vertices.size) { Point(0, 0) }
        vertices.forEachIndexed { i, _ ->
            val t = matrix[0][0] * vertices[i].x + matrix[0][1] * vertices[i].y + matrix[0][2]
            val u = matrix[1][0] * vertices[i].x + matrix[1][1] * vertices[i].y + matrix[1][2]
            result[i] = Point(t, u)
        }
        return result
    }

    private fun translate(input: Array<Point>, px: Int, py: Int): Array<Point> {
        val matrix: Array<IntArray> = arrayOf(
            intArrayOf(1, 0, px),
            intArrayOf(0, 1, py),
            intArrayOf(0, 0, 1)
        )

        return affineTransformationTranslation(input, matrix)
    }

    private fun rotation(input: Array<Point>, angle: Double): Array<Point> {
        val matrix: Array<DoubleArray> = arrayOf(
            doubleArrayOf(cos(angle), (-sin(angle)), 0.0),
            doubleArrayOf(sin(angle), cos(angle), 0.0),
            doubleArrayOf(0.0, 0.0, 1.0)
        )

        return affineTransformation(input, matrix)
    }

    private fun calculateCentroid(vertices: Array<Point>): Pair<Int, Int> {
        val mediumX = vertices.sumOf { it.x } / vertices.size
        val mediumY = vertices.sumOf { it.y } / vertices.size
        return Pair(mediumX, mediumY)
    }
}