/*
 * the Box class
 * 
 * this class creates a box
 * 
 * NOTE: this class is incomplete
 * 
 * NOTE: this class doesn't actually hold most of the data that is used to make
 * the box. Instead, this class is used to take inputs and spit out a box mesh.
 * That means that if you later wanted data about the box, like it's
 * coordinates, you wouldn't be able to get that data from here
 * 
 * Aidin Yazdi
 */

package engine.objects;

import engine.graphics.Mesh;
import engine.maths.Vector3f;

public class Box {
    /*
     * return an array of meshes to make up a uniform box (all sides are the
     * same)
     */
    public static Mesh[] generateUniformBox(Vector3f topFrontLeft, float length, Vector3f color1, Vector3f color2,
	    Vector3f color3, Vector3f color4, String texture) {
	return null;
    }
}
