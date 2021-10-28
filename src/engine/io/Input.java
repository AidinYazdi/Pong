/*
 * the Input class
 * 
 * this class handles user inputs
 * 
 * Aidin Yazdi
 */

package engine.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class Input {
    /*
     * this boolean array represents every key on the keyboard
     * 
     * if a given element is true, that means its corresponding key is being
     * pressed down this frame. If a given element is false, that means its
     * corresponding key is not being pressed down this frame
     */
    private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
    /*
     * the same thing that was done with keys is now repeated with the mouse
     * buttons and stored in the variable buttons
     */
    private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    // these variables store the mouse's x and y positions
    private static double mouseX, mouseY;
    /*
     * these variables store the mouse's scroll positions
     * 
     * scrollX is for sideways scrolling and scrollY is for vertical scrolling
     */
    private static double scrollX, scrollY;

    /*
     * the following three variables are all callbacks that basically just
     * handle the different types of user input
     */
    private GLFWKeyCallback keyboard;
    private GLFWCursorPosCallback mouseMove;
    private GLFWMouseButtonCallback mouseButtons;
    // this one is for the mouse scrolling
    private GLFWScrollCallback mouseScroll;

    /*
     * the constructor method
     */
    public Input() {
	/*
	 * basically, every time a key is pressed this callback function is
	 * called
	 * 
	 * it seems like the callback functions here work a lot like callbacks
	 * do in Node.JS
	 */
	keyboard = new GLFWKeyCallback() {
	    public void invoke(long window, int key, int scancode, int action, int mods) {
		/*
		 * "key" is an integer which is passed into this method and
		 * represents the current key that we are checking
		 * 
		 * this will set the corresponding element to "key" in the
		 * keys[] array to true if that key is being pressed and false
		 * if that key is not being pressed
		 */
		keys[key] = (action != GLFW.GLFW_RELEASE);
	    }
	};
	/*
	 * this is the same thing that was done for keyboard (look above) but
	 * for mouseMove
	 */
	mouseMove = new GLFWCursorPosCallback() {
	    public void invoke(long window, double xPos, double yPos) {
		// set the new x and y positions of the mouse for the frame
		mouseX = xPos;
		mouseY = yPos;
	    }
	};
	/*
	 * this is the same thing that was done for keyboard (look above) but
	 * for mouseButtons
	 */
	mouseButtons = new GLFWMouseButtonCallback() {
	    public void invoke(long window, int button, int action, int mods) {
		// check the keyboard version of this for explanation comments
		buttons[button] = (action != GLFW.GLFW_RELEASE);
	    }
	};
	/*
	 * this is the same thing that was done for keyboard (look above) but
	 * for mouseScroll
	 */
	mouseScroll = new GLFWScrollCallback() {
	    public void invoke(long window, double offsetx, double offsety) {
		// I think scroll down will subtrack and scroll up will add
		scrollX += offsetx;
		scrollY += offsety;
	    }
	};
    }

    /*
     * this method will return true or false depending on whether or not the
     * given key is being pressed
     */
    public static boolean isKeyDown(int key) {
	return keys[key];
    }

    // if the given mouse button is down or not
    public static boolean isButtonDown(int button) {
	return buttons[button];
    }

    /*
     * this method destroys the callbacks (basically, after the inputs have been
     * processed we reset the callbacks so that those inputs don't remain in
     * there after the frame has been processed)
     */
    public void destroy() {
	keyboard.free();
	mouseMove.free();
	mouseButtons.free();
	mouseScroll.free();
    }

    /*
     * the following methods are getters:
     */
    public static double getMouseX() {
	return mouseX;
    }

    public static double getMouseY() {
	return mouseY;
    }

    public static double getScrollX() {
	return scrollX;
    }

    public static double getScrollY() {
	return scrollY;
    }

    public GLFWKeyCallback getKeyboardCallback() {
	return keyboard;
    }

    public GLFWCursorPosCallback getMouseMoveCallback() {
	return mouseMove;
    }

    public GLFWMouseButtonCallback getMouseButtonsCallback() {
	return mouseButtons;
    }

    public GLFWScrollCallback getMouseScrollCallback() {
	return mouseScroll;
    }
}
