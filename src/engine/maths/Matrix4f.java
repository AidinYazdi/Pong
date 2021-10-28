/*
 * the Matrix4f class
 * 
 * this class represents a 4x4 matrix (meaning, 4 different sets of 4 numbers)
 * 
 * Aidin Yazdi
 */

package engine.maths;

import java.util.Arrays;

public class Matrix4f {
    // how big the matrix can be
    public static final int SIZE = 4;
    // the elements in the matrix
    private float[] elements = new float[SIZE * SIZE];

    // the identity matrix - used for math
    public static Matrix4f identity() {
	// initialize the matrix
	Matrix4f result = new Matrix4f();

	// set all the values to zero
	for (int i = 0; i < SIZE; i++) {
	    for (int j = 0; j < SIZE; j++) {
		result.set(i, j, 0.0f);
	    }
	}

	/*
	 * set the elements in the diagonal of the matrix to 1 (recall from math
	 * class that this is what an identity matrix is)
	 */
	result.set(0, 0, 1);
	result.set(1, 1, 1);
	result.set(2, 2, 1);
	result.set(3, 3, 1);

	return result;
    }

    /*
     * this is a translation matrix used for moving vectors (recall from Linear
     * Algebra class)
     */
    public static Matrix4f translate(Vector3f translate) {
	// initialize the matrix
	Matrix4f result = Matrix4f.identity();

	// change the identity matrix into a translation matrix
	result.set(3, 0, translate.getX());
	result.set(3, 1, translate.getY());
	result.set(3, 2, translate.getZ());

	return result;
    }

    /*
     * this is a rotation matrix used for rotating vectors (recall from Linear
     * Algebra class)
     * 
     * the angle should be put in degrees (the method will convert it to radians
     * for the math)
     */
    public static Matrix4f rotate(float angle, Vector3f axis) {
	// initialize the matrix
	Matrix4f result = Matrix4f.identity();

	/*
	 * the following is all the math involved in the rotation. Because this
	 * is so heavily math based, I'm not going to comment it. Look up
	 * rotation matrices that use an axis and angle for more information
	 */
	float cos = (float) Math.cos(Math.toRadians(angle));
	float sin = (float) Math.sin(Math.toRadians(angle));
	float C = 1 - cos;

	result.set(0, 0, cos + axis.getX() * axis.getX() * C);
	result.set(0, 1, axis.getX() * axis.getY() * C - axis.getZ() * sin);
	result.set(0, 2, axis.getX() * axis.getZ() * C + axis.getY() * sin);
	result.set(1, 0, axis.getY() * axis.getX() * C + axis.getZ() * sin);
	result.set(1, 1, cos + axis.getY() * axis.getY() * C);
	result.set(1, 2, axis.getY() * axis.getZ() * C - axis.getX() * sin);
	result.set(2, 0, axis.getZ() * axis.getX() * C - axis.getY() * sin);
	result.set(2, 1, axis.getZ() * axis.getY() * C + axis.getX() * sin);
	result.set(2, 2, cos + axis.getZ() * axis.getZ() * C);

	return result;
    }

    // a scalar matrix
    public static Matrix4f scale(Vector3f scalar) {
	// initialize the matrix
	Matrix4f result = Matrix4f.identity();

	// change the identity matrix into a scalar matrix
	result.set(0, 0, scalar.getX());
	result.set(1, 1, scalar.getY());
	result.set(2, 2, scalar.getZ());

	return result;
    }

    /*
     * a method to create a matrix which will be used as the uniform in the
     * vertex shader to transform the mesh
     */
    public static Matrix4f transform(Vector3f position, Vector3f rotation, Vector3f scale) {
	// initialize the matrix
	Matrix4f result = Matrix4f.identity();

	/*
	 * initialize the matrices needed for the calculations to do the
	 * transformation
	 * 
	 * the rotation matrices for the x, y, and z axis must be unit vectors
	 * (length of 1)
	 */
	Matrix4f translationMatrix = Matrix4f.translate(position);
	Matrix4f rotXMatrix = Matrix4f.rotate(rotation.getX(), new Vector3f(1, 0, 0));
	Matrix4f rotYMatrix = Matrix4f.rotate(rotation.getY(), new Vector3f(0, 1, 0));
	Matrix4f rotZMatrix = Matrix4f.rotate(rotation.getZ(), new Vector3f(0, 0, 1));
	Matrix4f scaleMatrix = Matrix4f.scale(scale);

	/*
	 * do the calculations (look it up if you want an explanation, I'm not
	 * going to go into the mathematical details here)
	 */
	Matrix4f rotationMatrix = Matrix4f.multiply(rotXMatrix, Matrix4f.multiply(rotYMatrix, rotZMatrix));

	result = Matrix4f.multiply(translationMatrix, Matrix4f.multiply(rotationMatrix, scaleMatrix));

	return result;
    }

