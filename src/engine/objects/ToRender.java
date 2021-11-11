/*
 * the ToRender class
 * 
 * this class holds all the data for the different things that are going to be
 * rendered
 * 
 * Aidin Yazdi
 */

package engine.objects;

import java.util.Random;

import org.lwjgl.glfw.GLFW;

import engine.graphics.Mesh;
import engine.io.Input;
import engine.maths.Vector3f;
import pong.Pong;

public class ToRender {
    // initialize the meshes[] array and the objects[] array
    Mesh[] meshes;
    private GameObject[] objects;

    // variables to make coloring easier
    private Vector3f red = new Vector3f(1.0f, 0.0f, 0.0f);
    private Vector3f green = new Vector3f(0.0f, 1.0f, 0.0f);
    private Vector3f blue = new Vector3f(0.0f, 0.0f, 1.0f);
    private Vector3f white = new Vector3f(1.0f, 1.0f, 1.0f);
    private Vector3f black = new Vector3f(0.0f, 0.0f, .0f);

    // variables to keep track of the centers of circles and colors of circles
    private Vector3f[] circleCenters =
	{
	    new Vector3f(0.0f, 0.0f, 0.0f)
	};
    private Vector3f[] circleColors =
	{
	    white
	};

    // variables to keep track of starting positions
    private float paddleStartingPositionX = -0.000015f;
    private float heartStartingX = -0.00001035f;
    private float heartStartingY = 0.0000087f;
    private float heartStartingWidth = 0.00000165f;
    private float textStartingWidth = 0.0000035f;

    // variables to keep track of speeds and previous movements
    private Vector3f paddleSpeed = new Vector3f(0.0f, 0.00000025f, 0.0f);
    private Vector3f[] lastFramePaddleMovements =
	{
	    new Vector3f(0.0f, 0.0f, 0.0f),
	    new Vector3f(0.0f, 0.0f, 0.0f)
	};
    private Vector3f lastFrameBallMovement = new Vector3f(0.0f, 0.0f, 0.0f);
    private float ballSpeedModifier = 0.12f;
    private float ballSpeedIncrease = 0.000000005f;

    // an object to keep track of general data about the game status
    private Pong gameStatus = new Pong();

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
		Rectangle.generateRectangle(new Vector3f(-0.00002f, 0.000007f, 0.0f), 0.0000003f, 0.001f, white, white,
			white, white, null),
		Rectangle.generateRectangle(new Vector3f(-0.00002f, -0.00000849f, 0.0f), 0.0000003f, 0.001f, white,
			white, white, white, null)
	    };

	/*
	 * make a circle:
	 * 
	 * the number of squares that will be used to make a circle
	 */
	int circleIndices = 30;
	/*
	 * another temp mesh array to store all the squares that will be used to
	 * make up the circle
	 */
	Mesh[] tempMeshArray2 = Circle.generateCircle(circleCenters[0], Circle.getStandardCircleRadius(), circleIndices,
		circleColors[0]);

	// make more shapes
	Mesh[] tempMeshArray3 =
	    {
		Rectangle.generateRectangle(
			new Vector3f(paddleStartingPositionX, Rectangle.getStandardRectangleLength() / 2.0f, 0.0f),
			Rectangle.getStandardRectangleLength(), Rectangle.getStandardRectangleWidth(), blue, blue, blue,
			blue, null),
		Rectangle.generateRectangle(
			new Vector3f((-1.0f * paddleStartingPositionX) - Rectangle.getStandardRectangleWidth(),
				Rectangle.getStandardRectangleLength() / 2.0f, 0.0f),
			Rectangle.getStandardRectangleLength(), Rectangle.getStandardRectangleWidth(), red, red, red,
			red, null),
		Square.generateSquare(new Vector3f(heartStartingX - heartStartingWidth, heartStartingY, 0.0f),
			new Vector3f(0.0f, 0.0f, 0.0f), heartStartingWidth, new Vector3f(0.0f, 0.0f, 0.0f),
			new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f),
			"/textures/Heart.png"),
		Square.generateSquare(new Vector3f(heartStartingX, heartStartingY, 0.0f),
			new Vector3f(0.0f, 0.0f, 0.0f), heartStartingWidth, new Vector3f(0.0f, 0.0f, 0.0f),
			new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f),
			"/textures/Heart.png"),
		Square.generateSquare(new Vector3f(heartStartingX + heartStartingWidth, heartStartingY, 0.0f),
			new Vector3f(0.0f, 0.0f, 0.0f), heartStartingWidth, new Vector3f(0.0f, 0.0f, 0.0f),
			new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f),
			"/textures/Heart.png"),
		Square.generateSquare(new Vector3f((-1.0f * heartStartingX), heartStartingY, 0.0f),
			new Vector3f(0.0f, 0.0f, 0.0f), heartStartingWidth, new Vector3f(0.0f, 0.0f, 0.0f),
			new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f),
			"/textures/Heart.png"),
		Square.generateSquare(new Vector3f((-1.0f * heartStartingX) - heartStartingWidth, heartStartingY, 0.0f),
			new Vector3f(0.0f, 0.0f, 0.0f), heartStartingWidth, new Vector3f(0.0f, 0.0f, 0.0f),
			new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f),
			"/textures/Heart.png"),
		Square.generateSquare(
			new Vector3f((-1.0f * heartStartingX) - (heartStartingWidth + heartStartingWidth),
				heartStartingY, 0.0f),
			new Vector3f(0.0f, 0.0f, 0.0f), heartStartingWidth, new Vector3f(0.0f, 0.0f, 0.0f),
			new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f),
			"/textures/Heart.png")
