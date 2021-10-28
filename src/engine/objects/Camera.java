/*
 * the Camera class
 * 
 * this is the class that holds all the information for the camera
 * 
 * Aidin Yazdi
 */

package engine.objects;

import org.lwjgl.glfw.GLFW;

import engine.io.Input;
import engine.maths.Vector3f;

public class Camera {
    private Vector3f position, rotation;
    private float moveSpeed = 0.1f, mouseSensitivity = 0.20f, rotateSpeed = 1.5f;
    private double oldMouseX = 0, oldMouseY = 0, newMouseX, newMouseY;

    // the constructor
    public Camera(Vector3f position, Vector3f rotation) {
	this.position = position;
	this.rotation = rotation;
    }

    /*
     * move and rotating the camera (this will often make it look like
     * everything else is moving)
     * 
     * we are currently moving the camera based on WASD
     */
    public void update() {
	/*
	 * these values will help with camera movement working even if the
	 * camera is off-axis (meaning, if the camera is at an angle, then we
	 * don't want 'w' to go forward. Rather, we want it to go at the same
	 * angle the camera is at)
	 */
	float x = (float) Math.sin(Math.toRadians(rotation.getY())) * moveSpeed;
	float z = (float) Math.cos(Math.toRadians(rotation.getY())) * moveSpeed;

	// camera movement
	if (Input.isKeyDown(GLFW.GLFW_KEY_A)) {
	    position = Vector3f.add(position, new Vector3f(-z, 0, x));
	}
	if (Input.isKeyDown(GLFW.GLFW_KEY_D)) {
	    position = Vector3f.add(position, new Vector3f(z, 0, -x));
	}
	if (Input.isKeyDown(GLFW.GLFW_KEY_W)) {
	    position = Vector3f.add(position, new Vector3f(-x, 0, -z));
	}
	if (Input.isKeyDown(GLFW.GLFW_KEY_S)) {
	    position = Vector3f.add(position, new Vector3f(x, 0, z));
	}
	if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
	    position = Vector3f.add(position, new Vector3f(0, moveSpeed, 0));
	}
	if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
	    position = Vector3f.add(position, new Vector3f(0, -moveSpeed, 0));
	}

	// rotate the camera
	rotateWithArrowKeys();
    }

    // rotate the camera with the mouse
    public void rotateWithMouse() {
	// get the new mouseX and mouseY values
	newMouseX = Input.getMouseX();
	newMouseY = Input.getMouseY();
	// first, get the difference in mouseX and mouseY values
	float dx = (float) (newMouseX - oldMouseX);
	float dy = (float) (newMouseY - oldMouseY);
	// actually do the rotation
	rotation = Vector3f.add(rotation, new Vector3f((-dy) * mouseSensitivity, (-dx) * mouseSensitivity, 0));
	// update the oldMouseX and oldMouseY values for the next frame to use
	oldMouseX = newMouseX;
	oldMouseY = newMouseY;
    }

    // rotate the camera with the arrow keys
    public void rotateWithArrowKeys() {
	if (Input.isKeyDown(GLFW.GLFW_KEY_UP)) {
	    rotation = Vector3f.add(rotation, new Vector3f(rotateSpeed, 0, 0));
	}
	if (Input.isKeyDown(GLFW.GLFW_KEY_DOWN)) {
	    rotation = Vector3f.add(rotation, new Vector3f(-rotateSpeed, 0, 0));
	}
	if (Input.isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
	    rotation = Vector3f.add(rotation, new Vector3f(0, -rotateSpeed, 0));
	}
	if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT)) {
	    rotation = Vector3f.add(rotation, new Vector3f(0, rotateSpeed, 0));
	}
    }

    // the following methods are getters
    public Vector3f getPosition() {
	return position;
    }

    public Vector3f getRotation() {
	return rotation;
    }
}
