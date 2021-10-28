/*
 * the Rectangle class
 * 
 * this class creates a rectangle
 * 
 * Aidin Yazdi
 */

package engine.objects;

import engine.graphics.Material;
import engine.graphics.Mesh;
import engine.graphics.Vertex;
import engine.maths.Vector2f;
import engine.maths.Vector3f;

public class Rectangle {
    // standard measurements for rectangles
    public static float getStandardRectangleLength() {
	return 2.0f;
    }

    public static float getStandardRectangleWidth() {
	return 1.0f;
    }

    /*
     * return a rectangle mesh
     * 
     * xAngle is the angle, in degrees, that the rectangle should be rotated
     * about the x axis (with the topLeft coordinate of the rectangle serving as
     * (0,0)). The same is true of yAngle and zAngle
     */
    public static Mesh generateRectangle(Vector3f topLeft, float length, float width, Vector3f color1, Vector3f color2,
	    Vector3f color3, Vector3f color4, String texture) {
	/*
	 * set up the coordinates for the other corners (that aren't the top
	 * left corner - since that corner is passed in as a parameter)
	 */
	Vector3f bottomLeft = Vector3f.add(topLeft, new Vector3f(0.0f, -length, 0.0f));
	Vector3f bottomRight = Vector3f.add(bottomLeft, new Vector3f(width, 0.0f, 0.0f));
	Vector3f topRight = Vector3f.add(bottomRight, new Vector3f(0.0f, length, 0.0f));

	/*
	 * set up the material (either null if the path given is null, or an
	 * actual material)
	 * 
	 * this step must be done here because if we just pass in
	 * "new Material(texture)" as an argument, and the texture is null,
	 * instead of setting the material to null it will create a valid
	 * instance of the Material class - but just with the "path" variable
	 * set to null. This will cause our program to not realize that there is
	 * no material/texture attached to this mesh, so it will try to get the
	 * material/texture. However, since the path to the texture is null
	 * (since there is no texture) it will just start to throw a whole bunch
	 * of errors
	 */
	Material material;
	if (texture != null) {
	    material = new Material(texture);
	} else {
	    material = null;
	}

	/*
	 * set up the mesh of the rectangle given all the information and return
	 * it
	 */
	return new Mesh(new Vertex[]
	    {
		new Vertex(topLeft, color1, new Vector2f(0.0f, 0.0f)),
		new Vertex(bottomLeft, color2, new Vector2f(0.0f, 1.0f)),
		new Vertex(bottomRight, color3, new Vector2f(1.0f, 1.0f)),
		new Vertex(topRight, color4, new Vector2f(01.0f, 0.0f))
	    }, new int[]
	    {
		0,
		1,
		2,
		0,
		3,
		2
	    }, material);
    }

    // a function to handle moving a rectangle mesh
    public static Mesh moveRectangle(Mesh rectangle, Vector3f movement, String texture) {
	return Rectangle.generateRectangle(Vector3f.add(rectangle.getVertices()[0].getPosition(), movement),
		getStandardRectangleLength(), getStandardRectangleWidth(), rectangle.getVertices()[0].getColor(),
		rectangle.getVertices()[1].getColor(), rectangle.getVertices()[2].getColor(),
		rectangle.getVertices()[3].getColor(), texture);
    }
}
