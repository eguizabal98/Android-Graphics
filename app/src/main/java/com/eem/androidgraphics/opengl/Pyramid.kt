package com.eem.androidgraphics.opengl

import android.opengl.GLES32
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Pyramid {

    companion object {
        // number of coordinates per vertex in this array
        private const val COORDS_PER_VERTEX = 3
        private const val COLOR_PER_VERTEX = 4
    }

    private val vertexShaderCode =
        "attribute vec3 aVertexPosition;" + "uniform mat4 uMVPMatrix;varying vec4 vColor;" +
                "attribute vec4 aVertexColor;" +
                "void main() {gl_Position = uMVPMatrix *vec4(aVertexPosition,1.0);" +
                "vColor=aVertexColor;}"
    private val fragmentShaderCode = "precision mediump float;varying vec4 vColor; " +
            "void main() {gl_FragColor = vColor;}"

    private var vertexBuffer: FloatBuffer? = null
    private var colorBuffer: FloatBuffer? = null
    private var mProgram = 0
    private var mPositionHandle = 0
    private var mColorHandle = 0
    private var mMVPMatrixHandle = 0

    private var vertexCount = 0 // number of vertices
    private val vertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex
    private val colorStride = COLOR_PER_VERTEX * 4 // 4 bytes per vertex

    private val pyramidVertex = floatArrayOf(
        //Front Face
        0f, 1f, 0f,
        -1f, -1f, 1f,
        1f, -1f, 1f,
        //Right Face
        0f, 1f, 0f,
        1f, -1f, 1f,
        1f, -1f, -1f,
        //Back Face
        0f, 1f, 0f,
        1f, -1f, -1f,
        -1f, -1f, -1f,
        //Left Face
        0f, 1f, 0f,
        -1f, -1f, -1f,
        -1f, -1f, 1f
    )

    private val pyramidColor = floatArrayOf(
        //Front Face
        1f, 0f, 0f, 1f,
        0f, 1f, 0f, 1f,
        0f, 0f, 1f, 1f,
        //Right Face
        1f, 0f, 0f, 1f,
        0f, 0f, 1f, 1f,
        0f, 1f, 0f, 1f,
        //Back Face
        1f, 0f, 0f, 1f,
        0f, 1f, 0f, 1f,
        0f, 0f, 1f, 1f,
        //Left Face
        1f, 0f, 0f, 1f,
        0f, 0f, 1f, 1f,
        0f, 1f, 0f, 1f,
    )

    init {
        val bb: ByteBuffer =
            ByteBuffer.allocateDirect(pyramidVertex.size * 4) // (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(pyramidVertex)
        vertexBuffer?.position(0)
        vertexCount = pyramidVertex.size / COORDS_PER_VERTEX

        val cb: ByteBuffer =
            ByteBuffer.allocateDirect(pyramidColor.size * 4) // (# of coordinate values * 4 bytes per float)
        cb.order(ByteOrder.nativeOrder())
        colorBuffer = cb.asFloatBuffer()
        colorBuffer?.put(pyramidColor)
        colorBuffer?.position(0)

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
        mPositionHandle = GLES32.glGetAttribLocation(mProgram, "aVertexPosition")
        mColorHandle = GLES32.glGetAttribLocation(mProgram, "aVertexColor")

        // Enable a handle to the triangle vertices
        GLES32.glEnableVertexAttribArray(mPositionHandle)
        GLES32.glEnableVertexAttribArray(mColorHandle)

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

        GLES32.glVertexAttribPointer(mColorHandle, COLOR_PER_VERTEX,
            GLES32.GL_FLOAT, false, colorStride, colorBuffer)
        // Draw the triangle
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, vertexCount)
    }
}