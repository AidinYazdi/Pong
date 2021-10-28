/*
 * the FileUtils class
 * 
 * this is a class with a bunch of helper functions
 * 
 * Aidin Yazdi
 * 
 * im at 26:36
 */

package engine.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {

    /*
     * this method will load a file as a string
     * 
     * it can be used to load shader files
     */
    public static String loadAsString(String path) {
	/*
	 * StringBuilder is a class already built into Java that will allow us
	 * to build the file as a string
	 */
	StringBuilder result = new StringBuilder();

	/*
	 * we need a try-catch because it's possible that the path will not lead
	 * to a file
	 * 
	 * this first big line in the try() will basically go to the resources
	 * folder of our project and get the desired file
	 */
	try (BufferedReader reader = new BufferedReader(
		new InputStreamReader(FileUtils.class.getResourceAsStream(path)))) {
	    // this will read into the result variable the file line by line
	    String line = "";
	    while ((line = reader.readLine()) != null) {
		// we want the line break at the end of each line
		result.append(line).append("\n");
	    }
	} catch (IOException e) {
	    System.err.println("Couldn't find the file at " + path);
	}

	return result.toString();
    }
}
