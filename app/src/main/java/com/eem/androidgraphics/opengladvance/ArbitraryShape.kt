package com.eem.androidgraphics.opengladvance
import android.opengl.GLES32
import com.eem.androidgraphics.opengl.MyRenderer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


class ArbitraryShape {
    private val vertexShaderCode = "attribute vec3 aVertexPosition;" +  //vertex of an object
            "attribute vec4 aVertexColor;" +  //the colour  of the object
            "uniform mat4 uMVPMatrix;" +  //model view  projection matrix
            "varying vec4 vColor;" +  //variable to be accessed by the fragment shader
            "void main() {" +
            "gl_Position = uMVPMatrix* vec4(aVertexPosition, 1.0);" +  //calculate the position of the vertex
            "vColor=aVertexColor;}" //get the colour from the application program
    private val fragmentShaderCode = "precision mediump float;" +  //define the precision of float
            "varying vec4 vColor;" +  //variable from the vertex shader
            //---------
            "void main() {" +
            "   gl_FragColor = vColor; }" //change the colour based on the variable from the vertex shader
    private val vertexBuffer: FloatBuffer
    private val colorBuffer: FloatBuffer
    private val indexBuffer: IntBuffer
    private val vertex2Buffer: FloatBuffer
    private val color2Buffer: FloatBuffer
    private val index2Buffer: IntBuffer
    private val ringVertexBuffer: FloatBuffer
    private val ringColorBuffer: FloatBuffer
    private val ringIndexBuffer: IntBuffer
    private val mProgram: Int
    private val mPositionHandle: Int
    private val mColorHandle: Int
    private val mMVPMatrixHandle: Int
    private val vertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex
    private val colorStride = COLOR_PER_VERTEX * 4 //4 bytes per vertex

