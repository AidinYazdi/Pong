/*
 * the Shader class
 * 
 * this is a class that uses the shaders
 * 
 * Aidin Yazdi
 */

package engine.graphics;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import engine.maths.Matrix4f;
import engine.maths.Vector2f;
import engine.maths.Vector3f;
import engine.utils.FileUtils;

public class Shader {
    // the actual shader files (in String form)
    private String vertexFile, fragmentFile;
    /*
     * the vertexID and fragmentID are basically the compiled shaders
     * 
     * the programID is the program - basically all the shaders put together to
     * send to the GPU to be rendered
     */
    private int vertexID, fragmentID, programID;

    // the constructor
    public Shader(String vertexPath, String fragmentPath) {
	/*
	 * use our FileUtils class to store the shader files in our variables
	 * using the paths
	 */
	this.vertexFile = FileUtils.loadAsString(vertexPath);
	this.fragmentFile = FileUtils.loadAsString(fragmentPath);
    }

    // this method actually creates and compiles the shaders and the program
    public void create() {
	// create the program
	programID = GL20.glCreateProgram();

	// create the vertex shader
	vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
	// link the vertex shader to its source file
	GL20.glShaderSource(vertexID, vertexFile);
	// compile the vertex shader
	GL20.glCompileShader(vertexID);
	// check for errors
	if (GL20.glGetShaderi(vertexID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
	    System.err.println("The Vertex Shader did not compile: " + GL20.glGetShaderInfoLog(vertexID));
	    return;
	}

	/*
	 * this is basically the same as the vertex shader, so refer to the
	 * above code for comments
	 */
	fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
	GL20.glShaderSource(fragmentID, fragmentFile);
	GL20.glCompileShader(fragmentID);
	if (GL20.glGetShaderi(fragmentID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
	    System.err.println("The Fragment Shader did not compile: " + GL20.glGetShaderInfoLog(fragmentID));
	    return;
	}

	// actually attach the compiled shaders to the program
	GL20.glAttachShader(programID, vertexID);
	GL20.glAttachShader(programID, fragmentID);

	/*
	 * links the program (I think this just makes it so that the program can
	 * be used)
	 */
	GL20.glLinkProgram(programID);
	// check for errors
	if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
	    System.err.println("The Program did not link correctly: " + GL20.glGetProgramInfoLog(programID));
	    return;
	}

	// validates that the program is a valid program
	GL20.glValidateProgram(programID);
	// check for errors
	if (GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
	    System.err.println("The Program did not validate correctly: " + GL20.glGetProgramInfoLog(programID));
	    return;
	}
    }

    // this function will return the location of the uniform
    public int getUniformLocation(String name) {
	return GL20.glGetUniformLocation(programID, name);
    }

    /*
     * the following six methods use method overriding to set up different types
     * of uniforms using different data types
     */
    public void setUniform(String name, float value) {
	GL20.glUniform1f(getUniformLocation(name), value);
    }

    public void setUniform(String name, int value) {
	GL20.glUniform1i(getUniformLocation(name), value);
    }

    public void setUniform(String name, boolean value) {
	/*
	 * since we can't pass in a boolean, we pass in 1 if value is true and 0
	 * if it's false
	 */
	GL20.glUniform1i(getUniformLocation(name), value ? 1 : 0);
    }

    public void setUniform(String name, Vector2f value) {
	GL20.glUniform2f(getUniformLocation(name), value.getX(), value.getY());
    }

    public void setUniform(String name, Vector3f value) {
	GL20.glUniform3f(getUniformLocation(name), value.getX(), value.getY(), value.getZ());
    }

    public void setUniform(String name, Matrix4f value) {
	// create a buffer to store the matrix in
	FloatBuffer matrix = MemoryUtil.memAllocFloat(Matrix4f.SIZE * Matrix4f.SIZE);
	// store the matrix in the buffer so that it is formatted it correctly
	matrix.put(value.getAll()).flip();
	// put the matrix buffer that we've created in a uniform
	GL20.glUniformMatrix4fv(getUniformLocation(name), true, matrix);
    }

    // this method will bind the shader to whatever we're drawing
    public void bind() {
	GL20.glUseProgram(programID);
    }

    /*
     * this method will unbind the shader after we're done drawing with it (I
     * think this just stops the program from being used so that we can delete
     * it or use it to draw something else)
     */
    public void unbind() {
	GL20.glUseProgram(0);
    }

    // this method deletes the program to free up system resources
    public void destroy() {
	GL20.glDetachShader(programID, vertexID);
	GL20.glDetachShader(programID, fragmentID);
	GL20.glDeleteShader(vertexID);
	GL20.glDeleteShader(fragmentID);
	GL20.glDeleteProgram(programID);
    }
}
