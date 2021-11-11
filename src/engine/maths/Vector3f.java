/*
 * the Vector3f class
 * 
 * this class handles vectors in 3 dimensions
 * 
 * vectors, even though they can be described in terms of space, are also often
 * used to just represent numbers (like an RGB value)
 * 
 * Aidin Yazdi
 */

package engine.maths;

public class Vector3f {
    /*
     * the coordinates describing the vector
     * 
     * recall from math class - vectors start at (0,0,0) and go until the
     * coordinate (x,y,z)
     */
    private float x, y, z;

    // the constructor
    public Vector3f(float x, float y, float z) {
	this.x = x;
	this.y = y;
	this.z = z;
    }

    // this will allow a vector to be reset even after it's been constructed
    public void set(float x, float y, float z) {
	this.x = x;
	this.y = y;
	this.z = z;
    }

    /*
     * the following methods (until the getters and setters) are methods to do
     * math with the vectors
     */
    public static Vector3f add(Vector3f vector1, Vector3f vector2) {
	return new Vector3f(vector1.getX() + vector2.getX(), vector1.getY() + vector2.getY(),
		vector1.getZ() + vector2.getZ());
    }

    public static Vector3f subtract(Vector3f vector1, Vector3f vector2) {
	return new Vector3f(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY(),
		vector1.getZ() - vector2.getZ());
    }

    public static Vector3f multiply(Vector3f vector1, Vector3f vector2) {
	return new Vector3f(vector1.getX() * vector2.getX(), vector1.getY() * vector2.getY(),
		vector1.getZ() * vector2.getZ());
    }

    public static Vector3f divide(Vector3f vector1, Vector3f vector2) {
	return new Vector3f(vector1.getX() / vector2.getX(), vector1.getY() / vector2.getY(),
		vector1.getZ() / vector2.getZ());
    }

    // returns the length of the vector
    public static float length(Vector3f vector) {
	return (float) Math.sqrt(
		(vector.getX() * vector.getX()) + (vector.getY() * vector.getY()) + (vector.getZ() * vector.getZ()));
    }

    // returns the vector but as a unit vector
    public static Vector3f normalize(Vector3f vector) {
	float len = Vector3f.length(vector);
	return Vector3f.divide(vector, new Vector3f(len, len, len));
    }

    // returns the dot product
    public static float dot(Vector3f vector1, Vector3f vector2) {
	return (vector1.getX() * vector2.getX()) + (vector1.getY() * vector2.getY())
		+ (vector1.getZ() * vector2.getZ());
    }

    // prints the vector (useful for debbuging)
    public void print() {
	System.out.println("(" + x + ", " + y + ", " + z + ")");
    }

    // this method will help us compare values (even across classes)
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + Float.floatToIntBits(x);
	result = prime * result + Float.floatToIntBits(y);
	result = prime * result + Float.floatToIntBits(z);
	return result;
    }

    // this method will check if another object equals this vector object
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Vector3f other = (Vector3f) obj;
	if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
	    return false;
	if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
	    return false;
	if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
	    return false;
	return true;
    }

    // the following methods are getters and setters
    public float getX() {
	return x;
    }

    public void setX(float x) {
	this.x = x;
    }

    public float getY() {
	return y;
    }

    public void setY(float y) {
	this.y = y;
    }

    public float getZ() {
	return z;
    }

    public void setZ(float z) {
	this.z = z;
    }
}
