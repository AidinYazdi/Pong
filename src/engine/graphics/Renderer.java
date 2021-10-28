/*
 * the Renderer class
 * 
 * this is the class that actually renders the mesh
 * 
 * Aidin Yazdi
 */

package engine.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import engine.io.Window;
import engine.maths.Matrix4f;
import engine.objects.Camera;
import engine.objects.GameObject;

public class Renderer {
    // the shader
    private Shader shader;

    // the window
    private Window window;

    // the constructor
    public Renderer(Window window, Shader shader) {
	this.shader = shader;
	this.window = window;
    }

    public void renderMesh(GameObject object, Camera camera) {
	// bind the VBO
	GL30.glBindVertexArray(object.getMesh().getVAO());
	/*
	 * the next two lines enable positions in the vertex attribute array
	 * 
	 * for instance, we store the position in the first spot in the VAO
	 * (element 0). So, before we put the position buffer (the PBO) in the
	 * VAO we need to enable that element (so we must enable position 0 in
	 * the VAO). For the CBO, we need the second position (element 1), and
	 * so on and so forth
	 */
	GL30.glEnableVertexAttribArray(0);
	GL30.glEnableVertexAttribArray(1);
	GL30.glEnableVertexAttribArray(2);

	// bind the IBO
	GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, object.getMesh().getIBO());

	// deal with the texture
	if (object.getMesh().getMaterial() != null) {
	    // put our texture in the OpenGL texture array at the zero index
	    GL13.glActiveTexture(GL13.GL_TEXTURE0);
	    // bind our texture
	    GL13.glBindTexture(GL11.GL_TEXTURE_2D, object.getMesh().getMaterial().getTextureID());
	} else {
	    GL13.glActiveTexture(0);
	    GL13.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	// bind the shader (source code can be found in the Shader class)
	shader.bind();

	// set the uniforms
	shader.setUniform("model", Matrix4f.transform(object.getPosition(), object.getRotation(), object.getScale()));
	shader.setUniform("view", Matrix4f.view(camera.getPosition(), camera.getRotation()));
	shader.setUniform("projection", window.getProjectionMatrix());
	shader.setUniform("isTextured", (object.getMesh().getMaterial() != null) ? true : false);
	
	/*
	 * the actual draw function
	 * 
	 * GL_TRIANGLES - what we want to draw (triangles work great because
	 * pretty much any shape can be made from them
	 * 
	 * mesh.getIndices().length - how many indices we want to draw
	 * 
	 * GL11.GL_FLOAT - what type of numbers we want to use to draw
	 * 
	 * 0 - the pointer (we don't have a pointer so we can just pass in 0 and
	 * ignore this argument)
	 */
	GL11.glDrawElements(GL11.GL_TRIANGLES, object.getMesh().getIndices().length, GL11.GL_UNSIGNED_INT, 0);

	/*
	 * disable/unbind everything to free up system resources
	 * 
	 * the source code for shader.unbind() can be found in the Shader class
	 */
	shader.unbind();
	GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	GL30.glDisableVertexAttribArray(0);
	GL30.glDisableVertexAttribArray(1);
	GL30.glDisableVertexAttribArray(2);
	GL30.glBindVertexArray(0);
    }
}
