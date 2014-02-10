package com.checkers.gameboard;

import java.util.ArrayList;

/*
 *TODO: 
 *-create static class for helper methods to clean things up
 * */
public class GameBoard {
	private final int EMPTY_SPACE = 0;
	private final int CHECKER_BLACK = 1;
	private final int CHECKER_RED = 2;
	private final int CHECKER_BLACK_KING = 3;
	private final int CHECKER_RED_KING = 4;
	// used to move up down
	private static int[][] mBoard;
	private final int[][] mInitialBoard = { 
			{ 0, 2, 0, 2, 0, 2, 0, 2 },
			{ 2, 0, 2, 0, 2, 0, 2, 0 }, 
			{ 0, 2, 0, 2, 0, 2, 0, 2 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
			{ 1, 0, 1, 0, 1, 0, 1, 0 }, 
			{ 0, 1, 0, 1, 0, 1, 0, 1 }, 
			{ 1, 0, 1, 0, 1, 0, 1, 0 } };
	
	//used as board reference values	
	//can make this generated for different screen resolutions
	private int[] mPositionArray = { 0, 60, 120, 180, 240, 300, 360, 420, 480 }; 
	public GameBoard() {
		mBoard = mInitialBoard;
	}

	/*
	 * STATUS: working -calculates the ID of mouse click -currently assumes
	 * 480x480 board -this can be changed in mPositionArray -public for now this
	 * will likely change
	 */
	public int[] coordinateToID(int mPosX, int mPosY) {
		int[] ID = new int[2];
		// should convert to while loop with stopping condition when both are
		// found
		for (int i = 0; i < mPositionArray.length - 1; i++) {
			if (inRange(mPosX, mPositionArray[i], mPositionArray[i + 1]))
				ID[1] = i;
			if (inRange(mPosY, mPositionArray[i], mPositionArray[i + 1]))
				ID[0] = i;
		}
		System.out.printf("\nID: row %d col %d : mouse click: x %d y %d\n", ID[0] + 1, ID[1] + 1, mPosX, mPosY);
		return ID;
	}

	/*
	 * STATUS: working -returns the upper left corner coordinates
	 */
	public int[] IDtoCoordinate(int[] ID) {
		if (ID[0] > 7 || ID[1] > 7)
			System.out.println("your crazy");
		int[] coord = new int[2];
		coord[1] = mPositionArray[ID[0]];
		coord[0] = mPositionArray[ID[1]];
		System.out.printf("\nCoord gen: x %d y %d: ID: row %d col %d\n", coord[0], coord[1], ID[0] + 1, ID[1] + 1);
		return coord;
	}

	/*
	 * STATUS: working -checks if x<=n<y returns true if they are otherwise
	 * false -helper method for coordToID
	 */
	private boolean inRange(int n, int x, int y) {
		if (x <= n && n < y)
			return true;
		else
			return false;
	}

	public void movePeice(int[] selectedID, int[] moveToID) {
		if (checkTileAvailiblity(moveToID)) {
			setBoardValue(moveToID, getIdValue(selectedID));
			mBoard[selectedID[0]][selectedID[1]] = EMPTY_SPACE;
		} else {
			System.out.println("\nTile is Occupied");
		}
	}

	/*
	 * TODO: -->**add checker piece check** -Returns an array of tile locations
	 * that are possible moves -In the view highlight this tiles after check
	 */
	public ArrayList<int[]> getPossibleMoves(int[] ID) {
		int[] checkLeftRight = { -1, 1, -1, 1 };
		int[] checkUpDown = { -1, -1, 1, 1 };

		ArrayList<int[]> moves = new ArrayList<int[]>();
		int[] tempID = new int[2];
		for (int i = 0; i < 4; i++) {
			if (((getIdCol(ID) == 0 && i == 0) || (getIdCol(ID) == 0 && i == 2)) || ((getIdCol(ID) == 7 && i == 1) || (getIdCol(ID) == 7 && i == 3))) {
				continue;
			} else {
				tempID[0] = getIdRow(ID) + checkUpDown[i];
				tempID[1] = getIdCol(ID) + checkLeftRight[i];
				if (checkTileAvailiblity(tempID) && getIdValue(ID) == CHECKER_BLACK && tempID[0] < getIdRow(ID)) {
					moves.add(tempID);
					System.out.println("\ncurrent ID: " + ID[0] + " " + ID[1] + " Possible Move ID: Row " + (tempID[0] + 1) + " col " + (tempID[1] + 1));
				} else if (checkTileAvailiblity(tempID) && getIdValue(ID) == CHECKER_RED && tempID[0] > getIdRow(ID)) {
					moves.add(tempID);
					System.out.println("\ncurrent ID: " + ID[0] + " " + ID[1] + " Possible Move ID: Row " + (tempID[0] + 1) + " col " + (tempID[1] + 1));
				} else if (checkTileAvailiblity(tempID) && (getIdValue(ID)== CHECKER_BLACK_KING || getIdValue(ID)==CHECKER_RED_KING)){
					moves.add(tempID);
					System.out.println("\ncurrent ID: " +ID[0] + " " + ID[1] + " Possible Move ID: Row " + (tempID[0] + 1) + " col " + (tempID[1] + 1));
				}
			}
		}
		return moves;

	}

	private int getIdValue(int[] ID) {
		return mBoard[ID[0]][ID[1]];
	}

	private int getIdRow(int[] ID) {
		return ID[0];
	}

	private int getIdCol(int[] ID) {
		return ID[1];
	}

	private void setBoardValue(int[] ID, int value) {
		mBoard[ID[0]][ID[1]] = value;
	}

	public boolean checkTileAvailiblity(int[] ID) {
		if (mBoard[ID[0]][ID[1]] == 0)
			return true;
		else
			return false;
	}

	public void printGameBoard() {
		System.out.print("Current Game View \n---------------\n");
		for (int i = 0; i < mBoard.length; i++) {
			for (int j = 0; j < mBoard.length; j++) {
				System.out.print(mBoard[i][j] + " ");
			}
			System.out.println();
		}
		System.out.print("---------------");
	}

	public static void main(String[] args) {
		GameBoard gameBoard = new GameBoard();
		gameBoard.printGameBoard();
		gameBoard.coordinateToID(65, 25);
		int[] id = { 0, 1 };
		gameBoard.IDtoCoordinate(id);
		// int[] id1 = {0,1};
		int[] id2 = { 2 , 1 };
		ArrayList<int[]> moves = gameBoard.getPossibleMoves(id2);
		gameBoard.movePeice(id2, moves.get(1));
		//gameBoard.printGameBoard();
		//ArrayList<int[]> moves2 = gameBoard.getPossibleMoves(moves.get(1));

		// gameBoard.movePeice(id1,id2);
		// gameBoard.printGameBoard();

	}
}
