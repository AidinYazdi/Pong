/*
 * the Window class
 * 
 * this class uses mostly GLFW to create the window that the game will be played
 * in
 * 
 * Aidin Yazdi
 */

package engine.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import engine.maths.Matrix4f;
import engine.maths.Vector3f;

// the Window class
public class Window {
    // these variables keep track of how many FPS we are doing
    private int frames;
    private static long time;

    /*
     * this is an object which handles inputs. Check the source file for
     * explanation
     */
    private Input input;

    /*
     * these are the variables that store the color of the background (they're
     * float since colors are stored between 0 and 1 in OpenGL)
     * 
     * since the background color needs to be described in the RGB color space,
     * we can describe it with a 3 dimensional vector where x = R, y = G, and z
     * = B
     * 
     * the background color is referred to as the "clear color" in OpenGL
     * terminology
     * 
     * in order to keep the code from throwing errors, the background vector is
     * initialized to (0,0,0) (but it can be changed later)
     */
    private Vector3f background = new Vector3f(0, 0, 0);

    // the width and the height of the window
    private int width, height;
    // the title of the window
    private String title;

    /*
     * this is the window variable itself. The reason it is a type "long" is
     * because of how Java interacts with the GLFW code which was originally
     * written for C++
     */
    private long window;

    // this is a callback for the window that will resize the window
    private GLFWWindowSizeCallback sizeCallback;

    /*
     * this boolean tells us whether or not the window has been resized (in
     * which case it needs to be updated to the new size)
     */
    private boolean isResized;
    /*
     * whether or not the window is fullscreen (kinda like the variable
     * isResized)
     */
    private boolean isFullscreen;

    // the window position
    private int[] windowPosX = new int[1], windowPosY = new int[1];

    // the projection matrix
    private Matrix4f projection;

    // the Window constructor
    public Window(int width, int height, String title) {
	this.width = width;
	this.height = height;
	this.title = title;
	/*
	 * the first argument to the projection matrix is the field of view (in
	 * degrees)
	 * 
	 * the third argument is how near something can be before it's not shown
	 * to the viewer
	 * 
	 * the forth argument is how far something can be before it's not shown
	 * to the viewer
	 */
	projection = Matrix4f.projection(70.0f, ((float) width) / ((float) height), 0.1f, 1000.0f);
    }

