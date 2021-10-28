/*
 * the Circle class
 * 
 * this class creates a circle
 * 
 * NOTE: this class doesn't actually hold most of the data that is used to make
 * the circle. Instead, this class is used to take inputs and spit out a circle
 * mesh. That means that if you later wanted data about the circle, like it's
 * coordinates, you wouldn't be able to get that data from here
 * 
 * Aidin Yazdi
 */

package engine.objects;

import engine.graphics.Mesh;
import engine.maths.Vector3f;

public class Circle {
    // standard measurements for circles
    public static float getStandardCircleRadius() {
	return 1.0f;
    }

    // return a circle mesh
    public static Mesh[] generateCircle(Vector3f center, float radius, int indices, Vector3f color) {
	// create an array to store all the top left corners of the squares in
	Vector3f[] vectors = new Vector3f[indices];
	/*
	 * create an array to store all the z-axis angles the squares should be
	 * tilted at in
	 */
	float[] zAngles = new float[indices];
	/*
	 * store the coordinates of the center of the circle to use them for the
	 * math later
	 * 
	 * also store the length of the circle
	 */
	float x = center.getX();
	float y = center.getY();
	float z = center.getX();
	float l = (float) Math.sqrt(2) * radius;

	/*
	 * iterate a bunch of times to create all the top left vectors for the
	 * squares and store the z angle that each square should be at
	 */
	for (int i = 0; i < indices; i++) {
	    float xPrime, yPrime;
	    float theta = (float) ((90.0f / (float) indices) * (float) i);
	    float thetaPrime = (180.0f - 45.0f) - theta;

	    xPrime = (float) (radius * Math.cos(Math.toRadians(thetaPrime))) + x;
//	    yPrime = (float) ((xPrime - x)
//		    * Math.tan(Math.toRadians(((180.0f - theta) / 2.0f) - ((180 - (theta * 2.0f)) / 2.0f)))) + y;
	    yPrime = (float) (radius * Math.sin(Math.toRadians(thetaPrime))) + y;

	    vectors[i] = new Vector3f(xPrime, yPrime, z);
//	    zAngles[i] = (float) (1.0f * ((90.0f / (float) indices) * (float) i));
	    zAngles[i] = -1.0f * theta;
	}

	// make all the square meshes
	Mesh[] squareMeshes = new Mesh[indices];
	for (int i = 0; i < vectors.length; i++) {
	    squareMeshes[i] = Square.generateSquare(vectors[i], new Vector3f(0.0f, 0.0f, zAngles[i]), l, color, color,
		    color, color, null);
	}

	// return the square meshes (which make up the circle mesh)
	return squareMeshes;
    }
}
