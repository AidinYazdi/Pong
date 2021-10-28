/*
 * the Vector2f class
 * 
 * this class handles vectors in 2 dimensions
 * 
 * vectors, even though they can be described in terms of space, are also often
 * used to just represent numbers
 * 
 * Aidin Yazdi
 */

package engine.maths;

public class Vector2f {
    /*
     * the coordinates describing the vector
     * 
     * recall from math class - vectors start at (0,0) and go until the
     * coordinate (x,y)
     */
    private float x, y;

    // the constructor
    public Vector2f(float x, float y) {
	this.x = x;
	this.y = y;
    }

    // the following methods are used for vector math
    public static Vector2f add(Vector2f vector1, Vector2f vector2) {
	return new Vector2f(vector1.getX() + vector2.getX(), vector1.getY() + vector2.getY());
    }

    public static Vector2f subtract(Vector2f vector1, Vector2f vector2) {
	return new Vector2f(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY());
    }

    public static Vector2f multiply(Vector2f vector1, Vector2f vector2) {
	return new Vector2f(vector1.getX() * vector2.getX(), vector1.getY() * vector2.getY());
    }

    public static Vector2f divide(Vector2f vector1, Vector2f vector2) {
	return new Vector2f(vector1.getX() / vector2.getX(), vector1.getY() / vector2.getY());
    }

    // returns the length of the vector
    public static float length(Vector2f vector) {
	return (float) Math.sqrt((vector.getX() * vector.getX()) + (vector.getY() * vector.getY()));
    }

    // returns the vector but as a unit vector
    public static Vector2f normalize(Vector2f vector) {
	float len = Vector2f.length(vector);
	return Vector2f.divide(vector, new Vector2f(len, len));
    }

    // returns the dot product
    public static float dot(Vector2f vector1, Vector2f vector2) {
	return (vector1.getX() * vector2.getX()) + (vector1.getY() * vector2.getY());
    }

    // this method will help us compare values (even across classes)
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + Float.floatToIntBits(x);
	result = prime * result + Float.floatToIntBits(y);
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
	Vector2f other = (Vector2f) obj;
	if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
	    return false;
	if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
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
}
