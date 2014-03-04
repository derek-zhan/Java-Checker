package com.checkers.gameboard;

import java.util.ArrayList;

/*
 *TODO: 
 *-create static class for helper methods to clean things up
 * */
public class GameBoardModel {
	public static final int EMPTY_SPACE = 0;
	public static final int CHECKER_BLACK = 1;
	public static final int CHECKER_RED = 2;
	public static final int CHECKER_BLACK_KING = 3;
	public static final int CHECKER_RED_KING = 4;
	public static final int BOARDER = 0; // boarder pixels
	// used to move up down
	private int[][] mBoard;
	// dummy initialization
	private int[] focusedLoc = { -10, -10 };
	private ArrayList<int[]> movesOnFocus = new ArrayList<int[]>();
	// always start with black
	private boolean playerBlackTurn = true;
	private int redScore = 0;
	private int blackScore = 0;
	// defines how a piece is searched
	private final int[] CHECK_LEFT_RIGHT = { -1, 1, -1, 1 };
	private final int[] CHECK_UP_DOWN = { -1, -1, 1, 1 };
	// default game board
	private final int[][] mInitialBoard = { 
			{ 0, 2, 0, 2, 0, 2, 0, 2 },
			{ 2, 0, 2, 0, 2, 0, 2, 0 }, 
			{ 0, 2, 0, 2, 0, 2, 0, 2 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
			{ 1, 0, 1, 0, 1, 0, 1, 0 }, 
			{ 0, 1, 0, 1, 0, 1, 0, 1 }, 
			{ 1, 0, 1, 0, 1, 0, 1, 0 } };
	
	private final int[][] blankBoard = {
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0 } };

	/*-used as board reference values
	 *-can make this generated for different screen resolutions
	 */
	private int[] mPositionArray = { 0, 60, 120, 180, 240, 300, 360, 420, 480 };

	// sets default board and default turn
	public GameBoardModel() {
		this.mBoard = mInitialBoard;
	}

	// constructor with option to set turn and board
	public GameBoardModel(int[][] board, boolean blackTurn) {
		this.mBoard = board;
		this.playerBlackTurn = blackTurn;
	}

	/*
	 * INPUTS: -integer mouse Position X -integer mouse Position YFUNCTION:
	 * -generates the ID of the mouse click on the playing board -assumes board
	 * is 480x480OUTPUT: -ID array -->length 2
	 */
	public int[] coordinateToID(int mPosX, int mPosY) {
		int[] ID = new int[2];
		for (int i = 0; i < mPositionArray.length - 1; i++) {
			if (inRange(mPosX - BOARDER, mPositionArray[i], mPositionArray[i + 1]))
				ID[1] = i;
			if (inRange(mPosY - BOARDER, mPositionArray[i], mPositionArray[i + 1]))
				ID[0] = i;
		}
		return ID;
	}

	/*
	 * INPUTS:- Tile iD FUNCTION: -Checks if the input ID is the same as the
	 * currently focused ID OUTPUT: - boolean
	 */
	public boolean isFocused(int[] id) {
		if (focusedLoc[0] == id[0] && focusedLoc[1] == id[1])
			return true;
		else
			return false;
	}

	public void setFocusedLocation(int[] id) {
		this.focusedLoc = id;
	}

	public int[] getFocusedLocation() {
		return this.focusedLoc;
	}

	/*
	 * INPUTS: - ID of tile FUNCTION: -returns the co-ordinates of the upper
	 * left corner of the tile OUTPUT: -integer array of legnth 2
	 */
	public int[] IDtoCoordinate(int[] ID) {
		if (ID[0] > 7 || ID[1] > 7) {
			System.out.println("ID error");
		}
		int[] coord = new int[2];
		coord[1] = mPositionArray[ID[0]] + BOARDER;
		coord[0] = mPositionArray[ID[1]] + BOARDER;
		return coord;
	}

	/*
	 * INPUTS:-integer values, x, y, nFUNCTION:-returns true if x<=n<y otherwise
	 * false-helper method for coordToID
	 */
	private boolean inRange(int n, int x, int y) {
		if (x <= n && n < y)
			return true;
		else
			return false;
	}

	/*
	 * INPUTS: -ID selected -ID to jump to FUNCTION: -moves the piece from
	 * selected ID to the Jump to ID
	 */
	public void movePiece(int[] selectedID, int[] moveToID) {
		if (shouldUpdateScore(selectedID, moveToID)) {
			setBoardValue(calcIdToRemove(selectedID, moveToID), EMPTY_SPACE);
			updateScore();
			System.out.println("red: " + getRedScore());
			System.out.println("black: " + getBlackScore());
		}
		if (checkTileAvailiblity(moveToID)) {
			setBoardValue(moveToID, getIdValue(selectedID));
			setBoardValue(genId(selectedID[0], selectedID[1]), EMPTY_SPACE);
		} else {
			System.out.println("\nTile is Occupied");
		}
	}

	/*
	 * INPUTS: -ID selected -ID to jump to FUNCTION: -calculates the id to be
	 * removed when a piece jumps the opponent
	 */
	private int[] calcIdToRemove(int[] selectedID, int[] moveToID) {
		return genId((selectedID[0] + moveToID[0]) / 2, (selectedID[1] + moveToID[1]) / 2);
	}

