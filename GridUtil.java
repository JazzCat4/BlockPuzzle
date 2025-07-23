package hw3;

import static api.Orientation.*;
import static api.CellType.*;

import java.util.ArrayList;

import api.Cell;
import api.CellType;
import api.Orientation;


/**
 * Utilities for parsing string descriptions of a grid.
 * @Author Ethen Santana
 */
public class GridUtil {
	/**
	 * Constructs a 2D grid of Cell objects given a 2D array of cell descriptions.
	 * String descriptions are a single character and have the following meaning.
	 * <ul>
	 * <li>"*" represents a wall.</li>
	 * <li>"e" represents an exit.</li>
	 * <li>"." represents a floor.</li>
	 * <li>"[", "]", "^", "v", or "#" represent a part of a boulder. A boulder is not a
	 * type of cell, it is something placed on a cell floor. For these descriptions
	 * a cell is created with CellType of FLOOR. This method does not create any
	 * boulders or set boulders on cells.</li>
	 * </ul>
	 * The method only creates cells and not boulders. See the other utility method
	 * findBoulders which is used to create the boulders.
	 * 
	 * @param desc a 2D array of strings describing the grid
	 * @return a 2D array of cells the represent the grid without any boulders present
	 */
	public static Cell[][] createGrid(String[][] desc) {
		int rowLength = desc.length;
		int colLength = desc[0].length;
		Cell[][] grid = new Cell[rowLength][colLength];
		
		for (int i=0; i < rowLength; i++) {
			for (int j = 0; j < colLength; j++) {
				String currSpace = desc[i][j];
				CellType type;
				// for wall spaces
				if (currSpace.equals("*")) {
				type = CellType.WALL;	
				}
				// For Exit spaces
				else if (currSpace.equals("e")) {
				type = CellType.EXIT;
				}
				
				// For Floor spaces, this includes ., [, ], ^, v, #
				else {
				type = CellType.GROUND;
				}
				
				// Create Cell variable
				Cell currCell = new Cell(i, j, type);
				
				//Add cell to array of cells (grid)
				grid[i][j] = currCell;
			}
		}
		return grid;
	}

	/**
	 * Returns a list of boulders that are constructed from a given 2D array of cell
	 * descriptions. String descriptions are a single character and have the
	 * following meanings.
	 * <ul>
	 * <li>"[" the start (left most column) of a horizontal boulder</li>
	 * <li>"]" the end (right most column) of a horizontal boulder</li>
	 * <li>"^" the start (top most row) of a vertical boulder</li>
	 * <li>"v" the end (bottom most column) of a vertical boulder</li>
	 * <li>"#" inner segments of a boulder, these are always placed between the start
	 * and end of the boulder</li>
	 * <li>"*", ".", and "e" symbols that describe cell types, meaning there is not
	 * boulder currently over the cell</li>
	 * </ul>
	 * 
	 * @param desc a 2D array of strings describing the grid
	 * @return a list of boulders found in the given grid description
	 */
	public static ArrayList<Boulder> findBoulders(String[][] desc) {
		// Create arraylist of boulders
		ArrayList<Boulder> blist = new ArrayList<Boulder>();
		
		// iterate through desc
		for (int i = 0; i < desc.length; i++) {
			for (int j = 0; j < desc[0].length; j++) {
				String currCell = desc[i][j];
				
				// If start of horizontal boulder, put into makeHorizontalBoulder
				if (currCell.equals("[")) {
					Boulder b = makeHorizontalBoulder(i, j, desc);
					blist.add(b);
				}
				// If start of vertical Boulder, put into makeVerticalBoulder
				else if (currCell.equals("^")) {
					Boulder b = makeVerticalBoulder(i, j, desc);
					blist.add(b);
				}
			}
		}
		return blist;
	}
	
	/**
	 * Method to construct a horizontal boulder object when detected in a grid
	 * 
	 * @param startRow indicating the starting row where the boulder is located
	 * @param StartCol indicating the starting column where the boulder is located
	 * @param desc String array of grid
	 * @return Boulder
	 */
	private static Boulder makeHorizontalBoulder(int startRow, int startCol, String[][] desc) {
		// Length starts at 0
		int length = 0;
		int i = startRow;
		int j = startCol;
		String currCell = desc[i][j];
		
		while (!currCell.equals("]")) {
			// While we are not at the end of the boulder, add 1 to length, move to the right by one, and update currCell
			// Dont need to worry about index as the boulder HAS to end before we reach an index out of range
			length+=1;
			j += 1;
			currCell = desc[i][j];
		}
		// Compensate for last cell
		length+=1;
		
		Boulder b = new Boulder(startRow, startCol, length, Orientation.HORIZONTAL);
		return b;
	}
	
	/**
	 * Method to construct a Vertical boulder object when detected in a grid
	 * 
	 * @param startRow indicating the starting row where the boulder is located
	 * @param StartCol indicating the starting column where the boulder is located
	 * @param desc String array of grid
	 * @return Boulder
	 */	
	private static Boulder makeVerticalBoulder(int startRow, int startCol, String[][] desc) {
		// Length starts at 0
		int length = 0;
		int i = startRow;
		int j = startCol;
		String currCell = desc[i][j];
		
		while (!currCell.equals("v")) {
			// While we are not at the end of the boulder, add 1 to length, move down by one, and update currCell
			// Dont need to worry about index as the boulder HAS to end before we reach an index out of range
			length+=1;
			i += 1;
			currCell = desc[i][j];
		}
		//Compensate for last cell
		length += 1;
		
		Boulder b = new Boulder(startRow, startCol, length, Orientation.VERTICAL);
		return b;
	}
}
