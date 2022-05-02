package com.eem.androidgraphics.opengl

import android.opengl.GLES32
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import kotlin.math.cos
import kotlin.math.sin


class Triangle {

    private val vertexShaderCode =
        "attribute vec3 aVertexPosition;" + "uniform mat4 uMVPMatrix;varying vec4 vColor;" +
                "void main() {gl_Position = uMVPMatrix *vec4(aVertexPosition,1.0);" +
                "vColor=vec4(0.0,1.0,0.0,1.0);}"
    private val fragmentShaderCode = "precision mediump float;varying vec4 vColor; " +
            "void main() {gl_FragColor = vColor;}"

    private var vertexBuffer: FloatBuffer? = null
    private var mProgram = 0
    private var mPositionHandle = 0
    private var mMVPMatrixHandle = 0

    // number of coordinates per vertex in this array
    val COORDS_PER_VERTEX = 3
    private var vertexCount = 0 // number of vertices
    private val vertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    init {
        val radius = 1f
        var prevx = radius
        var prevy = 0f
        val resolution = 180

        var triangleVertex = mutableListOf<Float>()

        for (i in 0..(90+resolution)) {
            val angle = (i/180f) * Math.PI
            val x = (radius * cos(angle)).toFloat()
            val y = (radius * sin(angle)).toFloat()

            //First Quadrant
            triangleVertex.add(prevx)
            triangleVertex.add(prevy)
            triangleVertex.add(1f)
            triangleVertex.add(0f)
            triangleVertex.add(0f)
            triangleVertex.add(1f)
            triangleVertex.add(x)
            triangleVertex.add(y)
            triangleVertex.add(1f)

            //Second Quadrant
            triangleVertex.add(-prevx)
            triangleVertex.add(prevy)
            triangleVertex.add(1f)
            triangleVertex.add(0f)
            triangleVertex.add(0f)
            triangleVertex.add(1f)
            triangleVertex.add(-x)
            triangleVertex.add(y)
            triangleVertex.add(1f)

            //Third Quadrant
            triangleVertex.add(-prevx)
            triangleVertex.add(-prevy)
            triangleVertex.add(1f)
            triangleVertex.add(0f)
            triangleVertex.add(0f)
            triangleVertex.add(1f)
            triangleVertex.add(-x)
            triangleVertex.add(-y)
            triangleVertex.add(1f)

            //Fourth Quadrant
            triangleVertex.add(prevx)
            triangleVertex.add(-prevy)
            triangleVertex.add(1f)
            triangleVertex.add(0f)
            triangleVertex.add(0f)
            triangleVertex.add(1f)
            triangleVertex.add(x)
            triangleVertex.add(-y)
            triangleVertex.add(1f)
        }

        val vertexArray = triangleVertex.toFloatArray()

        // initialize vertex byte buffer for shape coordinates
        val bb: ByteBuffer = ByteBuffer.allocateDirect(vertexArray.size * 4) // (# of coordinate values * 4 bytes per float)

        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(vertexArray)
        vertexBuffer?.position(0)
        vertexCount = vertexArray.size / COORDS_PER_VERTEX
        // prepare shaders and OpenGL program
        // prepare shaders and OpenGL program
        val vertexShader: Int = MyRenderer.loadShader(GLES32.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int =
            MyRenderer.loadShader(GLES32.GL_FRAGMENT_SHADER, fragmentShaderCode)
        mProgram = GLES32.glCreateProgram() // create empty OpenGL Program

        GLES32.glAttachShader(mProgram, vertexShader) // add the vertex shader to program

        GLES32.glAttachShader(mProgram, fragmentShader) // add the fragment shader to program

        GLES32.glLinkProgram(mProgram) // link the  OpenGL program to create an executable

        GLES32.glUseProgram(mProgram) // Add program to OpenGL environment

        // get handle to vertex shader's vPosition member
        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES32.glGetAttribLocation(mProgram, "aVertexPosition")
        // Enable a handle to the triangle vertices
        // Enable a handle to the triangle vertices
        GLES32.glEnableVertexAttribArray(mPositionHandle)
        // get handle to shape's transformation matrix
        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES32.glGetUniformLocation(mProgram, "uMVPMatrix")
        MyRenderer.checkGlError("glGetUniformLocation")
    }

    fun draw(mvpMatrix: FloatArray?) {
        // Apply the projection and view transformation
        GLES32.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)
        MyRenderer.checkGlError("glUniformMatrix4fv")
        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
            GLES32.GL_FLOAT, false, vertexStride, vertexBuffer)
        // Draw the triangle
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, vertexCount)
    }
}