//		Square.generateSquare(new Vector3f(-textStartingWidth / 2.0f, textStartingWidth + 0.000001f, 0.0f),
//			new Vector3f(0.0f, 0.0f, 0.0f), textStartingWidth, new Vector3f(0.0f, 0.0f, 0.0f),
//			new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f),
//			"/textures/PressSpace.png")
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
	Vector3f[] movements = pongUpdate(meshes[32].getVertices()[0].getPosition().getY(),
		meshes[33].getVertices()[0].getPosition().getY(), circleCenters[0]);
	updateRectangle(32, movements[0]);
	updateRectangle(33, movements[1]);
	updateCircle(2, 31, movements[2], 0);
	// the hearts
	if (movements[4].getY() != 0.0f) {
	    updateSquare(36, movements[4]);
	    resetCircle(2, 31, new Vector3f(0.0f, 0.0f, 0.0f), 0);
	    lastFrameBallMovement = new Vector3f(0.0f, 0.0f, 0.0f);
	} else if (movements[5].getY() != 0.0f) {
	    updateSquare(35, movements[5]);
	    resetCircle(2, 31, new Vector3f(0.0f, 0.0f, 0.0f), 0);
	    lastFrameBallMovement = new Vector3f(0.0f, 0.0f, 0.0f);
	} else if (movements[6].getY() != 0.0f) {
	    updateSquare(34, movements[6]);
	    resetCircle(2, 31, new Vector3f(0.0f, 0.0f, 0.0f), 0);
	    lastFrameBallMovement = new Vector3f(0.0f, 0.0f, 0.0f);
	}
	if (movements[7].getY() != 0.0f) {
	    updateSquare(39, movements[7]);
	    resetCircle(2, 31, new Vector3f(0.0f, 0.0f, 0.0f), 0);
	    lastFrameBallMovement = new Vector3f(0.0f, 0.0f, 0.0f);
	} else if (movements[8].getY() != 0.0f) {
	    updateSquare(38, movements[8]);
	    resetCircle(2, 31, new Vector3f(0.0f, 0.0f, 0.0f), 0);
	    lastFrameBallMovement = new Vector3f(0.0f, 0.0f, 0.0f);
	} else if (movements[9].getY() != 0.0f) {
	    updateSquare(37, movements[9]);
	    resetCircle(2, 31, new Vector3f(0.0f, 0.0f, 0.0f), 0);
	    lastFrameBallMovement = new Vector3f(0.0f, 0.0f, 0.0f);
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

    // reset the position of circle meshes
    public void resetCircle(int firstIndex, int lastIndex, Vector3f newPosition, int circleIndex) {
	circleCenters[circleIndex] = newPosition;
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

    // the pong logic
    public Vector3f[] pongUpdate(float paddle1Y, float paddle2Y, Vector3f circleCenter) {
	/*
	 * the 0th index in the left paddle, the 1st index is the right paddle,
	 * and the 2nd index is the circle
	 * 
	 * the 3rd index is the "Press Space" texture
	 * 
	 * the 4th-9th indices are the hearts (first the blue side hearts and
	 * then the red side hearts)
	 */
	Vector3f[] movements =
	    {
		new Vector3f(0.0f, 0.0f, 0.0f),
		new Vector3f(0.0f, 0.0f, 0.0f),
		new Vector3f(0.0f, 0.0f, 0.0f),
		new Vector3f(0.0f, 0.0f, 0.0f),
		new Vector3f(0.0f, 0.0f, 0.0f),
		new Vector3f(0.0f, 0.0f, 0.0f),
		new Vector3f(0.0f, 0.0f, 0.0f),
		new Vector3f(0.0f, 0.0f, 0.0f),
		new Vector3f(0.0f, 0.0f, 0.0f),
		new Vector3f(0.0f, 0.0f, 0.0f)
	    };

	// only update the game if the game isn't paused
	if (!gameStatus.paused) {
	    /*
	     * create a temporary circle center to deal with for the ball's
	     * theoretical position
	     */
	    Vector3f tempCircleCenter = new Vector3f(circleCenter.getX(), circleCenter.getY(), circleCenter.getZ());

	    // move the ball
	    movements[2] = lastFrameBallMovement;
	    /*
	     * if the ball wasn't moving at all, initialize x and y movement for
	     * it. If it just wasn't moving horizontally, only initialize x
	     * movement
	     */
	    Random rand = new Random();
	    float scaler = 0.000001f;
	    if (movements[2].getX() == 0) {
		if (rand.nextBoolean()) {
		    movements[2].setX(0.125f * scaler);
		} else {
		    movements[2].setX(-0.125f * scaler);
		}
	    }
	    // update the center of the ball to where it will be next frame
	    tempCircleCenter = Vector3f.add(circleCenter, movements[2]);
	    // collision check against the upper and lower bounds
	    if ((tempCircleCenter.getY() + Circle.getStandardCircleRadius()) > (0.000007f - 0.0000003f)) {
		movements[2].setY(movements[2].getY() * (float) -1.0);
	    } else if ((tempCircleCenter.getY() - Circle.getStandardCircleRadius()) < -0.00000849f) {
		movements[2].setY(movements[2].getY() * (float) -1.0);
	    }
	    /*
	     * collision check against the paddles
	     * 
	     * only check against the left paddle if the ball is moving left and
	     * the right paddle if the ball is moving right
	     */
	    float leftPaddleBottom = paddle1Y - Rectangle.getStandardRectangleLength(),
		    rightPaddleBottom = paddle2Y - Rectangle.getStandardRectangleLength();
	    if (movements[2].getX() < 0) {
		movements[2] = ballPaddleCollisionCheckLeft(tempCircleCenter, movements[2], paddleStartingPositionX,
			paddle1Y, Circle.getStandardCircleRadius(), Rectangle.getStandardRectangleLength(),
			Rectangle.getStandardRectangleWidth());
	    } else {
		movements[2] = ballPaddleCollisionCheckRight(tempCircleCenter, movements[2],
			(-1.0f * paddleStartingPositionX) - Rectangle.getStandardRectangleWidth(), paddle2Y,
			Circle.getStandardCircleRadius(), Rectangle.getStandardRectangleLength(),
			Rectangle.getStandardRectangleWidth());
	    }
	    // collision check if the ball is out of bounds
	    if (tempCircleCenter.getX() < -0.0000157f) {
		gameStatus.blueLives -= 1;
		gameStatus.redPoint = true;
		gameStatus.paused = true;
		movements[3] = new Vector3f(0.0f, 1.0f, 0.0f);
		movements[6 - gameStatus.blueLives] = new Vector3f(0.0f, 1.0f, 0.0f);
		if (gameStatus.blueLives == 0) {
		    gameStatus.redWins = true;
		}
	    } else if (tempCircleCenter.getX() > 0.0000157f) {
		gameStatus.redLives -= 1;
		gameStatus.bluePoint = true;
		gameStatus.paused = true;
		movements[3] = movements[3] = new Vector3f(0.0f, 1.0f, 0.0f);
		movements[9 - gameStatus.redLives] = new Vector3f(0.0f, 1.0f, 0.0f);
		if (gameStatus.redLives == 0) {
		    gameStatus.blueWins = true;
		}
	    }
	    // set last frame's movements to this frame
	    lastFrameBallMovement = movements[2];

	    // move the paddles
	    if (Input.isKeyDown(GLFW.GLFW_KEY_W) && !(paddle1Y >= (0.000007f - 0.0000003f))) {
		movements[0] = Vector3f.add(movements[0], paddleSpeed);
	    }
	    if (Input.isKeyDown(GLFW.GLFW_KEY_S)
		    && !(paddle1Y <= (-0.00000849f + Rectangle.getStandardRectangleLength()))) {
		movements[0] = Vector3f.subtract(movements[0], paddleSpeed);
	    }
	    if (Input.isKeyDown(GLFW.GLFW_KEY_UP) && !(paddle2Y >= (0.000007f - 0.0000003f))) {
		movements[1] = Vector3f.add(movements[1], paddleSpeed);
	    }
	    if (Input.isKeyDown(GLFW.GLFW_KEY_DOWN)
		    && !(paddle2Y <= (-0.00000849f + Rectangle.getStandardRectangleLength()))) {
		movements[1] = Vector3f.subtract(movements[1], paddleSpeed);
	    }

	    /*
	     * set paddleY to whatever the paddleY would be next frame (after
	     * the movements has been factored in).
	     * 
	     * The reason we have to do this here is because we only want to
	     * move the paddle based on collisions if it would have collided
	     * next frame. If the paddle wouldn't have collided anyways
	     * (because, with the movements, it wouldn't have violated the
	     * borders) then there is no need to adjust the paddle based on the
	     * borders
	     */
	    paddle1Y += movements[0].getY();
	    paddle2Y += movements[1].getY();

	    // move the paddles within the bounds
	    if (paddle1Y >= 0.000007f) {
		movements[0] = Vector3f.add(movements[0], new Vector3f(0.0f, 0.000007f - paddle1Y, 0.0f));
	    }
	    if ((paddle1Y - Rectangle.getStandardRectangleLength()) <= -0.00000849f) {
		movements[0] = Vector3f.add(movements[0], new Vector3f(0.0f, -0.00000849f - leftPaddleBottom, 0.0f));
	    }
	    if (paddle2Y >= 0.000007f) {
		movements[1] = Vector3f.add(movements[1], new Vector3f(0.0f, 0.000007f - paddle2Y, 0.0f));
	    }
	    if ((paddle2Y - Rectangle.getStandardRectangleLength()) <= -0.00000849f) {
		movements[1] = Vector3f.add(movements[1], new Vector3f(0.0f, -0.00000849f - rightPaddleBottom, 0.0f));
	    }
	} else {
	    if (gameStatus.blueWins) {
		System.out.println("blue wins");
	    } else if (gameStatus.redWins) {
		System.out.println("red wins");
	    } else if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
		gameStatus.paused = false;
//		movements[3] = movements[3] = new Vector3f(0.0f, -1.0f, 0.0f);
	    }
	}

	// return the movements that we want to happened to the paddles and ball
	return movements;
    }

    /*
     * check if the ball is touching the right paddle
     * 
     * if the ball is touching a paddle, return what the new movement of the
     * ball should be
     */
    private Vector3f ballPaddleCollisionCheckLeft(Vector3f ballCenter, Vector3f movement, float paddleX, float paddleY,
	    float ballRadius, float paddleLength, float paddleWidth) {
	Vector3f nextMovement = movement;
	float paddleVerticalMidpoint = paddleY - (paddleLength / 2.0f);
	if (((ballCenter.getX() - ballRadius) < (paddleX + paddleWidth))
		&& (ballCenter.getX() > (paddleX + paddleWidth))
		&& ((ballCenter.getY() + ballRadius) >= (paddleY - paddleLength))
		&& ((ballCenter.getY() - ballRadius) <= paddleY)) {
	    /*
	     * reverse the horizontal movement of the ball and add a little bit
	     * of speed to the ball each time it hits the paddle
	     */
	    nextMovement.setX(nextMovement.getX() * -1.0f);
	    if (Math.abs(nextMovement.getX()) < 0.0000004f) {
		nextMovement.setX(nextMovement.getX() + ballSpeedIncrease);
	    }

	    /*
	     * if the ball is above or below the middle of the paddle, change
	     * its vertical movement correspondingly
	     */
	    float distanceToCenter = ballCenter.getY() - paddleVerticalMidpoint;
	    float speedDifferential = ballSpeedModifier * Math.abs(distanceToCenter);
	    if (distanceToCenter < 0) {
		nextMovement.setY(nextMovement.getY() - speedDifferential);
	    } else if (distanceToCenter > 0) {
		nextMovement.setY(nextMovement.getY() + speedDifferential);
	    }
	} else if (((ballCenter.getX() - ballRadius) < (paddleX + paddleWidth))
		&& (ballCenter.getX() <= (paddleX + paddleWidth)) && (ballCenter.getX() >= paddleX)
		&& ((ballCenter.getY() + ballRadius) >= (paddleY - paddleLength))
		&& ((ballCenter.getY() - ballRadius) <= paddleY)) {
	    /*
	     * if the ball is above or below the middle of the paddle, change
	     * its vertical movement correspondingly
	     */
	    float distanceToCenter = ballCenter.getY() - paddleVerticalMidpoint;
	    float speedDifferential = paddleSpeed.getY() + (ballSpeedModifier * Math.abs(distanceToCenter));
	    if (distanceToCenter < 0) {
		nextMovement.setY(nextMovement.getY() - speedDifferential);
	    } else if (distanceToCenter > 0) {
		nextMovement.setY(nextMovement.getY() + speedDifferential);
	    }
	}
	return nextMovement;
    }

    /*
     * check if the ball is touching the right paddle
     * 
     * if the ball is touching a paddle, return what the new movement of the
     * ball should be
     */
    private Vector3f ballPaddleCollisionCheckRight(Vector3f ballCenter, Vector3f movement, float paddleX, float paddleY,
	    float ballRadius, float paddleLength, float paddleWidth) {
	Vector3f nextMovement = movement;
	float paddleVerticalMidpoint = paddleY - (paddleLength / 2.0f);
	if (((ballCenter.getX() + ballRadius) > paddleX) && (ballCenter.getX() < paddleX)
		&& ((ballCenter.getY() + ballRadius) >= (paddleY - paddleLength))
		&& ((ballCenter.getY() - ballRadius) <= paddleY)) {
	    /*
	     * reverse the horizontal movement of the ball and add a little bit
	     * of speed to the ball each time it hits the paddle
	     */
	    nextMovement.setX(nextMovement.getX() * -1.0f);
	    if (Math.abs(nextMovement.getX()) < 0.0000004f) {
		nextMovement.setX(nextMovement.getX() - ballSpeedIncrease);
	    }

	    /*
	     * if the ball is above or below the middle of the paddle, change
	     * its vertical movement correspondingly
	     */
	    float distanceToCenter = ballCenter.getY() - paddleVerticalMidpoint;
	    float speedDifferential = ballSpeedModifier * Math.abs(distanceToCenter);
	    if (distanceToCenter < 0) {
		nextMovement.setY(nextMovement.getY() - speedDifferential);
	    } else if (distanceToCenter > 0) {
		nextMovement.setY(nextMovement.getY() + speedDifferential);
	    }
	} else if (((ballCenter.getX() - ballRadius) < (paddleX + paddleWidth))
		&& (ballCenter.getX() <= (paddleX + paddleWidth)) && (ballCenter.getX() >= paddleX)
		&& ((ballCenter.getY() + ballRadius) >= (paddleY - paddleLength))
		&& ((ballCenter.getY() - ballRadius) <= paddleY)) {
	    /*
	     * if the ball is above or below the middle of the paddle, change
	     * its vertical movement correspondingly
	     */
	    float distanceToCenter = ballCenter.getY() - paddleVerticalMidpoint;
	    float speedDifferential = paddleSpeed.getY() + (ballSpeedModifier * Math.abs(distanceToCenter));
	    if (distanceToCenter < 0) {
		nextMovement.setY(nextMovement.getY() - speedDifferential);
	    } else if (distanceToCenter > 0) {
		nextMovement.setY(nextMovement.getY() + speedDifferential);
	    }
	}
	return nextMovement;
    }
}
