/*
 * the Main class
 * 
 * basically, this is the file that is run to start the game engine. Everything
 * is called from here
 * 
 * Aidin Yazdi 3
 */

package main;

import org.lwjgl.glfw.GLFW;

import engine.graphics.Renderer;
import engine.graphics.Shader;
import engine.io.Input;
import engine.io.Window;
import engine.maths.Vector3f;
import engine.objects.Camera;
import engine.objects.ToRender;

/*
 * idk why I have to implement Runnable. I guess it's some sort of interface
 * somewhere with relevant code
 */
public class Main implements Runnable {
    /*
     * this defines an object as a thread. Basically, this object will be used
     * to run things on one thread of the CPU
     */
    public Thread game;

    /*
     * this defines a Window object. The Window class source code can be found
     * in the engine.io package
     * 
     * the window must be static to ensure that it never changes (because that
     * could create big issues with rendering and OpenGL and stuff)
     */
    public Window window;

    /*
     * this is the width and height of the window
     * 
     * these variables must be static. This is to ensure that the width and
     * height of the window never change (because that could create big issues
     * with rendering and OpenGL and stuff)
     */
    public final int WIDTH = 1280, HEIGHT = 720;

    /*
     * this basically creates all the meshes correctly. Check the source code
     * for more information
     */
    public ToRender toRender = new ToRender();

    // create the camera
    public Camera camera = new Camera(new Vector3f(0, 0, 1), new Vector3f(0, 0, 0));

    // the renderer (to actually render the mesh)
    public Renderer renderer;

    // the shader
    public Shader shader;

    // the method that starts the game
    public void start() {
	/*
	 * this gives the game object an actual thread to run. The thread is
	 * defined as the GameEngine object (which is why the "this" keyword is
	 * used)
	 */
	game = new Thread(this, "game");
	// this line will start the thread
	game.start();
    }

    /*
     * the function to initialize all the variables/objects for the game
     * 
     * the window and renderer will also be set up here
     */
    public void init() {
	System.out.println("initializing game...");

	/*
	 * initializes the actual window with the WIDTH and HEIGHT defined above
	 * and the title "GAME ENGINE"
	 */
	window = new Window(WIDTH, HEIGHT, "Game Engine");

	/*
	 * this method initializes the shader. The source code can be found in
	 * the Shader class
	 * 
	 * the path is relative to the resources folder (as is specified in the
	 * source code of the Shader class)
	 */
	shader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl");

	/*
	 * this method initializes the renderer. The source code can be found in
	 * the Renderer class
	 */
	renderer = new Renderer(window, shader);

	// sets the background color of the window
	window.setBackgroundColor(1.0f, 1.0f, 1.0f);

	/*
	 * this method creates the window. The source code can be found in the
	 * Window class
	 */
	window.create();

	/*
	 * this method creates the meshes. The source code can be found in the
	 * Mesh class
	 */
	for (int i = 0; i < toRender.getMeshes().length; i++) {
	    toRender.getMeshes()[i].create();
	}

	/*
	 * this method creates the shaders. The source code can be found in the
	 * Shader class
	 */
	shader.create();

	System.out.println("the game has been initialized");
    }

    public void run() {
	// initialize everything
	init();

	/*
	 * because this game engine registers whichever key is being pressed
	 * down each frame, if F11 is held down for more than one frame (which
	 * pretty much always happens unless the user is ridiculously fast) then
	 * the screen will rapidly toggle back and forth between fullscreen.
	 * This variable just makes sure that the screen will only toggle if the
	 * user has first stopped pressing the F11 key and then pressed it again
	 */
	boolean F11HasBeenReleased = true;

	/*
	 * the game loop
	 * 
	 * the second part of the condition in the while loop will end the game
	 * if the ESC key is pressed
	 */
	while (!window.shouldClose() && !Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
	    // update all the game variables
	    update();
	    // render the game
	    render();

	    // if the user inputs the F11 button, it will toggle the fullscreen
	    if (Input.isKeyDown(GLFW.GLFW_KEY_F11) && F11HasBeenReleased) {
		window.setFullscreen(!window.isFullscreen());
		F11HasBeenReleased = false;
	    } else if (!Input.isKeyDown(GLFW.GLFW_KEY_F11)) {
		F11HasBeenReleased = true;
	    }

	    /*
	     * if the user clicks on the window, lock the mouse to the center of
	     * the screen
	     */
	    if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
		window.setMouseState(true);
	    }
	}

	/*
	 * actually close/delete everything to shut it all off and free up
	 * system resources
	 */
	close();

	// tells the user that the game engine has been stopped
	System.out.println("game engine stopped");
    }

    // update all the game variables
    private void update() {
	// update the window
	window.update();

	// update the camera
	camera.update();

	// update all the meshes
	for (int i = 0; i < toRender.getMeshes().length; i++) {
	    toRender.getMeshes()[i].destroy();
	}
	toRender.update();
	for (int i = 0; i < toRender.getMeshes().length; i++) {
	    toRender.getMeshes()[i].create();
	}
    }

    // render the game
    private void render() {
	// render the mesh
	for (int i = 0; i < toRender.getObjects().length; i++) {
	    renderer.renderMesh(toRender.getObjects()[i], camera);
	}

	// swap the buffers of the window
	window.swapBuffers();
    }

    // close the game (and free up system resources)
    private void close() {
	// actually closes the window
	window.destroy();

	/*
	 * delete all the buffers and the vertex array object to free up system
	 * resources
	 */
	for (int i = 0; i < toRender.getMeshes().length; i++) {
	    toRender.getMeshes()[i].destroy();
	}

	/*
	 * delete the program (the shaders when put together) to free up system
	 * resources
	 */
	shader.destroy();
    }

    public static void main(String args[]) {
	/*
	 * create an instance of the Main class and run the start function on it
	 * 
	 * since a constructor for the Main class has not been defined, Java
	 * will use the implicit constructor
	 */
	new Main().start();
    }
}