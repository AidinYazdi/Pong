/*
 * the Mesh class
 * 
 * I think a mesh is like a collection of vertices that are all grouped together
 * to draw something in OpenGL
 * 
 * Aidin Yazdi
 */

package engine.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
    // an array of vertices to make up the mesh
    private Vertex[] vertices;
    // an array to keep track of in which order the vertices should be drawn
    private int[] indices;

    // this object will hold all the information about the texture
    private Material material;

    /*
     * the VAO (vertex array object) - check my LWJGL notes for more information
     * 
     * the PBO (positions buffer object)
     * 
     * the IBO (indices buffer object)
     * 
     * the CBO (color buffer object)
     * 
     * the TBO (texture coordinates buffer object)
     */
    private int vao, pbo, ibo, cbo, tbo;

    // the constructor
    public Mesh(Vertex[] vertices, int[] indices, Material material) {
	this.vertices = vertices;
	this.indices = indices;
	this.material = material;
    }

    /*
     * this method is perhaps the most important in the whole game engine
     * 
     * this is the method that organizes all of the vertices - basically all of
     * the data - into the VAO which then sends it to the GPU to be rendered
     */
    public void create() {
	// creates the material (check the source code for more information)
	if (material != null) {
	    material.create();
	}

	// creates the basic structure of the VAO
	vao = GL30.glGenVertexArrays();
	// binds our VAO to the "current VAO". Whichever VAO is the "current
	// VAO" is the
	// one that will be sent to the GPU - so this makes it so that our VAO
	// is sent
	// to the GPU
	GL30.glBindVertexArray(vao);

	// the data that will be stored in the PBO - the position data
	FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
	/*
	 * this array will temporarily hold the position data so that we can get
	 * all the vertices first before we store it in positionBuffer
	 */
	float[] positionData = new float[vertices.length * 3];
	// now we iterate through all the vertices and store the data
	for (int i = 0; i < vertices.length; i++) {
	    /*
	     * we need to offset everything by 3 when we store to positionData
	     * because there are 3 components to each vertex
	     */
	    positionData[(i * 3)] = vertices[i].getPosition().getX();
	    positionData[(i * 3) + 1] = vertices[i].getPosition().getY();
	    positionData[(i * 3) + 2] = vertices[i].getPosition().getZ();
	}
	/*
	 * actually store the position data in the positionBuffer. The data
	 * needs to be flipped because that's how OpenGL likes it
	 */
	positionBuffer.put(positionData).flip();

	// create the PBO - check the source code of the method for more details
	pbo = storeData(positionBuffer, 0, 3);

	/*
	 * the following code is for color. It mirrors the code above (which is
	 * for the PBO) so just check that code for comments
	 */
	FloatBuffer colorBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
	float[] colorData = new float[vertices.length * 3];
	for (int i = 0; i < vertices.length; i++) {
	    colorData[(i * 3)] = vertices[i].getColor().getX();
	    colorData[(i * 3) + 1] = vertices[i].getColor().getY();
	    colorData[(i * 3) + 2] = vertices[i].getColor().getZ();
	}
	colorBuffer.put(colorData).flip();
	cbo = storeData(colorBuffer, 1, 3);

	/*
	 * the following code is for the texture coordinates. It mirrors the
	 * code above (which is for the PBO) so just check that code for
	 * comments
	 */
	FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(vertices.length * 2);
	float[] textureData = new float[vertices.length * 2];
	for (int i = 0; i < vertices.length; i++) {
	    textureData[(i * 2)] = vertices[i].getTextureCoord().getX();
	    textureData[(i * 2) + 1] = vertices[i].getTextureCoord().getY();
	}
	textureBuffer.put(textureData).flip();
	tbo = storeData(textureBuffer, 2, 2);

	/*
	 * the following portion of the code (for the IBO) mirrors the portion
	 * of the code for the PBO (except that it uses integers instead of
	 * floats). So, just check the comments on the PBO section of the code
	 * for more details
	 */
	IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
	indicesBuffer.put(indices).flip();
	ibo = GL15.glGenBuffers();
	/*
	 * GL_ELEMENT_ARRAY_BUFFER tells OpenGL that this isn't a normal array
	 * of numbers with information that should be rendered - rather it's an
	 * array of numbers with the order of when OpenGL should render other
	 * information
	 */
	GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
	GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
	GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    // this method stores data into a buffer
    private int storeData(FloatBuffer buffer, int index, int size) {
	// make the bufferID into a buffer
	int bufferID = GL15.glGenBuffers();
	// bind the PBO
	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
	/*
	 * put the positionBuffer into the bufferID
	 * 
	 * the GL15.GL_ARRAY_BUFFER now refers to the PBO because we bound it
	 * above
	 * 
	 * GL15.GL_STATIC_DRAW is just telling OpenGL that we're not going to
	 * change the PBO in between now and rendering
	 */
	GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

	/*
	 * this will allow shaders to get the data from the VBO
	 * 
	 * each argument does as follows:
	 * 
	 * index - where we want to start storing the data (this will just start
	 * storing it at the beginning - so for the first buffer it will start
	 * at 0, for the next buffer it will start on 1, and so on)
	 * 
	 * size - how many pieces of data exist in each vertex (since our
	 * vertices exist in 3 dimension, there are usually 3 pieces of data to
	 * store - the x, y, and z coordinates)
	 * 
	 * GL11.GL_FLOAT - the size of each piece of data (we're using floats as
	 * our coordinates)
	 * 
	 * the last three arguments - idk but I don't think they're so relevant
	 * for what I'm doing here
	 */
	GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);

	/*
	 * this will unbind the buffer once we're done with it to free up system
	 * resources
	 */
	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

	// return the buffer now that we've created it
	return bufferID;
    }

    /*
     * this method deletes all the buffers and the vertex array object to free
     * up system resources
     */
    public void destroy() {
	GL15.glDeleteBuffers(pbo);
	GL15.glDeleteBuffers(cbo);
	GL15.glDeleteBuffers(ibo);
	GL15.glDeleteBuffers(tbo);

	GL30.glDeleteVertexArrays(vao);

	if (material != null) {
	    material.destroy();
	}
    }

    // the following are getters
    public Vertex[] getVertices() {
	return vertices;
    }

    public int[] getIndices() {
	return indices;
    }

    public int getVAO() {
	return vao;
    }

    public int getPBO() {
	return pbo;
    }

    public int getCBO() {
	return cbo;
    }

    public int getTBO() {
	return tbo;
    }

    public int getIBO() {
	return ibo;
    }

    public Material getMaterial() {
	return material;
    }
}
