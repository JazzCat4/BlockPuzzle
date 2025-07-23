package hw3;

import static api.Direction.*;
import static api.Orientation.*;

import java.util.ArrayList;

import api.Cell;
import api.Direction;
import api.Move;
import api.Orientation;

/**
 * Represents a board in the game. A board contains a 2D grid of cells and a
 * list of boulders that slide over the cells.
 * @Author Ethen Santana
 */
public class Board {
	/**
	 * 2D array of cells, the indexes signify (row, column) with (0, 0) representing
	 * the upper-left corner of the board.
	 */
	private Cell[][] grid;

	/**
	 * A list of boulders that are positioned on the board.
	 */
	private ArrayList<Boulder> boulders;

	/**
	 * A list of moves that have been made in order to get to the current position
	 * of boulders on the board.
	 */
	private ArrayList<Move> moveHistory;
	
	/*
	 * Integer representing the amount of moves that have happened
	 */
	private int moveCount;
	
	/*
	 * Current boulder being grabbed, if none = null
	 */
	private Boulder grabbedBoulder;
	
	private boolean gameOver;

	/**
	 * Constructs a new board from a given 2D array of cells and list of boulders. The
	 * cells of the grid should be updated to indicate which cells have boulders
	 * placed over them (i.e., setBoulder() method of Cell). The move history should
	 * be initialized as empty.
	 * 
	 * @param grid   a 2D array of cells which is expected to be a rectangular shape
	 * @param boulders list of boulders already containing row-column position which
	 *               should be placed on the board
	 */
	public Board(Cell[][] grid, ArrayList<Boulder> boulders) {
		
		this.grid = grid;
		this.boulders = boulders;
		grabbedBoulder = null;
		
		
		// Iterate through arraylist of boulders to set each boulder in the grid
		int row;
		int column;
		
		// Iterate through arraylist of boulders to set each boulder in the grid
		for (Boulder b : boulders) {
			// Get row and column of boulder
			row = b.getFirstRow();
			column = b.getFirstCol();
			
			// Create boulder on grid using placeBoulder() using given row and column as indexes for grid
			// Because boulders can have a length > 1 cell, we need to iterate through its length and place the same boulder for each cell it occupies
			if (b.getOrientation() == Orientation.HORIZONTAL) {
				for (int i=0; i < b.getLength(); i++) {
					grid[row][column+i].placeBoulder(b);
				}
			}
			else {
				for (int i=0; i < b.getLength(); i++) {
					grid[row+i][column].placeBoulder(b);
				}
			}
		}
		
		// Initialize move history
		moveHistory = new ArrayList<Move>();
		
		gameOver = false;
	}

	/**
	 * DO NOT MODIFY THIS CONSTRUCTOR
	 * <p>
	 * Constructs a new board from a given 2D array of String descriptions.
	 * 
	 * @param desc 2D array of descriptions
	 */
	public Board(String[][] desc) {
		this(GridUtil.createGrid(desc), GridUtil.findBoulders(desc));
	}

	/**
	 * Returns the number of rows of the board.
	 * 
	 * @return number of rows
	 */
	public int getRowSize() {
		return grid.length;
	}

	/**
	 * Returns the number of columns of the board.
	 * 
	 * @return number of columns
	 */
	public int getColSize() {
		return grid[0].length;
	}

	/**
	 * Returns the cell located at a given row and column.
	 * 
	 * @param row the given row
	 * @param col the given column
	 * @return the cell at the specified location
	 */
	public Cell getCellAt(int row, int col) {
		return grid[row][col];
	}

	/**
	 * Returns the total number of moves (calls to moveGrabbedBoulder which
	 * resulted in a boulder being moved) made so far in the game.
	 * 
	 * @return the number of moves
	 */
	public int getMoveCount() {
		return moveHistory.size();
	}

	/**
	 * Returns a list of all boulders on the board.
	 * 
	 * @return a list of all boulders
	 */
	public ArrayList<Boulder> getBoulders() {
		return boulders;
	}