    private fun createSphere(radius: Float, noLatitude: Int, noLongitude: Int) {
        val vertices = FloatArray(65535)
        val index = IntArray(65535)
        val color = FloatArray(65535)
        val pNormLen = (noLongitude + 1) * 3 * 3
        var vertexindex = 0
        var colorindex = 0
        var indx = 0
        val vertices2 = FloatArray(65535)
        val index2 = IntArray(65535)
        val color2 = FloatArray(65525)
        var vertex2index = 0
        var color2index = 0
        var indx2 = 0
        val ringVertices = FloatArray(65535)
        val ringIndex1 = IntArray(65535)
        val ringColor1 = FloatArray(65525)
        var rvindx = 0
        var rcindex = 0
        var rindx = 0
        val dist = 3f
        var plen = (noLongitude + 1) * 3 * 3
        var pcolorlen = (noLongitude + 1) * 4 * 3
        for (row in 0 until noLatitude + 1) {
            val theta = row * Math.PI / noLatitude
            val sinTheta = sin(theta)
            val cosTheta = cos(theta)
            var tcolor = -0.5f
            val tcolorinc = 1 / (noLongitude + 1).toFloat()
            for (col in 0 until noLongitude + 1) {
                val phi = col * 2 * Math.PI / noLongitude
                val sinPhi = sin(phi)
                val cosPhi = cos(phi)
                val x = cosPhi * sinTheta
                val z = sinPhi * sinTheta
                vertices[vertexindex++] = (radius * x).toFloat()
                vertices[vertexindex++] = (radius * cosTheta).toFloat() + dist
                vertices[vertexindex++] = (radius * z).toFloat()
                vertices2[vertex2index++] = (radius * x).toFloat()
                vertices2[vertex2index++] = (radius * cosTheta).toFloat() - dist
                vertices2[vertex2index++] = (radius * z).toFloat()
                color[colorindex++] = 1f
                color[colorindex++] = abs(tcolor)
                color[colorindex++] = 0f
                color[colorindex++] = 1f
                color2[color2index++] = 0f
                color2[color2index++] = 1f
                color2[color2index++] = abs(tcolor)
                color2[color2index++] = 1f
                if (row == 20) {
                    ringVertices[rvindx++] = (radius * x).toFloat()
                    ringVertices[rvindx++] = (radius * cosTheta).toFloat() + dist
                    ringVertices[rvindx++] = (radius * z).toFloat()
                    ringColor1[rcindex++] = 1f
                    ringColor1[rcindex++] = abs(tcolor)
                    ringColor1[rcindex++] = 0f
                    ringColor1[rcindex++] = 1f
                }
                if (row == 15) {
                    ringVertices[rvindx++] = (radius * x).toFloat() / 2
                    ringVertices[rvindx++] = (radius * cosTheta).toFloat() / 2 + 0.2f * dist
                    ringVertices[rvindx++] = (radius * z).toFloat() / 2
                    ringColor1[rcindex++] = 1f
                    ringColor1[rcindex++] = abs(tcolor)
                    ringColor1[rcindex++] = 0f
                    ringColor1[rcindex++] = 1f
                }
                if (row == 10) {
                    ringVertices[rvindx++] = (radius * x).toFloat() / 2
                    ringVertices[rvindx++] = (radius * cosTheta).toFloat() / 2 - 0.1f * dist
                    ringVertices[rvindx++] = (radius * z).toFloat() / 2
                    ringColor1[rcindex++] = 0f
                    ringColor1[rcindex++] = 1f
                    ringColor1[rcindex++] = abs(tcolor)
                    ringColor1[rcindex++] = 1f
                }
                if (row == 20) {
                    ringVertices[plen++] = (radius * x).toFloat()
                    ringVertices[plen++] = (-radius * cosTheta).toFloat() - dist
                    ringVertices[plen++] = (radius * z).toFloat()
                    ringColor1[pcolorlen++] = 0f
                    ringColor1[pcolorlen++] = 1f
                    ringColor1[pcolorlen++] = abs(tcolor)
                    ringColor1[pcolorlen++] = 1f
                    //-------
                }
                tcolor += tcolorinc
            }
        }
        //index buffer
        for (row in 0 until noLatitude) {
            for (col in 0 until noLongitude) {
                val p0 = row * (noLongitude + 1) + col
                val p1 = p0 + noLongitude + 1
                index[indx++] = p1
                index[indx++] = p0
                index[indx++] = p0 + 1
                index[indx++] = p1 + 1
                index[indx++] = p1
                index[indx++] = p0 + 1
                index2[indx2++] = p1
                index2[indx2++] = p0
                index2[indx2++] = p0 + 1
                index2[indx2++] = p1 + 1
                index2[indx2++] = p1
                index2[indx2++] = p0 + 1
            }
        }
        rvindx = (noLongitude + 1) * 3 * 4
        rcindex = (noLongitude + 1) * 4 * 4
        plen = noLongitude + 1
        for (j in 0 until plen - 1) {
            ringIndex1[rindx++] = j
            ringIndex1[rindx++] = j + plen
            ringIndex1[rindx++] = j + 1
            ringIndex1[rindx++] = j + plen + 1
            ringIndex1[rindx++] = j + 1
            ringIndex1[rindx++] = j + plen
            ringIndex1[rindx++] = j + plen
            ringIndex1[rindx++] = j + plen * 2
            ringIndex1[rindx++] = j + plen + 1
            ringIndex1[rindx++] = j + plen * 2 + 1
            ringIndex1[rindx++] = j + plen + 1
            ringIndex1[rindx++] = j + plen * 2
            ringIndex1[rindx++] = j + plen * 3
            ringIndex1[rindx++] = j
            ringIndex1[rindx++] = j + 1
            ringIndex1[rindx++] = j + 1
            ringIndex1[rindx++] = j + plen * 3 + 1
            ringIndex1[rindx++] = j + plen * 3
        }


        //set the buffers
        SphereVertex = vertices.copyOf(vertexindex)
        SphereIndex = index.copyOf(indx)
        SphereColor = color.copyOf(colorindex)
        Sphere2Vertex = vertices2.copyOf(vertex2index)
        Sphere2Index = index2.copyOf(indx2)
        Sphere2Color = color2.copyOf(color2index)
        ringVertex = ringVertices.copyOf(rvindx)
        ringColor = ringColor1.copyOf(rcindex)
        ringIndex = ringIndex1.copyOf(rindx)
    }

