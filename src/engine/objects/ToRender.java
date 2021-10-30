/*
 * the ToRender class
 * 
 * this class holds all the data for the different things that are going to be
 * rendered
 * 
 * Aidin Yazdi
 */

package engine.objects;

import org.lwjgl.glfw.GLFW;

import engine.graphics.Mesh;
import engine.io.Input;
import engine.maths.Vector3f;

public class ToRender {
    // initialize the meshes[] array and the objects[] array
    Mesh[] meshes;
    private GameObject[] objects;

    // variables to keep track of the centers of circles and colors of circles
    private Vector3f[] circleCenters =
	{
	    new Vector3f(3.0f, 0.0f, 0.0f)
	};
    private Vector3f[] circleColors =
	{
	    new Vector3f(0.0f, 0.0f, 1.0f)
	};

    // the constructor
    public ToRender() {
	/*
	 * ====== stuff to render goes here ======
	 * 
	 * the way rendering a shape works is that a mesh is defined with the
	 * first argument being a vertex array and the second argument being an
	 * indices array
	 * 
	 * the vertices in the vertex array are of the type Vector3f and each
	 * one represents a coordinate for a point on the shape that is being
	 * drawn
	 * 
	 * the int[] is a list of indices - basically, it's the order that the
	 * vertices should be drawn. Vertices can be drawn multiple times. Also,
	 * because of how the Renderer class is set up, the way OpenGL will draw
	 * is by grouping every 3 vertices (which correspond to every 3 indices)
	 * to draw a triangle
	 * 
	 * in the int[], 0 refers to the first vertex, 1 refers to the second
	 * vertex, and so on
	 * 
	 * Material is the texture that we want to load on these vertices
	 * 
	 * coordinates for the 2D textures (the Vector2f argument) must be
	 * defined counter-clockwise (top left, bottom left, bottom right, and
	 * then top right). Furthermore, while the coordinates of the Vector3f
	 * are defined with (0,0) being the center of the window, the texture
	 * coordinates (the Vector2f coordinates) are defined with the top left
	 * being (0,0). Also, the coordinates of the texture refer to
	 * coordinates on the mesh (so, (0,0) refers to the top left of the mesh
	 * - not the top left of the window - and (1,1) refers to the bottom
	 * right of the mesh)
	 * 
	 * temp mesh arrays hold all the meshes that will be passed into the
	 * meshes[] array
	 * 
	 * the meshes[] array holds all the meshes that will be rendered
	 * 
	 * in order to make generating meshes more simple, meshes are often
	 * generated using a method from a shape class
	 * 
	 * below is the first temp mesh array
	 */
	Mesh[] tempMeshArray1 = new Mesh[]
	    {
		Square.generateSquare(new Vector3f(-0.5f, 0.5f, -1.0f), new Vector3f(0.0f, 0.0f, 0.0f),
			Square.getStandardSquareLength(), new Vector3f(1.0f, 0.0f, 0.0f),
			new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 1.0f), new Vector3f(1.0f, 0.0f, 0.0f),
			"/textures/DrawnFace.png"),
		Square.generateSquare(new Vector3f(-0.5f, 0.5f, 1.0f), new Vector3f(0.0f, 0.0f, 45.0f), 1.0f,
			new Vector3f(1.0f, 0.0f, -1.0f), new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 1.0f),
			new Vector3f(1.0f, 0.0f, 0.0f), null),
		Triangle.generateTriangle(new Vector3f(-0.5f, 0.5f, 3.0f), 0, 0, 0, 2, new Vector3f(1.0f, 0.0f, 1.0f),
			new Vector3f(0.0f, 1.0f, 1.0f), new Vector3f(0.0f, 0.0f, 1.0f), null),
		Rectangle.generateRectangle(new Vector3f(3.0f, 0.0f, -1.0f), Rectangle.getStandardRectangleLength(),
			Rectangle.getStandardRectangleWidth(), new Vector3f(0.0f, 0.0f, 1.0f),
			new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0.0f, 0.0f, 1.0f), new Vector3f(0.0f, 1.0f, 0.0f),
			null)
	    };

	/*
	 * make a circle:
	 * 
	 * the number of squares that will be used to make a circle
	 */
	int circleIndices = 20;
	/*
	 * another temp mesh array to store all the squares that will be used to
	 * make up the circle
	 */
	Mesh[] tempMeshArray2 = Circle.generateCircle(circleCenters[0], Circle.getStandardCircleRadius(), circleIndices,
		circleColors[0]);

	// make more shapes
	Mesh[] tempMeshArray3 =
	    {
		Square.generateSquare(new Vector3f(-5.0f, -2.2f, 5f), new Vector3f(90.0f, 0.0f, 0.0f), 15.0f,
			new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f),
			new Vector3f(0.0f, 1.0f, 0.0f), "/textures/Fractal.png"),
		Square.generateSquare(new Vector3f(-5.0f, 11f, -2.5f), new Vector3f(30.0f, 0.0f, 0.0f), 15.0f,
			new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f),
			new Vector3f(0.0f, 1.0f, 0.0f), "/textures/Walk.png")
	    };

	/*
	 * initialize the actual meshes[] array and put all the meshes from the
	 * temp mesh arrays into it
	 */
	this.meshes = new Mesh[tempMeshArray1.length + tempMeshArray2.length + tempMeshArray3.length];
	for (int i = 0; i < tempMeshArray1.length; i++) {
	    meshes[i] = tempMeshArray1[i];
	}
	for (int i = 0; i < tempMeshArray2.length; i++) {
	    meshes[i + tempMeshArray1.length] = tempMeshArray2[i];
	}
	for (int i = 0; i < tempMeshArray3.length; i++) {
	    meshes[i + tempMeshArray1.length + tempMeshArray2.length] = tempMeshArray3[i];
	}

	/*
	 * this is the game object
	 * 
	 * it basically includes the mesh and a whole bunch of other stuff about
	 * movement of the mesh
	 * 
	 * check the source code for more details
	 */
	this.objects = new GameObject[meshes.length];

	for (int i = 0; i < objects.length; i++) {
	    objects[i] = new GameObject(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), meshes[i]);
	}
    }

    // the following methods are getters
    public Mesh[] getMeshes() {
	return meshes;
    }

    public GameObject[] getObjects() {
	return objects;
    }

    // update the positions of the textures in the game
    public void update() {
	/*
	 * this is where the game will be updated. All instructions for how the
	 * game should be updated should be put here
	 */
	if (Input.isKeyDown(GLFW.GLFW_KEY_E)) {
	    updateSquare(0, new Vector3f(0.0f, 0.1f, 0.0f));
	    updateRectangle(3, new Vector3f(0.0f, 0.1f, 0.0f));
	    updateCircle(4, 23, new Vector3f(0.0f, 0.1f, 0.0f), 0);
	}
    }

    // update the position of a square mesh
    public void updateSquare(int index, Vector3f movement) {
	meshes[index] = Square.moveSquare(meshes[index], movement, meshes[index].getMaterial().getPath());
	objects[index] = new GameObject(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1),
		meshes[index]);
    }

    // update the position of a rectangle mesh
    public void updateRectangle(int index, Vector3f movement) {
	meshes[index] = Rectangle.moveRectangle(meshes[index], movement, null);
	objects[index] = new GameObject(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1),
		meshes[index]);
    }

    // update the position of circle meshes
    public void updateCircle(int firstIndex, int lastIndex, Vector3f movement, int circleIndex) {
	circleCenters[circleIndex] = Vector3f.add(circleCenters[circleIndex], movement);
	Mesh[] temp = Circle.generateCircle(circleCenters[circleIndex], Circle.getStandardCircleRadius(),
		(lastIndex - firstIndex) + 1, circleColors[circleIndex]);

	/*
	 * cycle through all the indices on the main meshes[] array that have to
	 * do with the circle and update them
	 */
	int tempCounter = 0;
	for (int i = firstIndex; i <= lastIndex; i++) {
	    meshes[i] = temp[tempCounter];
	    objects[i] = new GameObject(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), meshes[i]);
	    tempCounter++;
	}
    }
}
