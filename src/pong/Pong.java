/*
 * the Pong class
 * 
 * this class holds data about the general status of the game at any given point
 * 
 * Aidin Yazdi
 */

package pong;

public class Pong {
    /*
     * blueWins - if blue wins the game
     * 
     * redWins - if red wins the game
     * 
     * bluePoint - if blue has scored a point
     * 
     * redPoint - if red has scored a point
     * 
     * paused - if the game is currently paused
     */
    public boolean blueWins, redWins, bluePoint, redPoint, paused;

    /*
     * blueLives - the amount of lives blue has left
     * 
     * redLives - the amount of lives red has left
     */
    public int blueLives, redLives;

    // the constructor
    public Pong() {
	// initialize all the data about the game
	blueWins = false;
	redWins = false;
	bluePoint = false;
	redPoint = false;
	paused = true;
	blueLives = 3;
	redLives = 3;
    }
}
