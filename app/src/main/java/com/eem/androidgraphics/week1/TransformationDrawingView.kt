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
//        val newPoints = scaling(points.toTypedArray(), 2.0, 2.0)
//        val newPoints = translate(points.toTypedArray(), 20.0, 40.0)
//        val newPoints = rotation(points.toTypedArray(), 30.0)
        val newPoints = shear(points.toTypedArray(), 0.4, 0.5)
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

    private fun translate(input: Array<Point>, px: Double, py: Double): Array<Point> {
        val matrix: Array<DoubleArray> = arrayOf(
            doubleArrayOf(1.0, 0.0, px),
            doubleArrayOf(0.0, 1.0, py),
            doubleArrayOf(0.0, 0.0, 1.0)
        )

        return affineTransformation(input, matrix)
    }

    private fun scaling(
        input: Array<Point>,
        widthScale: Double,
        heightScale: Double
    ): Array<Point> {
        val matrix: Array<DoubleArray> = arrayOf(
            doubleArrayOf(widthScale, 0.0, 0.0),
            doubleArrayOf(0.0, heightScale, 0.0),
            doubleArrayOf(0.0, 0.0, 1.0)
        )

        return affineTransformation(input, matrix)
    }

    private fun rotation(input: Array<Point>, angle: Double): Array<Point> {
        val rad = Math.toRadians(angle)
        val matrix: Array<DoubleArray> = arrayOf(
            doubleArrayOf(cos(rad), (-sin(rad)), 0.0),
            doubleArrayOf(sin(rad), cos(rad), 0.0),
            doubleArrayOf(0.0, 0.0, 1.0)
        )

        return affineTransformation(input, matrix)
    }

    private fun shear(input: Array<Point>, horizontal: Double, vertical: Double): Array<Point> {
        val matrix: Array<DoubleArray> = arrayOf(
            doubleArrayOf(1.0, horizontal, 0.0),
            doubleArrayOf(vertical, 1.0, 0.0),
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