    fun draw(mvpMatrix: FloatArray?) {
        // Apply the projection and view transformation
        GLES32.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)
        //---------
        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
            GLES32.GL_FLOAT, false, vertexStride, vertexBuffer)
        GLES32.glVertexAttribPointer(mColorHandle, COORDS_PER_VERTEX,
            GLES32.GL_FLOAT, false, colorStride, colorBuffer)
        // Draw the Sphere
        GLES32.glDrawElements(GLES32.GL_TRIANGLES,
            SphereIndex.size,
            GLES32.GL_UNSIGNED_INT,
            indexBuffer)
        //---------
        //2nd sphere
        GLES32.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
            GLES32.GL_FLOAT, false, vertexStride, vertex2Buffer)
        GLES32.glVertexAttribPointer(mColorHandle, COORDS_PER_VERTEX,
            GLES32.GL_FLOAT, false, colorStride, color2Buffer)
        // Draw the Sphere
        GLES32.glDrawElements(GLES32.GL_TRIANGLES,
            Sphere2Index.size,
            GLES32.GL_UNSIGNED_INT,
            index2Buffer)
        ///////////////////
        //Rings
        GLES32.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
            GLES32.GL_FLOAT, false, vertexStride, ringVertexBuffer)
        GLES32.glVertexAttribPointer(mColorHandle, COORDS_PER_VERTEX,
            GLES32.GL_FLOAT, false, colorStride, ringColorBuffer)
        GLES32.glDrawElements(GLES32.GL_TRIANGLES,
            ringIndex.size,
            GLES32.GL_UNSIGNED_INT,
            ringIndexBuffer)
    }

    companion object {
        //---------
        // number of coordinates per vertex in this array
        const val COORDS_PER_VERTEX = 3
        const val COLOR_PER_VERTEX = 4
        var SphereVertex = floatArrayOf()
        var SphereIndex = intArrayOf()
        var SphereColor = floatArrayOf()

        //2nd sphere
        var Sphere2Vertex = floatArrayOf()
        var Sphere2Index = intArrayOf()
        var Sphere2Color = floatArrayOf()

        //ring
        var ringVertex = floatArrayOf()
        var ringIndex = intArrayOf()
        var ringColor = floatArrayOf()
        var lightlocation = FloatArray(3) //point light source location
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
        val cb =
            ByteBuffer.allocateDirect(SphereColor.size * 4) // (# of coordinate values * 4 bytes per float)
        cb.order(ByteOrder.nativeOrder())
        colorBuffer = cb.asFloatBuffer()
        colorBuffer.put(SphereColor)
        colorBuffer.position(0)
        val ib = IntBuffer.allocate(SphereIndex.size)
        indexBuffer = ib
        indexBuffer.put(SphereIndex)
        indexBuffer.position(0)
        //2nd sphere
        val bb2 =
            ByteBuffer.allocateDirect(Sphere2Vertex.size * 4) // (# of coordinate values * 4 bytes per float)
        bb2.order(ByteOrder.nativeOrder())
        vertex2Buffer = bb2.asFloatBuffer()
        vertex2Buffer.put(Sphere2Vertex)
        vertex2Buffer.position(0)
        val cb2 =
            ByteBuffer.allocateDirect(Sphere2Color.size * 4) // (# of coordinate values * 4 bytes per float)
        cb2.order(ByteOrder.nativeOrder())
        color2Buffer = cb2.asFloatBuffer()
        color2Buffer.put(Sphere2Color)
        color2Buffer.position(0)
        val ib2 = IntBuffer.allocate(Sphere2Index.size)
        index2Buffer = ib2
        index2Buffer.put(SphereIndex)
        index2Buffer.position(0)
        val rbb =
            ByteBuffer.allocateDirect(ringVertex.size * 4) // (# of coordinate values * 4 bytes per float)
        rbb.order(ByteOrder.nativeOrder())
        ringVertexBuffer = rbb.asFloatBuffer()
        ringVertexBuffer.put(ringVertex)
        ringVertexBuffer.position(0)
        val rcb =
            ByteBuffer.allocateDirect(ringColor.size * 4) // (# of coordinate values * 4 bytes per float)
        rcb.order(ByteOrder.nativeOrder())
        ringColorBuffer = rcb.asFloatBuffer()
        ringColorBuffer.put(ringColor)
        ringColorBuffer.position(0)
        val rib = IntBuffer.allocate(ringIndex.size)
        ringIndexBuffer = rib
        ringIndexBuffer.put(ringIndex)
        ringIndexBuffer.position(0)
        //----------
        // prepare shaders and OpenGL program
        val vertexShader = MyRenderer.loadShader(GLES32.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = MyRenderer.loadShader(GLES32.GL_FRAGMENT_SHADER, fragmentShaderCode)
        mProgram = GLES32.glCreateProgram() // create empty OpenGL Program
        GLES32.glAttachShader(mProgram, vertexShader) // add the vertex shader to program
        GLES32.glAttachShader(mProgram, fragmentShader) // add the fragment shader to program
        GLES32.glLinkProgram(mProgram) // link the  OpenGL program to create an executable
        GLES32.glUseProgram(mProgram) // Add program to OpenGL environment
        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES32.glGetAttribLocation(mProgram, "aVertexPosition")
        // Enable a handle to the triangle vertices
        GLES32.glEnableVertexAttribArray(mPositionHandle)
        mColorHandle = GLES32.glGetAttribLocation(mProgram, "aVertexColor")
        // Enable a handle to the  colour
        GLES32.glEnableVertexAttribArray(mColorHandle)
        // Prepare the colour coordinate data
        GLES32.glVertexAttribPointer(mColorHandle,
            COLOR_PER_VERTEX,
            GLES32.GL_FLOAT,
            false,
            colorStride,
            colorBuffer)
        //---------
        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES32.glGetUniformLocation(mProgram, "uMVPMatrix")
    }
}