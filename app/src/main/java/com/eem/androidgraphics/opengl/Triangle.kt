package com.eem.androidgraphics.opengl

import android.opengl.GLES32
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class Triangle {

    private val vertexShaderCode =
        "attribute vec3 aVertexPosition;" + "uniform mat4 uMVPMatrix;varying vec4 vColor;" +
                "void main() {gl_Position = uMVPMatrix *vec4(aVertexPosition,1.0);" +
                "vColor=vec4(1.0,0.0,0.0,1.0);}"
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

    var triangleVertex = floatArrayOf(
        -1.0f, -1.0f, 1.0f,
        1.0f, -1.0f, 1.0f,
        0.0f, 1.0f, 1.0f)

    init {
        // initialize vertex byte buffer for shape coordinates

        // initialize vertex byte buffer for shape coordinates
        val bb: ByteBuffer = ByteBuffer.allocateDirect(triangleVertex.size * 4) // (# of coordinate values * 4 bytes per float)

        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(triangleVertex)
        vertexBuffer?.position(0)
        vertexCount = triangleVertex.size / COORDS_PER_VERTEX
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