/*
 * the GameObject class
 * 
 * objects are what we call the things that will actually appear on screen for
 * the player during the game. This is the main object that all those things
 * will be kept in
 * 
 * Aidin Yazdi
 */

package engine.objects;

import engine.graphics.Mesh;
import engine.maths.Vector3f;

public class GameObject {
    private Vector3f position, rotation, scale;
    private Mesh mesh;

    // the constructor
    public GameObject(Vector3f position, Vector3f rotation, Vector3f scale, Mesh mesh) {
	this.position = position;
	this.rotation = rotation;
	this.scale = scale;
	this.mesh = mesh;
    }
    
    // temporary function to test vector math
    public void update () {
	position.setZ((float) (position.getZ() - 0.05f));
    }

    // the following methods are getters
    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Mesh getMesh() {
        return mesh;
    }
}