	/*
	 * TODO: -->**add checker piece check** -Returns an array of tile locations
	 * that are possible moves -In the view highlight this tiles after check
	 */
	public ArrayList<int[]> getPossibleMoves(int[] ID) {

		ArrayList<int[]> moves = new ArrayList<int[]>();
		for (int i = 0; i < 4; i++) {
			int[] tempID = new int[2];
			if (isOnLeftOrRightEdge(ID, i) || isTopOrBottomEdge(ID, i)) {
				continue;
			} else {
				// creates temporary Id's surrounding focused ID
				tempID[0] = getIdRow(ID) + CHECK_UP_DOWN[i];
				tempID[1] = getIdCol(ID) + CHECK_LEFT_RIGHT[i];
				// Logic: testing every possible steps by finding the biggest possible step
				// Problem: only single jump
				if (getIdValue(ID) == CHECKER_BLACK && tempID[0] < getIdRow(ID)) {
					if (checkTileAvailiblity(tempID)) {
						moves.add(tempID);
					} else if ((getIdValue(tempID) == CHECKER_RED || getIdValue(tempID) == CHECKER_RED_KING)
							&& checkTileAvailiblity(genId(tempID[0] + CHECK_UP_DOWN[i], tempID[1] + CHECK_LEFT_RIGHT[i]))) {
						moves.add(genId(tempID[0] + CHECK_UP_DOWN[i], tempID[1] + CHECK_LEFT_RIGHT[i]));
					}

				} else if (getIdValue(ID) == CHECKER_RED && tempID[0] > getIdRow(ID)) {
					if (checkTileAvailiblity(tempID)) {
						moves.add(tempID);
					} else if ((getIdValue(tempID) == CHECKER_BLACK || getIdValue(tempID) == CHECKER_BLACK_KING)
							&& checkTileAvailiblity(genId(tempID[0] + CHECK_UP_DOWN[i], tempID[1] + CHECK_LEFT_RIGHT[i]))) {
						moves.add(genId(tempID[0] + CHECK_UP_DOWN[i], tempID[1] + CHECK_LEFT_RIGHT[i]));
					}

				} else if (checkTileAvailiblity(tempID) && (getIdValue(ID) == CHECKER_BLACK_KING || getIdValue(ID) == CHECKER_RED_KING)) {
					moves.add(tempID);
				}
			}
		}
		return moves;

	}
	// detect whether its left or right edge
	private boolean isOnLeftOrRightEdge(int[] ID, int searchIndex) {
		return ((getIdCol(ID) == 0 && searchIndex == 0) || (getIdCol(ID) == 0 && searchIndex == 2))
				|| ((getIdCol(ID) == 7 && searchIndex == 1) || (getIdCol(ID) == 7 && searchIndex == 3));
	}
	// detect whether its top or bottom edge
	private boolean isTopOrBottomEdge(int[] ID, int searchIndex) {
		return (((getIdRow(ID) == 0 && searchIndex == 0) || (getIdRow(ID) == 0 && searchIndex == 1)) 
				|| (((getIdRow(ID) == 7 && searchIndex == 2) || (getIdRow(ID) == 7 && searchIndex == 3))));

	}

	public int getIdValue(int[] ID) {
		return mBoard[ID[0]][ID[1]];
	}

	private int getIdRow(int[] ID) {
		return ID[0];
	}

	private int getIdCol(int[] ID) {
		return ID[1];
	}

	public void setBoardValue(int[] ID, int value) {
		mBoard[ID[0]][ID[1]] = value;
	}

	public int[] genId(int ID_1, int ID_2) {
		int[] temp = { ID_1, ID_2 };
		return temp;
	}

	private boolean shouldUpdateScore(int[] selectedID, int[] moveToID) {
		return (((int) Math.abs(selectedID[0] - moveToID[0]) == 2));
	}

	public boolean checkTileAvailiblity(int[] ID) {
		if (mBoard[ID[0]][ID[1]] == EMPTY_SPACE)
			return true;
		else
			return false;
	}

	public int[] getmPositionArray() {
		return mPositionArray;
	}

	public int[][] getmBoard() {
		return mBoard;
	}

	public void setmBoard(int[][] mBoard) {
		this.mBoard = mBoard;
	}

	public ArrayList<int[]> getMovesOnFocus() {
		return movesOnFocus;
	}

	public void setMovesOnFocus(ArrayList<int[]> movesOnFocus) {
		this.movesOnFocus = movesOnFocus;
	}

	public boolean isPlayerBlackTurn() {
		return playerBlackTurn;
	}

	public boolean isPlayerRedTurn() {
		return !playerBlackTurn;
	}

	public void setPlayerBlackTurn(boolean bool) {
		this.playerBlackTurn = bool;
	}

	public boolean isIdTurn(int[] id) {
		// using boolean value to decide whose turn
		// black turn is true
		// red turn is false
		if ((getIdValue(id) == CHECKER_BLACK || getIdValue(id) == CHECKER_BLACK_KING) && isPlayerBlackTurn())
			return true;
		else if ((getIdValue(id) == CHECKER_RED || getIdValue(id) == CHECKER_RED_KING) && isPlayerRedTurn())
			return true;
		else
			return false;
	}

	public void updateScore() {
		if (isPlayerBlackTurn()) {
			this.blackScore++;
		} else {
			this.redScore++;
		}
	}

	public int getRedScore() {
		return redScore;
	}

	public int getBlackScore() {
		return blackScore;
	}
/*
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
*/
	public void removeMovesOnFocus() {
		this.movesOnFocus.clear();
	}
	
	// returns true if tile is dark on the board
	public boolean isDarkTile(int[] ID) {
		if (((int) Math.pow(-1, ID[0] + ID[1])) == -1) {
			return true;
		} else {
			return false;
		}
	}
	
	public int[][] getBlankBoard() {
		return blankBoard;
	}
	
	
}
