/*
 * the Vertex class
 * 
 * this class represents the vertices that are used to render graphics
 * 
 * Aidin Yazdi
 */

package engine.graphics;

import engine.maths.Vector2f;
import engine.maths.Vector3f;

public class Vertex {
    /*
     * the position of the vertex is represented in 3D space using a 3D vector
     * 
     * the color of the vertex is represented as an RGB value using a 3D vector
     */
    private Vector3f position, color;

    // the coordinates for textures need to be represented by a 2D vector
    private Vector2f textureCoord;

    // the constructor
    public Vertex(Vector3f position, Vector3f color, Vector2f textureCoord) {
	this.position = position;
	this.color = color;
	this.textureCoord = textureCoord;
    }

    // a getter to get the position of the vertex
    public Vector3f getPosition() {
	return position;
    }

    // a getter to get the color of the vertex
    public Vector3f getColor() {
	return color;
    }

    // a getter to get the texture coordinates of the vertex
    public Vector2f getTextureCoord() {
	return textureCoord;
    }
}