    // this is the method that actually creates the window
    public void create() {
	// this creates our input object as an instance of the Input class
	input = new Input();

	/*
	 * initializes GLFW
	 * 
	 * since the function GLFW.glfwInit() evaluates to a boolean that says
	 * whether or not GLFW was initialized, we can use the function to also
	 * test whether or not GLFW was initialized and throw the user an error
	 * if it wasn't
	 */
	if (GLFW.glfwInit()) {
	    System.out.println("GLFW initialized");
	} else {
	    System.err.println("ERROR: GLFW was not initialized");
	    return;
	}

	// configures the version of OpenGL to work with Linux
	GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
	GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);
	GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
	GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);

	/*
	 * this turns our window variable into an actual window
	 * 
	 * if the window is in fullscreen mode, then we will tell the window
	 * that
	 */
	window = GLFW.glfwCreateWindow(width, height, title, isFullscreen ? GLFW.glfwGetPrimaryMonitor() : 0, 0);
	// error checking to make sure that the window was actually created
	if (window == 0) {
	    System.err.println("ERROR: window was not created");
	    return;
	} else {
	    System.out.println("window created");
	}

	/*
	 * these next two lines set a position for the window within the primary
	 * monitor of the user
	 * 
	 * the first line basically sets the variable videoMode to contain data
	 * about the primary monitor
	 * 
	 * the second line tells the window to actually display in the middle of
	 * the primary monitor
	 * 
	 * videoMode.width is the actual width, in pixels, of the primary
	 * monitor. The variable "width" is the width that we want out window to
	 * be (it is a private variable of the class Window - the variable
	 * definition can be found above). The same holds true for
	 * videoMode.height and the variable "height". The math that is done
	 * here basically centers the window within the primary monitor
	 */
	GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
	windowPosX[0] = (videoMode.width() - width) / 2;
	windowPosY[0] = (videoMode.height() - height) / 2;
	GLFW.glfwSetWindowPos(window, windowPosX[0], windowPosY[0]);

	/*
	 * this makes our current window the current context
	 * 
	 * basically, it tells GLFW to run all of it's functions on our window
	 */
	GLFW.glfwMakeContextCurrent(window);

	/*
	 * this makes the current context, which is now the window, an OpenGL
	 * window (basically, now we can render to it)
	 */
	GL.createCapabilities();

	// this just allows vertices to be shown correctly in 3D
	GL11.glEnable(GL11.GL_DEPTH_TEST);

	/*
	 * this will create all the callbacks for the window. The actual
	 * function that does this is written below
	 */
	createCallbacks();

	// this shows the window (it displays the window to the user)
	GLFW.glfwShowWindow(window);

	/*
	 * this limits the buffer swapping to however many times the monitor can
	 * handle (it's basically V-Sync). For most monitors, this will be 60
	 * times per second
	 * 
	 * essentially, this limits the game to however many FPS the monitor can
	 * actually display (approximately, it's not perfect)
	 */
	GLFW.glfwSwapInterval(1);

	// get the current system time (this is used for the FPS counter)
	time = System.currentTimeMillis();
    }

    /*
     * all the callbacks for the window will go in here. This function will
     * basically create all the callbacks for the window object when it is
     * called
     */
    private void createCallbacks() {
	// the callback for resizing the window
	sizeCallback = new GLFWWindowSizeCallback() {
	    public void invoke(long window, int w, int h) {
		width = w;
		height = h;
		/*
		 * tell the update function that the window has been resized and
		 * needs to be updated based on that
		 */
		isResized = true;
	    }
	};

	/*
	 * handling the inputs (check the source code for the Input class for
	 * more information):
	 * 
	 * this basically just sets up the callbacks for when keys are pressed
	 * (the methods that execute with the callbacks themselves can be found
	 * in the source code for the Input class)
	 */
	GLFW.glfwSetKeyCallback(window, input.getKeyboardCallback());
	GLFW.glfwSetCursorPosCallback(window, input.getMouseMoveCallback());
	GLFW.glfwSetMouseButtonCallback(window, input.getMouseButtonsCallback());
	GLFW.glfwSetScrollCallback(window, input.getMouseScrollCallback());

	// actually add the window resizing callback
	GLFW.glfwSetWindowSizeCallback(window, sizeCallback);
    }

    /*
     * this function updates the window. All the callbacks that are connected to
     * the window will be done in this method
     */
    public void update() {
	// this resizes the window to the correct size if it has been changed
	if (isResized) {
	    GL11.glViewport(0, 0, width, height);
	    isResized = false;
	}

	/*
	 * set the clear color (the background color)
	 * 
	 * GL11 is OpenGL Version 1.1
	 * 
	 * the alpha needs to be set to 1.0f (and not just 1.0) to signify that
	 * it's a float. Otherwise, it will just evaluate as a double which will
	 * cause OpenGL to throw an error
	 */
	GL11.glClearColor(background.getX(), background.getY(), background.getZ(), 1.0f);
	/*
	 * this tells OpenGL to actually clear (use the clear color)
	 * 
	 * the second argument tells OpenGL to clear in 3D (and not just 2D)
	 */
	GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

	GLFW.glfwPollEvents();

	// iterate the frames (used to display the FPS)
	frames++;

	// print the FPS once per second
	long tempTime;
	if ((tempTime = System.currentTimeMillis()) > (time + 1000)) {
	    time = tempTime;
	    // the old way of telling the user the FPS
	    System.out.println("FPS: " + frames);
	    /*
	     * the new way of telling the user the FPS: BROKEN - DO NOT USE -
	     * MESSES WITH UBUNTU FOR SOME REASON
	     */
	    // GLFW.glfwSetWindowTitle(tempTime, title + " | FPS: " + frames);
	    frames = 0;
	}
    }

    /*
     * this function swaps the buffers of the window (which colors it correctly)
     * 
     * I don't really fully understand this one
     */
    public void swapBuffers() {
	GLFW.glfwSwapBuffers(window);
    }

    /*
     * whether or not the window should close (like, if the user pressed the 'X'
     * button on the window to exit)
     */
    public boolean shouldClose() {
	return GLFW.glfwWindowShouldClose(window);
    }

    // this function destroys the window
    public void destroy() {
	/*
	 * clears the inputs (check the Input class source code for a better
	 * explanation)
	 */
	input.destroy();

	// frees the sizeCallback
	sizeCallback.free();

	/*
	 * this will close, destroy, and terminate GLFW (which frees up the
	 * system resources)
	 */
	GLFW.glfwWindowShouldClose(window);
	GLFW.glfwDestroyWindow(window);
	GLFW.glfwTerminate();
    }

    // this function sets the background color (the clear color)
    public void setBackgroundColor(float backgroundR, float backgroundG, float backgroundB) {
	background.set(backgroundR, backgroundG, backgroundB);
    }

    /*
     * the following methods are getters and setters
     */
    public boolean isFullscreen() {
	return isFullscreen;
    }

    public void setFullscreen(boolean isFullscreen) {
	this.isFullscreen = isFullscreen;
	/*
	 * if the screen is either being put in fullscreen or taken out of
	 * fullscreen, then the window must have been resized. So, we want to
	 * tell the window to update itself for that
	 */
	isResized = true;
	if (isFullscreen) {
	    GLFW.glfwGetWindowPos(window, windowPosX, windowPosY);
	    GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
	} else {
	    GLFW.glfwSetWindowMonitor(window, 0, windowPosX[0], windowPosY[0], width, height, 0);
	}
    }

    /*
     * this will set the mouse to locked or unlocked
     * 
     * if the mouse is locked, it will stay in the center of the screen. If it's
     * unlocked, it will be able to move around
     */
    public void setMouseState(boolean lock) {
	GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, lock ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public String getTitle() {
	return title;
    }

    public long getWindow() {
	return window;
    }

    public Matrix4f getProjectionMatrix() {
	return projection;
    }
}