	/**
	 * Returns true if the player has completed the puzzle by positioning a boulder
	 * over an exit, false otherwise.
	 * 
	 * @return true if the game is over
	 */
	public boolean isGameOver() {
		
		//Go through each boulder in the arraylist of boulders, and go through each of their cells to check each on individually to see if one is an exit cell
		for (Boulder b : boulders) {
			
			// For horizontal boulders
			if (b.getOrientation() == Orientation.HORIZONTAL) {
				for (int i = 0; i < b.getLength(); ++i) {
					
					if (grid[b.getFirstRow()][b.getFirstCol()+i].isExit()) {
						gameOver = true;
						return gameOver;
					}
				}
			}
			
			// For vertical boulders
			else {
				for (int i =0; i < b.getLength(); ++i) {
					
					if (grid[b.getFirstRow() + i][b.getFirstCol()].isExit()) {
						gameOver = true;
						return gameOver;
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Models the user grabbing (mouse button down) a boulder over the given row and
	 * column. The purpose) of grabbing a boulder is for the user to be able to drag
	 * the boulder to a new position, which is performed by calling
	 * moveGrabbedBoulder().
	 * <p>
	 * This method should find which boulder has been grabbed (if any) and record
	 * that boulder as grabbed in some way.
	 * 
	 * @param row row to grab the boulder from
	 * @param col column to grab the boulder from
	 */
	public void grabBoulderAt(int row, int col) {
		// Set Boulder variable grabbed boulder to the boulder in row and col. If no boulder, grabbedBoulder is null
		grabbedBoulder = grid[row][col].getBoulder();
		
	}

	/**
	 * Models the user releasing (mouse button up) the currently grabbed boulder
	 * (if any). Update the object accordingly to indicate no boulder is
	 * currently being grabbed.
	 */
	public void releaseBoulder() {
		grabbedBoulder = null;
	}

	/**
	 * Returns the currently grabbed boulder. If there is no currently grabbed
	 * boulder the method return null.
	 * 
	 * @return the currently grabbed boulder or null if none
	 */
	public Boulder getGrabbedBoulder() {
		return grabbedBoulder;
	}

	/**
	 * Returns true if the cell at the given row and column is available for a
	 * boulder to be placed over it. Boulders can only be placed over ground
	 * and exits. Additionally, a boulder cannot be placed over a cell that is
	 * already occupied by another boulder.
	 * 
	 * @param row row location of the cell
	 * @param col column location of the cell
	 * @return true if the cell is available for a boulder, otherwise false
	 */
	public boolean isAvailable(int row, int col) {
		// If the cell is type ground or exit, and there is no boulder, return true
		if ((grid[row][col].isGround() || grid[row][col].isExit()) && (grid[row][col].getBoulder() == null)) {
			return true;
		}
		return false;
	}

	/**
	 * Moves the currently grabbed boulder by one cell in the given direction. A
	 * horizontal boulder is only allowed to move right and left and a vertical boulder
	 * is only allowed to move up and down. A boulder can only move over a cell that
	 * is a floor or exit and is not already occupied by another boulder. The method
	 * does nothing under any of the following conditions:
	 * <ul>
	 * <li>The game is over.</li>
	 * <li>No boulder is currently grabbed by the user.</li>
	 * <li>A boulder is currently grabbed by the user, but the boulder is not allowed to
	 * move in the given direction.</li>
	 * </ul>
	 * If none of the above conditions are meet, the method does at least the following:
	 * <ul>
	 * <li>Moves the boulder object by calling its move() method.</li>
	 * <li>Calls placeBoulder() for the grid cell that the boulder is being moved into.</li>
	 * <li>Calls removeBoulder() for the grid cell that the boulder is being moved out of.</li>
	 * <li>Adds the move (as a Move object) to the end of the move history list.</li>
	 * <li>Increments the count of total moves made in the game.</li>
	 * </ul>
	 * 
	 * @param dir the direction to move
	 */
	public void moveGrabbedBoulder(Direction dir) {
		// If the game is not over, and there is a grabbed boulder:
		if (!isGameOver() && grabbedBoulder != null) {
			
			// If the orientation of the boulder is horizontal and the directions are left or right
			if (grabbedBoulder.getOrientation() == Orientation.HORIZONTAL && (dir == Direction.LEFT || dir == Direction.RIGHT)) {
				
				
				int row = grabbedBoulder.getFirstRow();
				int col = grabbedBoulder.getFirstCol();
				
				// FOR LEFT DIRECTION
				if (dir == Direction.LEFT) {
					// If space is not available, end function
					if (!isAvailable(row, col-1)) {
						return;
					}
					// We can now move the boulder
					grabbedBoulder.move(dir);
					
					// update variables
					row = grabbedBoulder.getFirstRow();
					col = grabbedBoulder.getFirstCol();
					
					// Place boulder on new cell
					grid[row][col].placeBoulder(grabbedBoulder);
					
					// Remove boulder on old cell
					grid[row][col + grabbedBoulder.getLength()].removeBoulder();
				}
				
				// FOR RIGHT DIRECTIOn
				if (dir == Direction.RIGHT) {
					// If space is not available, end function
					if (!isAvailable(row, col + grabbedBoulder.getLength())) {
						return;
					}
					// We can now move the boulder
					grabbedBoulder.move(dir);
					
					// update variables
					row = grabbedBoulder.getFirstRow();
					col = grabbedBoulder.getFirstCol();
					
					// Place boulder on new cell
					grid[row][col + grabbedBoulder.getLength() - 1].placeBoulder(grabbedBoulder);
					
					// Remove boulder on old cell
					grid[row][col - 1].removeBoulder();
				}
				
				
				//Add move to move history
				Move m = new Move(grabbedBoulder, dir);
				moveHistory.add(m);
				// Increment move count
				moveCount += 1;
				
			}
			
			// If the orientation of the boulder is vertical and the directions are up or down
			if (grabbedBoulder.getOrientation() == Orientation.VERTICAL && (dir == Direction.UP || dir == Direction.DOWN)) {
							
							
				int row = grabbedBoulder.getFirstRow();
				int col = grabbedBoulder.getFirstCol();
								
				// FOR UP DIRECTION
				if (dir == Direction.UP) {
					// If space is not available, end function
					if (!isAvailable(row-1, col)) {
						return;
					}
					// We can now move the boulder
					grabbedBoulder.move(dir);
					
					// update variables
					row = grabbedBoulder.getFirstRow();
					col = grabbedBoulder.getFirstCol();
					
					// Place boulder on new cell
					grid[row][col].placeBoulder(grabbedBoulder);
					
					// Remove boulder on old cell
					grid[row + grabbedBoulder.getLength()][col].removeBoulder();
				}
				
				// FOR RIGHT DIRECTIOn
				if (dir == Direction.DOWN) {
					// If space is not available, end function
					if (!isAvailable(row + grabbedBoulder.getLength(), col)) {
						return;
					}
					// We can now move the boulder
					grabbedBoulder.move(dir);
					
					// update variables
					row = grabbedBoulder.getFirstRow();
					col = grabbedBoulder.getFirstCol();
										
					// Place boulder on new cell
					grid[row + grabbedBoulder.getLength() - 1][col].placeBoulder(grabbedBoulder);
										
					// Remove boulder on old cell
					grid[row - 1][col].removeBoulder();
				}
							
							
				//Add move to move history
				Move m = new Move(grabbedBoulder, dir);
				moveHistory.add(m);
				// Increment move count
				moveCount += 1;
			}
		}
	}

	/**
	 * Resets the state of the game back to the start, which includes the move
	 * count, the move history, and whether the game is over. The method calls the
	 * reset method of each boulder object. It also updates each grid cells by calling
	 * their setBoulder method to either set a boulder if one is located over the cell
	 * or set null if no boulder is located over the cell.
	 */
	public void reset() {
		
		moveCount = 0;
		moveHistory = new ArrayList<Move>();
		
		gameOver = false;
		
		
		// everything regarding boulders
		for (Boulder b : boulders) {
			// Reset cells that each boulder is occupying
			int row = b.getFirstRow();
			int col = b.getFirstCol();			
			// For horizontal boulders
			if (b.getOrientation() == Orientation.HORIZONTAL) {
				for (int i = 0; i < b.getLength(); ++i) {
					grid[row][col+i].placeBoulder(null);
				}
			}
			
			// For vertical boulders
			else {
				for (int i = 0; i < b.getLength(); ++i) {
					grid[row+i][col].placeBoulder(null);
				}
				
			}
			// Reset boulder
			b.reset();
			row = b.getFirstRow();
			col = b.getFirstCol();
			
			// Call placeBoulder on new cells boulder is occupying
			// For horizontal
			if (b.getOrientation() == Orientation.HORIZONTAL) {
				for (int i=0; i < b.getLength(); ++i) {
					grid[row][col+i].placeBoulder(b);
				}
			}
			// For vertical
			else {
				for (int i =0; i < b.getLength(); ++i) {
					grid[row+i][col].placeBoulder(b);
				}
			}
			
		}
	}

	/**
	 * Returns a list of all legal moves that can be made by any boulder on the
	 * current board.
	 * 
	 * @return a list of legal moves
	 */
	public ArrayList<Move> getAllPossibleMoves() {
		ArrayList<Move> allPossibleMoves = new ArrayList<Move>();
		allPossibleMoves.clear();
		
		// Iterate through each boulder in boulders, and depending on orientation, determine if the boulder can be moved up/down or left/right
		for (Boulder b : boulders) {
			// For horizontal boulders
			if (b.getOrientation() == Orientation.HORIZONTAL) {
				int rowToCheck = b.getFirstRow();
				int colToCheck = b.getFirstCol();
				
				// Check to see if boulder can move to the left
				if (isAvailable(rowToCheck, colToCheck-1)) {
					Move m = new Move(b, Direction.LEFT);
					allPossibleMoves.add(m);
				}
				
				// Check to see if boulder can move to the right
				if (isAvailable(rowToCheck, colToCheck + b.getLength())) {
					Move m = new Move(b, Direction.RIGHT);
					allPossibleMoves.add(m);
				}
				
			}
			
			// For vertical boulders
			else {
				int rowToCheck = b.getFirstRow();
				int colToCheck = b.getFirstCol();
				
				// Check to see if boulder can move Up
				if (isAvailable(rowToCheck - 1, colToCheck)) {
					Move m = new Move(b, Direction.UP);
					allPossibleMoves.add(m);
				}
				
				// Check to see if boulder can move Down
				if (isAvailable(rowToCheck + b.getLength(), colToCheck)) {
					Move m = new Move(b, Direction.DOWN);
					allPossibleMoves.add(m);
				}
			}
		}
		
		return allPossibleMoves;
	}

	/**
	 * 
	 * Gets the list of all moves performed to get to the current position on the
	 * board.
	 * 
	 * @return a list of moves performed to get to the current position
	 */
	public ArrayList<Move> getMoveHistory() {
		return moveHistory;
	}

	/**
	 * EXTRA CREDIT 5 POINTS
	 * <p>
	 * This method is only used by the Solver.
	 * <p>
	 * Undo the previous move. The method gets the last move on the moveHistory list
	 * and performs the opposite actions of that move, which are the following:
	 * <ul>
	 * <li>if required, sets is game over to false</li>
	 * <li>grabs the moved boulder and calls moveGrabbedBoulder passing the opposite
	 * direction</li>
	 * <li>decreases the total move count by two to undo the effect of calling
	 * moveGrabbedBoulder twice</li>
	 * <li>removes the move from the moveHistory list</li>
	 * </ul>
	 * If the moveHistory list is empty this method does nothing.
	 */
	public void undoMove() {
		// TODO
		/*
		// Method runs as long as the moveHistory list is not empty
		if (!moveHistory.isEmpty()) {
			// Set gameOver to false, if it is already false then it doesnt matter if this is changed
			gameOver = false;
			
			// Get the last move object performed
			Move m = moveHistory.getLast();
			
			// Grab the last boulder moved
			grabbedBoulder = m.getBoulder();
			
			Direction dir = m.getDirection();
			
			// Get the opposite direction
			if (grabbedBoulder.getOrientation() == Orientation.VERTICAL) {
				if (dir == Direction.DOWN) {
					dir = Direction.UP;
				}
				else {
					dir = Direction.DOWN;
				}
			}
			else {
				if (dir == Direction.LEFT) {
					dir = Direction.RIGHT;
				}
				else {
					dir = Direction.LEFT;
				}
			}
			// Move Boulder
			grabbedBoulder.move(dir);
			
			// Decrease move count by 2
			moveCount -= 2;
			
			// Remove move from moveHistory
			moveHistory.removeLast();
			
		}
		*/
	}

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		boolean first = true;
		for (Cell row[] : grid) {
			if (!first) {
				buff.append("\n");
			} else {
				first = false;
			}
			for (Cell cell : row) {
				buff.append(cell.toString());
				buff.append(" ");
			}
		}
		return buff.toString();
	}
}
