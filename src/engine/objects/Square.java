/*
 * the Square class
 * 
 * this class creates a square
 * 
 * NOTE: this class doesn't actually hold most of the data that is used to make
 * the square. Instead, this class is used to take inputs and spit out a square
 * mesh. That means that if you later wanted data about the square, like it's
 * coordinates, you wouldn't be able to get that data from here
 * 
 * NOTE: for some reason rotating the square starts messing up if it's rotated
 * along more than one axis
 * 
 * Aidin Yazdi
 */

package engine.objects;

import engine.graphics.Material;
import engine.graphics.Mesh;
import engine.graphics.Vertex;
import engine.maths.MiscMath;
import engine.maths.Vector2f;
import engine.maths.Vector3f;

public class Square {
    // standard measurements for squares
    public static float getStandardSquareLength() {
	return 1.0f;
    }

    /*
     * return a square mesh
     * 
     * xAngle is the angle, in degrees, that the square should be rotated about
     * the x axis (with the topLeft coordinate of the square serving as (0,0)).
     * The same is true of yAngle and zAngle
     */
    public static Mesh generateSquare(Vector3f topLeft, Vector3f angles, float length, Vector3f color1, Vector3f color2,
	    Vector3f color3, Vector3f color4, String texture) {
	/*
	 * set up the coordinates for the other corners (that aren't the top
	 * left corner - since that corner is passed in as a parameter)
	 */
	Vector3f bottomLeft = Vector3f.add(topLeft, new Vector3f(0.0f, -length, 0.0f));
	Vector3f bottomRight = Vector3f.add(bottomLeft, new Vector3f(length, 0.0f, 0.0f));
	Vector3f topRight = Vector3f.add(bottomRight, new Vector3f(0.0f, length, 0.0f));

	// rotate the corners
	bottomLeft = rotations(topLeft, bottomLeft, angles);
	bottomRight = rotations(topLeft, bottomRight, angles);
	topRight = rotations(topLeft, topRight, angles);

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

	// set up the mesh of the square given all the information and return it
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

    // a function to handle to rotations
    public static Vector3f rotations(Vector3f topLeft, Vector3f vector, Vector3f angles) {
	// get the angles in radian form to make the math easier
	float x = (float) Math.toRadians(angles.getX());
	float y = (float) Math.toRadians(angles.getY());
	float z = (float) Math.toRadians(angles.getZ());

	// matrices needed to do the rotations
	float[][] Rx =
	    {
		{
		    1.0f,
		    0.0f,
		    0.0f
		},
		{
		    0.0f,
		    (float) Math.cos(x),
		    (float) Math.sin(x)
		},
		{
		    0.0f,
		    (float) (-1.0f * Math.sin(x)),
		    (float) Math.cos(x)
		}
	    };
	float[][] Ry =
	    {
		{
		    (float) Math.cos(y),
		    0.0f,
		    (float) (-1.0f * Math.sin(y))
		},
		{
		    0.0f,
		    1.0f,
		    0.0f
		},
		{
		    (float) Math.sin(y),
		    0.0f,
		    (float) Math.cos(y)
		}
	    };
	float[][] Rz =
	    {
		{
		    (float) Math.cos(z),
		    (float) Math.sin(z),
		    0.0f
		},
		{
		    (float) (-1.0f * Math.sin(z)),
		    (float) Math.cos(z),
		    0.0f
		},
		{
		    0.0f,
		    0.0f,
		    1.0f
		}
	    };

	/*
	 * format the vector that we're trying to rotate as a matrix so that we
	 * can do the matrix multiplication
	 * 
	 * also, format the matrix such that it only represents the vector from
	 * the top left to this point (meaning, if this point is 1 unit below
	 * the top left, even if it's 500 units down in terms of the space of
	 * the game engine, set the y coordinate to -1 since we only care about
	 * the placement of this vector in relation to the top left vector. The
	 * reason for this is that we're doing our rotations around the top left
	 * vector - meaning that we're assuming that (0, 0, 0) is at the top
	 * left vector for the purposes of the rotations)
	 */
	float[][] matrix =
	    {
		{
		    vector.getX() - topLeft.getX(),
		    vector.getY() - topLeft.getY(),
		    vector.getZ() - topLeft.getZ()
		}
	    };

	/*
	 * actually do the rotation in all three dimension (which is just matrix
	 * multiplication multiple times)
	 */
	matrix = MiscMath.multiplyMatrices(Rz, MiscMath.multiplyMatrices(Ry, MiscMath.multiplyMatrices(Rx, matrix)));

	/*
	 * return the result plus the top left vector (which we had subtracted
	 * earlier)
	 */
	return new Vector3f(matrix[0][0] + topLeft.getX(), matrix[0][1] + topLeft.getY(),
		matrix[0][2] + topLeft.getZ());
    }

    // a function to handle moving a square mesh
    public static Mesh moveSquare(Mesh square, Vector3f movement, String texture) {
	return Square.generateSquare(Vector3f.add(square.getVertices()[0].getPosition(), movement),
		new Vector3f(0.0f, 0.0f, 0.0f), getStandardSquareLength(), square.getVertices()[0].getColor(),
		square.getVertices()[1].getColor(), square.getVertices()[2].getColor(),
		square.getVertices()[3].getColor(), texture);
    }
}
