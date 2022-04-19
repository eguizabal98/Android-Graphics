package com.eem.androidgraphics.week2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class My3DView(context: Context) : View(context) {

    private val redPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG) //paint object for drawing the lines
    private val cubeVertices: Array<Coordinate> //the vertices of a 3D cube
    private var drawCubeVertices: Array<Coordinate>//the vertices for drawing a 3D cube

    init {
        //create the paint object
        //create the paint object
        redPaint.style = Paint.Style.STROKE //Stroke

        redPaint.color = Color.RED
        redPaint.strokeWidth = 2f
        //create a 3D cube
        //create a 3D cube
        cubeVertices = Array<Coordinate>(8) { Coordinate() }
        cubeVertices[0] = Coordinate(-1.0, -1.0, -1.0, 1.0)
        cubeVertices[1] = Coordinate(-1.0, -1.0, 1.0, 1.0)
        cubeVertices[2] = Coordinate(-1.0, 1.0, -1.0, 1.0)
        cubeVertices[3] = Coordinate(-1.0, 1.0, 1.0, 1.0)
        cubeVertices[4] = Coordinate(1.0, -1.0, -1.0, 1.0)
        cubeVertices[5] = Coordinate(1.0, -1.0, 1.0, 1.0)
        cubeVertices[6] = Coordinate(1.0, 1.0, -1.0, 1.0)
        cubeVertices[7] = Coordinate(1.0, 1.0, 1.0, 1.0)
        drawCubeVertices = translate(cubeVertices, 20.0, 20.0, 2.0)
        drawCubeVertices = scale(drawCubeVertices, 40.0, 40.0, 40.0)
        drawCubeVertices = rotate(drawCubeVertices, (45 * (2 * PI) / 360), AxisRotation.Y_AXI)
        drawCubeVertices = rotate(drawCubeVertices, (45 * (2 * PI) / 360), AxisRotation.X_AXI)
        drawCubeVertices = rotate(drawCubeVertices, (15 * (2 * PI) / 360), AxisRotation.Z_AXI)
        this.invalidate() //update the view
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { DrawCube(it) } //draw a cube onto the screen
    }

    private fun drawLinePairs(
        canvas: Canvas,
        vertices: Array<Coordinate>,
        start: Int,
        end: Int,
        paint: Paint,
    ) { //draw a line connecting 2 points
        //canvas - canvas of the view
        //points - array of points
        //start - index of the starting point
        //end - index of the ending point
        //paint - the paint of the line
        canvas.drawLine(
            vertices[start].x.toFloat(),
            vertices[start].y.toFloat(),
            vertices[end].x.toFloat(),
            vertices[end].y.toFloat(),
            paint
        )
    }

    private fun DrawCube(canvas: Canvas) { //draw a cube on the screen
        drawLinePairs(canvas, drawCubeVertices, 0, 1, redPaint)
        drawLinePairs(canvas, drawCubeVertices, 1, 3, redPaint)
        drawLinePairs(canvas, drawCubeVertices, 3, 2, redPaint)
        drawLinePairs(canvas, drawCubeVertices, 2, 0, redPaint)
        drawLinePairs(canvas, drawCubeVertices, 4, 5, redPaint)
        drawLinePairs(canvas, drawCubeVertices, 5, 7, redPaint)
        drawLinePairs(canvas, drawCubeVertices, 7, 6, redPaint)
        drawLinePairs(canvas, drawCubeVertices, 6, 4, redPaint)
        drawLinePairs(canvas, drawCubeVertices, 0, 4, redPaint)
        drawLinePairs(canvas, drawCubeVertices, 1, 5, redPaint)
        drawLinePairs(canvas, drawCubeVertices, 2, 6, redPaint)
        drawLinePairs(canvas, drawCubeVertices, 3, 7, redPaint)
    }

    //matrix and transformation functions
    private fun getIdentityMatrix(): DoubleArray { //return an 4x4 identity matrix
        val matrix = DoubleArray(16)
        matrix[0] = 1.0
        matrix[1] = 0.0
        matrix[2] = 0.0
        matrix[3] = 0.0
        matrix[4] = 0.0
        matrix[5] = 1.0
        matrix[6] = 0.0
        matrix[7] = 0.0
        matrix[8] = 0.0
        matrix[9] = 0.0
        matrix[10] = 1.0
        matrix[11] = 0.0
        matrix[12] = 0.0
        matrix[13] = 0.0
        matrix[14] = 0.0
        matrix[15] = 1.0
        return matrix
    }

    private fun transformation(
        vertex: Coordinate,
        matrix: DoubleArray,
    ): Coordinate { //affine transformation with homogeneous coordinates
        //i.e. a vector (vertex) multiply with the transformation matrix
        // vertex - vector in 3D
        // matrix - transformation matrix
        val result = Coordinate()
        result.x = matrix[0] * vertex.x + matrix[1] * vertex.y + matrix[2] * vertex.z + matrix[3]
        result.y = matrix[4] * vertex.x + matrix[5] * vertex.y + matrix[6] * vertex.z + matrix[7]
        result.z = matrix[8] * vertex.x + matrix[9] * vertex.y + matrix[10] * vertex.z + matrix[11]
        result.w =
            matrix[12] * vertex.x + matrix[13] * vertex.y + matrix[14] * vertex.z + matrix[15]
        return result
    }

    private fun transformation(
        vertices: Array<Coordinate>,
        matrix: DoubleArray,
    ): Array<Coordinate> {   //Affine transform a 3D object with vertices
        // vertices - vertices of the 3D object.
        // matrix - transformation matrix
        val result = Array(vertices.size) { Coordinate() }
        for (i in vertices.indices) {
            result[i] = transformation(vertices[i], matrix)
            result[i].normalise()
        }
        return result
    }

    //Affine transformation
    private fun translate(
        vertices: Array<Coordinate>,
        tx: Double,
        ty: Double,
        tz: Double,
    ): Array<Coordinate> {
        val matrix: DoubleArray = getIdentityMatrix()
        matrix[3] = tx
        matrix[7] = ty
        matrix[11] = tz
        return transformation(vertices, matrix)
    }

    private fun scale(
        vertices: Array<Coordinate>,
        sx: Double,
        sy: Double,
        sz: Double,
    ): Array<Coordinate> {
        val matrix: DoubleArray = getIdentityMatrix()
        matrix[0] = sx
        matrix[5] = sy
        matrix[10] = sz
        return transformation(vertices, matrix)
    }

    private fun rotate(
        vertices: Array<Coordinate>,
        radians: Double,
        axi: AxisRotation,
    ): Array<Coordinate> {
        val matrix: DoubleArray = getIdentityMatrix()
        when (axi) {
            AxisRotation.X_AXI -> {
                matrix[0] = 1.0
                matrix[5] = cos(radians)
                matrix[6] = -sin(radians)
                matrix[9] = sin(radians)
                matrix[10] = cos(radians)
                matrix[15] = 1.0
            }
            AxisRotation.Y_AXI -> {
                matrix[0] = cos(radians)
                matrix[2] = sin(radians)
                matrix[5] = 1.0
                matrix[8] = -sin(radians)
                matrix[10] = cos(radians)
                matrix[15] = 1.0
            }
            AxisRotation.Z_AXI -> {
                matrix[0] = cos(radians)
                matrix[1] = -sin(radians)
                matrix[4] = sin(radians)
                matrix[5] = cos(radians)
                matrix[10] = 1.0
                matrix[15] = 1.0
            }
        }
        return transformation(vertices, matrix)
    }

    enum class AxisRotation() {
        X_AXI,
        Y_AXI,
        Z_AXI
    }
}