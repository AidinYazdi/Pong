/*
 * the Material class
 * 
 * this class holds all the information about textures
 * 
 * Aidin Yazdi
 */

package engine.graphics;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Material {
    /*
     * a slick-util texture (slick-util is a library that we have included
     * separately from LWJGL)
     */
    private Texture texture;

    // the file path to the texture
    private String path;

    // the width and height of the texture
    private float width, height;

    // the texture ID
    private int textureID;

    /*
     * the constructor
     * 
     * "path" is the path to the given texture
     */
    public Material(String path) {
	this.path = path;
    }

    // set up all the information about the texture
    public void create() {
	/*
	 * the first argument is the type of file that will be used to load the
	 * texture (.png, .jpeg, etc.)
	 * 
	 * the last argument can be GL11.GL_NEAREST for more rigid images or
	 * GL11.GL_LINEAR for images that will be blurred to fit the given mesh
	 */
	try {
	    texture = TextureLoader.getTexture(path.split("[.]")[1], Material.class.getResourceAsStream(path),
		    GL11.GL_NEAREST);
	    // general information about the texture that we need to set up
	    width = texture.getWidth();
	    height = texture.getHeight();
	    textureID = texture.getTextureID();
	} catch (IOException e) {
	    System.err.println("Can't find the texture at " + path);
	}
    }

    // destroy the texture (I think this frees up system resources)
    public void destroy() {
	GL13.glDeleteTextures(textureID);
    }

    // the following methods are getters
    public float getWidth() {
	return width;
    }

    public float getHeight() {
	return height;
    }

    public int getTextureID() {
	return textureID;
    }

    public String getPath() {
	return path;
    }
}