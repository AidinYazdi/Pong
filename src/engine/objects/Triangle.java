/*
 * the Triangle class
 * 
 * this class creates a triangle
 * 
 * NOTE: check the Square class source code for notes about these types of
 * classes
 * 
 * Aidin Yazdi
 */

package engine.objects;

import engine.graphics.Material;
import engine.graphics.Mesh;
import engine.graphics.Vertex;
import engine.maths.Vector2f;
import engine.maths.Vector3f;

public class Triangle {
    /*
     * return a triangle mesh
     */
    public static Mesh generateTriangle(Vector3f topLeft, float xAngle, float yAngle, float zAngle, float length,
	    Vector3f color1, Vector3f color2, Vector3f color3, String texture) {
	/*
	 * set up the coordinates for the other corners (that aren't the top
	 * left corner - since that corner is passed in as a parameter)
	 */
	Vector3f bottomLeft = Vector3f.add(topLeft, new Vector3f(0.0f, -length, 0.0f));
	Vector3f bottomRight = Vector3f.add(bottomLeft, new Vector3f(length, 0.0f, 0.0f));

	/*
	 * set up the material (either null if the path given is null, or an
	 * actual material)
	 * 
	 * check the Square class for more details
	 */
	Material material;
	if (texture != null) {
	    material = new Material(texture);
	} else {
	    material = null;
	}

	// set up the mesh of the square given all the information and return it
	return new Mesh(new Vertex[]
	    {
		new Vertex(topLeft, color1, new Vector2f(0.0f, 0.0f)),
		new Vertex(bottomLeft, color2, new Vector2f(0.0f, 1.0f)),
		new Vertex(bottomRight, color3, new Vector2f(1.0f, 1.0f))
	    }, new int[]
	    {
		0,
		1,
		2
	    }, material);
    }
}