    /*
     * the projection matrix
     * 
     * the first argument is the field of view (FOV - how many degrees around
     * can you see - this is normally set to 70 degrees)
     * 
     * the second argument is the aspect ratio of our window
     * 
     * the third argument is how far things can be away from the viewer before
     * they stop being shown
     * 
     * the fourth argument is how close to the viewer things can be before they
     * stop being shown (an object that is really close to the viewer might not
     * be rendered)
     */
    public static Matrix4f projection(float fov, float aspect, float near, float far) {
	Matrix4f result = Matrix4f.identity();

	/*
	 * I'm not going to go into the math here. He is a video that explains
	 * it: https://www.youtube.com/watch?v=ih20l3pJoeU&t=1026s
	 */
	float tanFOV = (float) Math.tan(Math.toRadians(fov / 2));
	float range = far - near;

	result.set(0, 0, 1.0f / (aspect * tanFOV));
	result.set(1, 1, 1.0f / tanFOV);
	result.set(2, 2, -((far + near) / range));
	result.set(2, 3, -1);
	result.set(3, 2, -((2.0f * far * near) / range));
	result.set(3, 3, 0);

	return result;
    }

    /*
     * the view matrix
     * 
     * the first argument is the position of the camera
     * 
     * the second argument is the rotation of the camera
     */
    public static Matrix4f view(Vector3f position, Vector3f rotation) {
	// initialize the matrix
	Matrix4f result = Matrix4f.identity();

	/*
	 * initialize the matrices needed for the calculations to do the
	 * transformation
	 * 
	 * the rotation matrices for the x, y, and z axis must be unit vectors
	 * (length of 1)
	 * 
	 * these calculations are basically the same thing as a translation but
	 * in reverse. For instance, if the camera is moved to the left what
	 * we're really doing is moving everything else to the right. Also, we
	 * don't need to handle scaling with the camera since it will stay in
	 * constant scale
	 */
	Vector3f negative = new Vector3f(-position.getX(), -position.getY(), -position.getZ());
	Matrix4f translationMatrix = Matrix4f.translate(negative);
	Matrix4f rotXMatrix = Matrix4f.rotate(rotation.getX(), new Vector3f(1, 0, 0));
	Matrix4f rotYMatrix = Matrix4f.rotate(rotation.getY(), new Vector3f(0, 1, 0));
	Matrix4f rotZMatrix = Matrix4f.rotate(rotation.getZ(), new Vector3f(0, 0, 1));

	/*
	 * do the calculations (look it up if you want an explanation, I'm not
	 * going to go into the mathematical details here)
	 */
	Matrix4f rotationMatrix = Matrix4f.multiply(rotZMatrix, Matrix4f.multiply(rotYMatrix, rotXMatrix));

	result = Matrix4f.multiply(translationMatrix, rotationMatrix);

	return result;
    }

    // a method to multiply two matrices
    public static Matrix4f multiply(Matrix4f matrix, Matrix4f other) {
	// initialize the matrix
	Matrix4f result = Matrix4f.identity();

	/*
	 * multiply the two matrices into the result matrix
	 * 
	 * look up matrix multiplication if you're confused
	 */
	for (int i = 0; i < SIZE; i++) {
	    for (int j = 0; j < SIZE; j++) {
		result.set(i, j, matrix.get(i, 0) * other.get(0, j) + matrix.get(i, 1) * other.get(1, j)
			+ matrix.get(i, 2) * other.get(2, j) + matrix.get(i, 3) * other.get(3, j));
	    }
	}

	return result;
    }

    // this method will help us compare values (even across classes)
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + Arrays.hashCode(elements);
	return result;
    }

    // this method will check if another object equals this matrix object
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Matrix4f other = (Matrix4f) obj;
	if (!Arrays.equals(elements, other.elements))
	    return false;
	return true;
    }

    /*
     * to get an element of the matrix
     * 
     * we are treating this matrix like it's a 4x4 matrix. But, the actual array
     * elements[] is 1D. So, we use some math to store and retrieve elements
     * from the array so that even though it's a 1D array we can treat it like a
     * 2D array of dimensions 4x4
     * 
     * x and y represent the index of the matrix that is being requested
     */
    public float get(int x, int y) {
	return elements[(y * SIZE) + x];
    }

    /*
     * similar to the get method, this method treats the 1D array like it's 2D
     * and uses the given x and y indices to store the value like it's a 2D
     * array
     */
    public void set(int x, int y, float value) {
	elements[(y * SIZE) + x] = value;
    }

    // this method returns everything in the matrix
    public float[] getAll() {
	return elements;
    }
}
