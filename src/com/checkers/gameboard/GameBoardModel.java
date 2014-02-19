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
	private int[] focusedLoc = { -10, -10 }; // dummy initialization
	private ArrayList<int[]> movesOnFocus = new ArrayList<int[]>();
	private boolean playerBlackTurn = true;
	private int redScore = 0;
	private int blackScore = 0;
	private final int[][] mInitialBoard = { 
			{ 0, 2, 0, 2, 0, 2, 0, 2 },
			{ 2, 0, 2, 0, 2, 0, 2, 0 }, 
			{ 0, 2, 0, 2, 0, 2, 0, 2 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
			{ 1, 0, 1, 0, 1, 0, 1, 0 },
			{ 0, 1, 0, 1, 0, 1, 0, 1 },
			{ 1, 0, 1, 0, 1, 0, 1, 0 } };

	// used as board reference values
	// can make this generated for different screen resolutions
	private int[] mPositionArray = { 0, 60, 120, 180, 240, 300, 360, 420, 480 };

	public GameBoardModel() {
		this.mBoard = mInitialBoard;
	}

	public GameBoardModel(int[][] board, boolean blackTurn) {
		this.mBoard = board;
		this.playerBlackTurn = blackTurn;
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
			if (inRange(mPosX - BOARDER, mPositionArray[i], mPositionArray[i + 1]))
				ID[1] = i;
			if (inRange(mPosY - BOARDER, mPositionArray[i], mPositionArray[i + 1]))
				ID[0] = i;
		}
		// System.out.printf("\nID: row %d col %d : mouse click: x %d y %d\n",
		// ID[0] + 1, ID[1] + 1, mPosX, mPosY);
		return ID;
	}

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
	 * STATUS: working -returns the upper left corner coordinates
	 */
	public int[] IDtoCoordinate(int[] ID) {
		if (ID[0] > 7 || ID[1] > 7) {
			System.out.println("you are crazy");
		}
		int[] coord = new int[2];
		coord[1] = mPositionArray[ID[0]] + BOARDER;
		coord[0] = mPositionArray[ID[1]] + BOARDER;
		// System.out.printf("\nCoord gen: x %d y %d: ID: row %d col %d\n",
		// coord[0], coord[1], ID[0] + 1, ID[1] + 1);
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
		System.out.println((int) Math.abs(selectedID[0]- selectedID[0] ));
		if (((int) Math.abs(selectedID[0]- moveToID[0] )== 2)){
			setBoardValue(calcIdToRemove(selectedID, moveToID), EMPTY_SPACE);
			updateScore();
			System.out.println("red: " + getRedScore());
			System.out.println("balck: " + getBlackScore());
		}
		if (checkTileAvailiblity(moveToID)) {
			setBoardValue(moveToID, getIdValue(selectedID));
			setBoardValue(genId(selectedID[0], selectedID[1]), EMPTY_SPACE);
		} else {
			System.out.println("\nTile is Occupied");
		}
	}
	
	private int[] calcIdToRemove(int[] selectedID, int[] moveToID){
		return genId((selectedID[0] + moveToID[0])/2,(selectedID[1] + moveToID[1])/2);
	}

	/*
	 * TODO: -->**add checker piece check** -Returns an array of tile locations
	 * that are possible moves -In the view highlight this tiles after check
	 */
	public ArrayList<int[]> getPossibleMoves(int[] ID) {
		int[] checkLeftRight = { -1, 1, -1, 1 };
		int[] checkUpDown = { -1, -1, 1, 1 };

		ArrayList<int[]> moves = new ArrayList<int[]>();
		for (int i = 0; i < 4; i++) {
			int[] tempID = new int[2];
			if (((getIdCol(ID) == 0 && i == 0) || (getIdCol(ID) == 0 && i == 2)) || ((getIdCol(ID) == 7 && i == 1) || (getIdCol(ID) == 7 && i == 3))) {
				continue;
			} else {
				tempID[0] = getIdRow(ID) + checkUpDown[i];
				tempID[1] = getIdCol(ID) + checkLeftRight[i];

				if (getIdValue(ID) == CHECKER_BLACK && tempID[0] < getIdRow(ID)) {
					if (checkTileAvailiblity(tempID)) {
						moves.add(tempID);
					} else if ((getIdValue(tempID) == CHECKER_RED || getIdValue(tempID) == CHECKER_RED_KING)
							&& checkTileAvailiblity(genId(tempID[0] + checkUpDown[i], tempID[1] + checkLeftRight[i]))) {
						moves.add(genId(tempID[0] + checkUpDown[i], tempID[1] + checkLeftRight[i]));
					}
					
				} else if (getIdValue(ID) == CHECKER_RED && tempID[0] > getIdRow(ID)) {
					if (checkTileAvailiblity(tempID)) {
						moves.add(tempID);
					} else if ((getIdValue(tempID) == CHECKER_BLACK || getIdValue(tempID) == CHECKER_BLACK_KING)
							&& checkTileAvailiblity(genId(tempID[0] + checkUpDown[i], tempID[1] + checkLeftRight[i]))) {
						moves.add(genId(tempID[0] + checkUpDown[i], tempID[1] + checkLeftRight[i]));
					}
					
				} else if (checkTileAvailiblity(tempID) && (getIdValue(ID) == CHECKER_BLACK_KING || getIdValue(ID) == CHECKER_RED_KING)) {
					moves.add(tempID);
				}
			}
		}
		return moves;

	}

//	private int getOppositeIdValue(int[] ID) {
//		switch (getIdValue(ID)) {
//		case CHECKER_BLACK:
//			return CHECKER_RED;
//		case CHECKER_RED:
//			return CHECKER_BLACK;
//		case CHECKER_BLACK_KING:
//			return CHECKER_RED_KING;
//		case CHECKER_RED_KING:
//			return CHECKER_BLACK;
//		default:
//			return 0;
//		}
//	}

	public int getIdValue(int[] ID) {
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

	public int[] genId(int ID_1, int ID_2) {
		int[] temp = { ID_1, ID_2 };
		return temp;
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
	
	public void increaseRedScore(){
		
	}

	public ArrayList<int[]> getMovesOnFocus() {
		return movesOnFocus;
	}

	public void setMovesOnFocus(ArrayList<int[]> movesOnFocus) {
		this.movesOnFocus = movesOnFocus;
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

	public void removeMovesOnFocus() {
		this.movesOnFocus.clear();
	}

	public boolean isIdTurn(int[] id) {
		// black turn is true
		// red turn is false
		if ((getIdValue(id) == CHECKER_BLACK || getIdValue(id) == CHECKER_BLACK_KING) && isPlayerBlackTurn())
			return true;
		else if ((getIdValue(id) == CHECKER_RED || getIdValue(id) == CHECKER_RED_KING) && isPlayerRedTurn())
			return true;
		else
			return false;
	}
	
	public void updateScore(){
		if (isPlayerBlackTurn()){
			this.blackScore++;
		}else {
			this.redScore++;
		}
	}

	public int getRedScore() {
		return redScore;
	}


	public int getBlackScore() {
		return blackScore;
	}
}
