package com.eem.androidgraphics.opengladvance

import android.opengl.GLES32
import com.eem.androidgraphics.opengl.MyRenderer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Sphere {
    private val vertexShaderCode = "attribute vec3 aVertexPosition;" +
            "attribute vec4 aVertexColor;" +
            "uniform mat4 uMVPMatrix;varying vec4 vColor;" +
            "void main() {" +
            "gl_Position = uMVPMatrix *vec4(aVertexPosition,1.0);" +
            "vColor=aVertexColor;" +
            "}"
    private val fragmentShaderCode = "precision lowp float;varying vec4 vColor; " +
            "void main() {" +
            "gl_FragColor=vColor;" +  //the fragment color
            "}"
    private val vertexBuffer: FloatBuffer
    private val colorBuffer: FloatBuffer
    private val indexBuffer: IntBuffer
    private val mProgram: Int
    private val mPositionHandle: Int
    private val mColorHandle: Int
    private val mMVPMatrixHandle: Int
    private val vertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex
    private val colorStride = COLOR_PER_VERTEX * 4

    private fun createSphere(radius: Float, noLatitude: Int, noLongitude: Int) {
        val vertices = FloatArray(65535)
        val pindex = IntArray(65535)
        val pcolor = FloatArray(65535)
        var vertexindex = 0
        var colorindex = 0
        var indx = 0
        val dist = 0f
        for (row in 0..noLatitude) {
            val theta = row * Math.PI / noLatitude
            val sinTheta = Math.sin(theta)
            val cosTheta = Math.cos(theta)
            var tcolor = -0.5f
            val tcolorinc = 1f / (noLongitude + 1).toFloat()
            for (col in 0..noLongitude) {
                val phi = col * 2 * Math.PI / noLongitude
                val sinPhi = Math.sin(phi)
                val cosPhi = Math.cos(phi)
                val x = cosPhi * sinTheta
                val z = sinPhi * sinTheta
                vertices[vertexindex++] = (radius * x).toFloat()
                vertices[vertexindex++] = (radius * cosTheta).toFloat() + dist
                vertices[vertexindex++] = (radius * z).toFloat()
                pcolor[colorindex++] = 1f
                pcolor[colorindex++] = Math.abs(tcolor)
                pcolor[colorindex++] = 0f
                pcolor[colorindex++] = 1f
                tcolor += tcolorinc
            }
        }
        for (row in 0 until noLatitude) {
            for (col in 0 until noLongitude) {
                val first = row * (noLongitude + 1) + col
                val second = first + noLongitude + 1
                pindex[indx++] = first
                pindex[indx++] = second
                pindex[indx++] = first + 1
                pindex[indx++] = second
                pindex[indx++] = second + 1
                pindex[indx++] = first + 1
            }
        }
        SphereVertex = vertices.copyOf(vertexindex)
        SphereIndex = pindex.copyOf(indx)
        SphereColor = pcolor.copyOf(colorindex)
    }

    fun draw(mvpMatrix: FloatArray?) {
        // Apply the projection and view transformation
        GLES32.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)
        MyRenderer.checkGlError("glUniformMatrix4fv")
        //===================
        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
            GLES32.GL_FLOAT, false, vertexStride, vertexBuffer)
        GLES32.glVertexAttribPointer(mColorHandle, COLOR_PER_VERTEX,
            GLES32.GL_FLOAT, false, colorStride, colorBuffer)
        // Draw the sphere
        GLES32.glDrawElements(GLES32.GL_LINES,
            SphereIndex.size,
            GLES32.GL_UNSIGNED_INT,
            indexBuffer)
    }

    companion object {
        // number of coordinates per vertex in this array
        const val COORDS_PER_VERTEX = 3
        const val COLOR_PER_VERTEX = 4
        var SphereVertex = floatArrayOf()
        var SphereColor = floatArrayOf()
        var SphereIndex = intArrayOf()
    }

    init {
        createSphere(2f, 30, 30)
        // initialize vertex byte buffer for shape coordinates
        val bb =
            ByteBuffer.allocateDirect(SphereVertex.size * 4) // (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer.put(SphereVertex)
        vertexBuffer.position(0)
        val ib = IntBuffer.allocate(SphereIndex.size)
        indexBuffer = ib
        indexBuffer.put(SphereIndex)
        indexBuffer.position(0)
        val cb = ByteBuffer.allocateDirect(SphereColor.size * 4)
        cb.order(ByteOrder.nativeOrder())
        colorBuffer = cb.asFloatBuffer()
        colorBuffer.put(SphereColor)
        colorBuffer.position(0)
        //////////////////////
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
        // Enable a handle to the triangle vertices
        GLES32.glEnableVertexAttribArray(mPositionHandle)
        // Prepare the triangle coordinate data
        GLES32.glVertexAttribPointer(mPositionHandle,
            COORDS_PER_VERTEX,
            GLES32.GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer)
        MyRenderer.checkGlError("glVertexAttribPointer")
        mColorHandle = GLES32.glGetAttribLocation(mProgram, "aVertexColor")
        GLES32.glEnableVertexAttribArray(mColorHandle)
        GLES32.glVertexAttribPointer(mColorHandle,
            COLOR_PER_VERTEX,
            GLES32.GL_FLOAT,
            false,
            colorStride,
            colorBuffer)
        mMVPMatrixHandle = GLES32.glGetUniformLocation(mProgram, "uMVPMatrix")
        MyRenderer.checkGlError("glGetUniformLocation-mMVPMatrixHandle")
    }
}