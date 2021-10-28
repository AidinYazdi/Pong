/*
 * the MiscMath class
 * 
 * this class has a bunch of methods used for mathematical operations
 * 
 * Aidin Yazdi
 */

package engine.maths;

public class MiscMath {
    /*
     * multiply two 2D matrices together
     * 
     * NOTE: the method will only work with matrices that are arrays formatted
     * where the outer array refers to the columns and the inner array refers to
     * the rows. Meaning, each element of the outer array should be an entire column
     * 
     * this code was edited from code found here:
     * https://www.baeldung.com/java-matrix-multiplication
     */
    public static float[][] multiplyMatrices(float[][] firstMatrix, float[][] secondMatrix) {
	float[][] result = new float[secondMatrix.length][firstMatrix[0].length];

//	for (int row = 0; row < result.length; row++) {
//	    for (int col = 0; col < result[row].length; col++) {
//		result[row][col] = multiplyMatricesCell(firstMatrix, secondMatrix, row, col);
//	    }
//	}

	for (int col = 0; col < result.length; col++) {
	    for (int row = 0; row < result[col].length; row++) {
		result[col][row] = multiplyMatricesCell(firstMatrix, secondMatrix, col, row);
	    }
	}

	return result;
    }

    /*
     * helper function for the multiplyMatrices function
     * 
     * this code was edited from code found here:
     * https://www.baeldung.com/java-matrix-multiplication
     */
    public static float multiplyMatricesCell(float[][] firstMatrix, float[][] secondMatrix, int col, int row) {
	float cell = 0;
	for (int i = 0; i < secondMatrix[col].length; i++) {
	    cell += firstMatrix[i][row] * secondMatrix[col][i];
	}
	return cell;
    }
}
