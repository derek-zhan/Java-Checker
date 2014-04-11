package com.checkers.gameboard;

import java.util.ArrayList;
import java.util.Random;

import sun.security.util.Length;

/**
 * GameBoardModel is the backbone or model of the checkers application. This
 * Class contains all crucial methods for the checkers application including the
 * representation of the board and pieces
 * 
 * @author SONY
 * @version 0.3
 * @since 06-04-2014
 * */
public class GameBoardModel {
	public static final int EMPTY_SPACE = 0;
	public static final int CHECKER_BLACK = 1;
	public static final int CHECKER_RED = 2;
	public static final int CHECKER_BLACK_KING = 3 * CHECKER_BLACK;
	public static final int CHECKER_RED_KING = 3 * CHECKER_RED;
	// boarder pixels
	public static final int BOARDER = 0;

	// used to move up down
	private int[][] mBoard;
	// dummy initialization

	private int[] focusedLoc = { -10, -10 };
	private ArrayList<int[]> movesOnFocus = new ArrayList<int[]>();

	// always start with black
	private boolean playerBlackTurn = false;
	private int redScore = 0;
	private int blackScore = 0;
	private boolean blackComp = false;
	private boolean redComp = false;
	private boolean lastStand = false;
	private boolean gameOver = false;
	private int tryLimit = 10;

	// defines how a piece is searched
	private final int[] CHECK_LEFT_RIGHT = { -1, 1, -1, 1 };
	private final int[] CHECK_UP_DOWN = { -1, -1, 1, 1 };

	// default game board
	private final int[][] mInitialBoard = { { 0, 2, 0, 2, 0, 2, 0, 2 }, { 2, 0, 2, 0, 2, 0, 2, 0 }, { 0, 2, 0, 2, 0, 2, 0, 2 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 1, 0, 1, 0, 1, 0, 1, 0 }, { 0, 1, 0, 1, 0, 1, 0, 1 }, { 1, 0, 1, 0, 1, 0, 1, 0 } };

	// blank empty board
	private final int[][] blankBoard = { { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } };

	// used as board reference values
	private final int[] BOARD_REF_POSITION = { 0, 60, 120, 180, 240, 300, 360, 420, 480 };

	/**
	 * Class constructor with option to set the computer variables
	 * 
	 * @param isBlackComp
	 *            boolean is set true is BLACK is computer
	 * @param isRedComp
	 *            boolean is set true if RED is computer
	 * */
	public GameBoardModel(boolean isBlackComp, boolean isRedComp) {
		this.mBoard = mInitialBoard;
		this.blackComp = isBlackComp;
		this.redComp = isRedComp;
		this.gameOver = false;
		// timer.setStart();
	}

	/**
	 * Class constructor with option to set board, turn, and red and black score
	 * 
	 * @param board
	 *            2D array of length 8x8 representing the checkers board
	 * @param blackTurn
	 *            boolean of current/initial turn
	 * @param blackScore
	 *            integer of the black Score
	 * @param redScore
	 *            integer of the red score
	 * 
	 * */
	public GameBoardModel(int[][] board, boolean blackTurn, int blackScore, int redScore, boolean isBlackComp, boolean isRedComp) {
		this.mBoard = board;
		this.playerBlackTurn = blackTurn;
		this.blackScore = blackScore;
		this.redScore = redScore;
		this.blackComp = isBlackComp;
		this.redComp = isRedComp;
		this.gameOver = false;
	}

	/**
	 * Takes the mouse Position and generates the ID of the mouse click on the
	 * board. Assumes the board is 480 x 480 pixels
	 * 
	 * @param mPosX
	 *            mouse X position
	 * @param mPosY
	 *            mouse Y position
	 * @return integer array of size 2 representing the ID of its location on
	 *         the board
	 * */
	public int[] coordinateToID(int mPosX, int mPosY) {
		int[] ID = new int[2];
		for (int i = 0; i < BOARD_REF_POSITION.length - 1; i++) {
			if (inRange(mPosX - BOARDER, BOARD_REF_POSITION[i], BOARD_REF_POSITION[i + 1]))
				ID[1] = i;
			if (inRange(mPosY - BOARDER, BOARD_REF_POSITION[i], BOARD_REF_POSITION[i + 1]))
				ID[0] = i;
		}

		// System.out.printf("click row: %d col: %d \n", ID[0], ID[1]);
		return ID;
	}

	/**
	 * Returns true if the input id is the focused tile.
	 * 
	 * @param id
	 *            of the tile being compared to the focused id
	 * @return boolean
	 * 
	 * */
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

	/**
	 * This method converts the tile ID, an array of size 2 to the corresponding
	 * X and Y coordinate location. This method is used so translate the
	 * computerized model of the game to the visual model.
	 * 
	 * @param ID
	 *            integer array of size 2 representing the tile ID
	 * 
	 * @return integer array of size 2 representing the X and Y coordinates for
	 *         the visual board. Note that the returned X and Y values are the
	 *         upper left coordinates of the tile
	 * */
	public int[] IDtoCoordinate(int[] ID) {
		if (ID[0] > 7 || ID[1] > 7) {
			System.out.println("ID error");
		}
		int[] coord = new int[2];
		coord[1] = BOARD_REF_POSITION[ID[0]] + BOARDER;
		coord[0] = BOARD_REF_POSITION[ID[1]] + BOARDER;
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

	/**
	 * Moves the game board piece from the selected id to the move to id
	 * 
	 * @param selectedID
	 *            integer array of size 2 of the first id
	 * @param moveToID
	 *            integer array of size 2 of the id to move to
	 * 
	 * @return void
	 * 
	 * */
	public void movePiece(int[] selectedID, int[] moveToID) {
		// System.out.println("turn (black): " + isPlayerBlackTurn());
		// System.out.println("black comp: " + this.blackComp);
		// System.out.println("red comp: " + this.redComp);
		if (isJumpingMove(selectedID, moveToID)) {
			setBoardValue(calcIdToRemove(selectedID, moveToID), EMPTY_SPACE);
			updateScore();
		}
		if (isTileEmpty(moveToID)) {
			// checking if it hits the top and bottom edge and if it isn't
			// already a king
			if ((isBlack(selectedID) && !isKing(selectedID) && !isKing(selectedID) && getIdRow(moveToID) == 0)
					|| (isRed(selectedID) && !isKing(selectedID) && !isKing(selectedID) && getIdRow(moveToID) == 7)) {
				setBoardValue(moveToID, 3 * getIdValue(selectedID));
			} else {
				setBoardValue(moveToID, getIdValue(selectedID));
			}
			// set first Id to empty space
			setBoardValue(genId(selectedID[0], selectedID[1]), EMPTY_SPACE);

		} else {
			// this should never execute
			System.out.println("\nTile is Occupied");
		}

		// checking double jump
		ArrayList<int[]> list = getPossibleMoves(moveToID, true);
		if (!list.isEmpty() && isJumpingMove(selectedID, moveToID)) {
			setFocusedLocation(moveToID);
			setMovesOnFocus(list);
		} else {
			setPlayerBlackTurn(!isPlayerBlackTurn());
			removeMovesOnFocus();
		}

	}

	/**
	 * Returns the id of the tile which is to be removed and set to and empty
	 * tile when a checkers jump is performed
	 * 
	 * @param selectedID
	 *            integer array of size 2 of the first ID
	 * @param moveToID
	 *            integer array of size 2 of the ID to move too
	 * 
	 * @return integer array of size 2 of the tile ID to remove
	 * */
	private int[] calcIdToRemove(int[] selectedID, int[] moveToID) {
		return genId((selectedID[0] + moveToID[0]) / 2, (selectedID[1] + moveToID[1]) / 2);
	}

	/*
	 * generates an ArrayList of Tile IDs of possible moves of the input ID
	 * INPUTS: - integer array of tile ID ->ei. [2,5]
	 */

	/**
	 * Returns a list of the possible moves in the form of a list of tile ID's
	 * 
	 * @param ID
	 *            integer array of size 2 of the starting tile
	 * 
	 * @param checkOnlyJumping
	 *            boolean variable that when set to true will return only the
	 *            possible jumping moves
	 * 
	 * @return ArrayList<int[]> list of possible moves
	 * 
	 * */
	public ArrayList<int[]> getPossibleMoves(int[] ID, boolean checkOnlyJumping) {
		ArrayList<int[]> moves = new ArrayList<int[]>();
		for (int i = 0; i < 4; i++) {

			// one square away
			int[] tempID = genId(getIdRow(ID) + CHECK_UP_DOWN[i], getIdCol(ID) + CHECK_LEFT_RIGHT[i]);
			// two squares away
			int[] tempID_2 = genId(tempID[0] + CHECK_UP_DOWN[i], tempID[1] + CHECK_LEFT_RIGHT[i]);

			if (!isOutOfBoardRange(tempID)) { // non-jumping
				if (isTileEmpty(tempID) && inMovableDirection(ID, CHECK_UP_DOWN[i])) {
					if (checkOnlyJumping) {
						continue;
					}
					moves.add(tempID);
					continue; // skip rest of loop
				}
			}
			if (!isOutOfBoardRange(tempID_2)) { // jumping
				if (isTileEmpty(tempID_2) && isOpponentPiece(ID, tempID) && inMovableDirection(ID, CHECK_UP_DOWN[i])) {
					moves.add(tempID_2);
				}
			}
		}
		return moves;
	}

	/**
	 * Returns a list of tile ID's of checkers pieces that can make moves
	 * 
	 * @param isJumpinng
	 *            a boolean variable that when set to true that when set to true
	 *            will return only pieces that can make jumping positions
	 * 
	 * @return ArrayList<int[]> a list of the tile ID's of the positions that
	 *         can make moves
	 * */
	public ArrayList<int[]> getPositions(boolean isJumping, boolean playerTurnBlack) {
		ArrayList<int[]> jumpingPos = new ArrayList<int[]>();
		for (int i = 0; i < mBoard.length; i++) {
			for (int j = 0; j < mBoard[0].length; j++) {
				int[] id = genId(i, j);
				if (playerTurnBlack) {
					if (isBlack(id) && !getPossibleMoves(id, isJumping).isEmpty()) {
						jumpingPos.add(id);
					}
				} else {
					if (isRed(id) && !getPossibleMoves(id, isJumping).isEmpty()) {
						jumpingPos.add(id);
					}
				}
			}
		}
		return jumpingPos;
	}

	/**
	 * Returns true if the input tile ID is out of the checkers board range
	 * 
	 * @param ID
	 *            integer array of size 2 of the tile ID
	 * 
	 * @return boolean
	 * 
	 * */
	private boolean isOutOfBoardRange(int[] ID) {
		if ((ID[0] < 0 || ID[0] > 7) || (ID[1] < 0 || ID[1] > 7))
			return true;
		else
			return false;
	}

	/**
	 * Creates a integer array of size 2. It takes integer one and two and
	 * creates the array [one, two]. This translates to the tile ID on the game
	 * board
	 * 
	 * @param ID_1
	 *            integer of the Y position on the board
	 * @param ID_2
	 *            integer of the X position on the board
	 * */
	public int[] genId(int ID_1, int ID_2) {
		int[] temp = { ID_1, ID_2 };
		return temp;
	}

	/**
	 * Returns two if the two input tile ID's are 2 units apart in Y position.
	 * This translates to whether the potential moves if jumping or non-jumping.
	 * It arbitrarily chooses the Y position since a checkers jumping is both
	 * two units in the X and Y
	 * 
	 * @param selectedID
	 *            integer array of size 2 of the first tile ID
	 * @param moveToID
	 *            integer array of size 2 of the move to ID
	 * 
	 * @return boolean true if Y position's differ by 2
	 * */
	public boolean isJumpingMove(int[] selectedID, int[] moveToID) {
		return (((int) Math.abs(selectedID[0] - moveToID[0]) == 2));
	}

	/**
	 * Returns true if the input tile ID represents and an empty tile on the
	 * checkers board
	 * 
	 * @param ID
	 *            integer array of size 2 of the tile
	 * 
	 * @return boolean
	 * */
	private boolean isTileEmpty(int[] ID) {
		if (getIdValue(ID) == EMPTY_SPACE)
			return true;
		else
			return false;
	}

	/**
	 * Returns true if the input tile ID represents either of black or red king
	 * 
	 * @param ID
	 *            integer array of size 2 of the tile
	 * 
	 * @return boolean
	 * */
	private boolean isKing(int[] ID) {
		if (getIdValue(ID) == CHECKER_BLACK_KING || getIdValue(ID) == CHECKER_RED_KING)
			return true;
		else
			return false;
	}

	/**
	 * Returns true if the input tile ID represents a BLACK checkers piece
	 * 
	 * @param ID
	 *            integer array of size 2 of the tile
	 * 
	 * @return boolean
	 * */
	private boolean isBlack(int[] ID) {
		if (getIdValue(ID) == CHECKER_BLACK || getIdValue(ID) == CHECKER_BLACK_KING)
			return true;
		else
			return false;
	}

	/**
	 * Returns true if the input tile ID represents a RED checkers piece
	 * 
	 * @param ID
	 *            integer array of size 2 of the tile
	 * 
	 * @return boolean
	 * */
	private boolean isRed(int[] ID) {
		if (getIdValue(ID) == CHECKER_RED || getIdValue(ID) == CHECKER_RED_KING)
			return true;
		else
			return false;
	}

	/**
	 * Returns true if the tile IDs represent different colored pieces. Uses an
	 * exclusive or to do so.
	 * 
	 * @param selectedID
	 *            integer array of size 2 of the first tile ID
	 * @param moveToID
	 *            integer array of size 2 of the move to ID
	 * */
	private boolean isOpponentPiece(int[] selectedID, int[] moveToID) {
		return isBlack(selectedID) ^ isBlack(moveToID);
	}

	/**
	 * Returns true if the tile ID representing the checkers piece is in a
	 * movable direction given the search index. The search index is either 1 or
	 * -1 representing an move up or down respectively.
	 * 
	 * @param ID
	 *            integer array of size 2 of the tile
	 * 
	 * @param seachValue
	 *            integer value that represents the direction
	 * 
	 * @return boolean
	 * */
	private boolean inMovableDirection(int[] ID, int searchValue) {
		if (isKing(ID))
			return true;
		else if (getIdValue(ID) == CHECKER_BLACK && searchValue < 0)
			return true;
		else if (getIdValue(ID) == CHECKER_RED && searchValue > 0)
			return true;
		else
			return false;
	}

	/**
	 * Returns true if the piece being represented by the tile is the same color
	 * as the current players turn. Black's turn is true and Red's turn if false
	 * 
	 * @param id
	 *            integer array of size 2 representing the tile
	 * 
	 * @return boolean
	 * */
	public boolean isIdTurn(int[] id) {
		if ((getIdValue(id) == CHECKER_BLACK || getIdValue(id) == CHECKER_BLACK_KING) && isPlayerBlackTurn())
			return true;
		else if ((getIdValue(id) == CHECKER_RED || getIdValue(id) == CHECKER_RED_KING) && isPlayerRedTurn())
			return true;
		else
			return false;
	}

	/**
	 * updates the score based on the current players turn
	 * 
	 * @return void
	 * */
	private void updateScore() {
		if (isPlayerBlackTurn()) {
			this.blackScore++;
		} else {
			this.redScore++;
		}
	}

	/**
	 * Returns true if the input tile ID represents a dark tile. Uses the notion
	 * of (-1)^(i+j) for alternating values on the game board
	 * 
	 * @param ID
	 *            integer array of size 2 representing the tile
	 * 
	 * @return boolean
	 * 
	 * */
	public boolean isDarkTile(int[] ID) {
		if (((int) Math.pow(-1, ID[0] + ID[1])) == -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the score of the current player
	 * 
	 * @return integer current score
	 * */
	private int getCurrentPlayerScore() {
		if (isPlayerBlackTurn())
			return getBlackScore();
		else
			return getRedScore();
	}

	/**
	 * Returns true if the current player is a computer
	 * 
	 * @return boolean
	 * */
	public boolean isCurrenTurnComputer() {
		if ((isPlayerBlackTurn() && this.blackComp)) {
			return true;
		} else if ((isPlayerRedTurn() && this.redComp)) {
			return true;
		} else
			return false;
	}

	/**
	 * Returns the the surrounding tile IDs of opponent pieces for a given tile
	 * ID.
	 * 
	 * @param ID
	 *            integer array of size 2 representing the tile ID
	 * 
	 * @return ArrayList<int[]> Returns an ArrayList of integer arrays of size 2
	 *         representing tile IDs
	 * */
	private ArrayList<int[]> getNearPieces(int[] ID) {
		ArrayList<int[]> tempIDs = new ArrayList<int[]>();
		for (int i = 0; i < 4; i++) {
			int[] tempID = genId(getIdRow(ID) + CHECK_UP_DOWN[i], getIdCol(ID) + CHECK_LEFT_RIGHT[i]);
			int[] tempID_2 = genId(tempID[0] + CHECK_UP_DOWN[i], tempID[1] + CHECK_LEFT_RIGHT[i]);
			// System.out.println("isOppPiece" + isOpponentPiece(ID, tempID));
			// if (!isOutOfBoardRange(tempID))
			if (!isOutOfBoardRange(tempID_2) && isOpponentPiece(ID, tempID_2)) {
				tempIDs.add(tempID_2);
			}
		}
		return tempIDs;
	}

	/**
	 * Returns true if the input tile ID can be jumped by an opponent
	 * 
	 * @param ID
	 *            integer array of size 2 representing a tile ID
	 * 
	 * @return boolean true if ID piece can be jumped
	 * */
	private boolean canPieceBeJumped(int[] ID) {
		ArrayList<int[]> surrPieces = getNearPieces(ID);
		if (surrPieces.isEmpty()) {
			return false;
		} else {
			for (int i = 0; i < surrPieces.size(); i++) {
				if (!getPossibleMoves(surrPieces.get(i), true).isEmpty()) {
					// System.out.println("can be jumped!!!!!!");
					return true;
				}
			}
			return false;
		}

	}

	/**
	 * Checker AI:
	 * Checkers Artificial intelligence. This method is the AI for a computer
	 * opponent. This AI works off a trial and error based algorithm. Firstly,
	 * if it detects a piece can be jumped it will (must) jump. Otherwise, if it detects
	 * a piece that is going to be jumped by an opponent then it will attempt to move it to
	 * a safe location. If neither of these conditions are met then it will attempt to move a piece
	 * to a safe location, one that cannot be jumped. This is done by proposing a move then checking if it 
	 * can be jumped. If the number of propositions reaches a set limit and a safe move is not found then it
	 * randomly moves a piece.
	 * 
	 * 
	 * @return void
	 * */
	public void checkerAI() {
		// TODO add AI algorithm
		

		Random rand = new Random(System.currentTimeMillis());
		ArrayList<int[]> jumpingPos = getPositions(true, isPlayerBlackTurn());


		// make move
		try {
			if (!jumpingPos.isEmpty()) {
				int rNum1 = rand.nextInt(jumpingPos.size());
				ArrayList<int[]> toIds = getPossibleMoves(jumpingPos.get(rNum1), true);
				int rNum2 = rand.nextInt(toIds.size());
				movePiece(jumpingPos.get(rNum1), toIds.get(rNum2));
			} else {

				ArrayList<int[]> nonJumpPos = getPositions(false, isPlayerBlackTurn());

				// if a piece can be jumped move it if can
				for (int i = 0; i < nonJumpPos.size(); i++) {

					ArrayList<int[]> tempMoves = getPossibleMoves(nonJumpPos.get(i), false);
					if (canPieceBeJumped(nonJumpPos.get(i)) && !tempMoves.isEmpty()) {
						movePiece(nonJumpPos.get(i), tempMoves.get(0));
						return;
					}
				}


				//generate initial random numbers and move to IDs
				int rNum1 = rand.nextInt(nonJumpPos.size());
				ArrayList<int[]> toIds = getPossibleMoves(nonJumpPos.get(rNum1), false);
				int rNum2 = rand.nextInt(toIds.size());

				int trys = 0;
				while (trys < this.tryLimit) {
					movePiece(nonJumpPos.get(rNum1), toIds.get(rNum2));
					if (!getPositions(true, isPlayerBlackTurn()).isEmpty()) {
						System.out.println("in here trys: " + trys);
						//rand.setSeed(System.currentTimeMillis());
						movePiece(toIds.get(rNum2), nonJumpPos.get(rNum1));
						rNum1 = rand.nextInt(nonJumpPos.size());
						toIds = getPossibleMoves(nonJumpPos.get(rNum1), false);
						rNum2 = rand.nextInt(toIds.size());
						trys++;

					} else {
						return;
					}
				}
				movePiece(nonJumpPos.get(rNum1), toIds.get(rNum2));

			}
		} catch (Exception e) {
			System.out.println("No more moves!");
		}

	}
	
	// (+) basic **ACCESORS & MUTATORS**

	public int getIdValue(int[] ID) {
		return mBoard[ID[0]][ID[1]];
	}

	// gets row value of ID
	private int getIdRow(int[] ID) {
		return ID[0];
	}

	// gets column value of ID
	private int getIdCol(int[] ID) {
		return ID[1];
	}

	// sets the ID value to input value
	public void setBoardValue(int[] ID, int value) {
		mBoard[ID[0]][ID[1]] = value;
	}

	public int[][] getmBoard() {
		return mBoard;
	}

	public void setmBoard(int[][] board) {
		this.mBoard = board;
	}

	// gets proposed locations to move too
	public ArrayList<int[]> getMovesOnFocus() {
		return movesOnFocus;
	}

	// sets proposed locations to move too
	public void setMovesOnFocus(ArrayList<int[]> movesOnFocus) {
		this.movesOnFocus = movesOnFocus;
	}

	// return's true if it is black's turn
	public boolean isPlayerBlackTurn() {
		return playerBlackTurn;
	}

	// returns true if it is red's turn
	public boolean isPlayerRedTurn() {
		return !playerBlackTurn;
	}

	// sets player turn
	public void setPlayerBlackTurn(boolean bool) {
		this.playerBlackTurn = bool;
	}

	// gets RED score
	public int getRedScore() {
		return redScore;
	}

	// gets BLACK score
	public int getBlackScore() {
		return blackScore;
	}

	// sets RED score
	public void setRedScore(int score) {
		this.redScore = score;
	}

	// sets BLACK score
	public void setBlackScore(int score) {
		this.blackScore = score;
	}

	// removes the list of possible moves that are on focus (being displayed)
	public void removeMovesOnFocus() {
		this.movesOnFocus.clear();
	}

	// returns the empty board
	public int[][] getBlankBoard() {
		return blankBoard;
	}

	public int[][] getDefaultBoard() {
		return mInitialBoard;
	}

	public boolean isLastStand() {
		return lastStand;
	}

	public void setLastStand(boolean lastStand) {
		this.lastStand = lastStand;
	}

	public boolean isBlackComp() {
		return blackComp;
	}

	public void setBlackComp(boolean blackComp) {
		this.blackComp = blackComp;
	}

	public boolean isRedComp() {
		return redComp;
	}

	public void setRedComp(boolean redComp) {
		this.redComp = redComp;
	}
	

	public void setGameOver(boolean b) {
		this.gameOver = b;
	}

	public boolean isGameOver() {
		return this.gameOver;
	}

	public int getTryLimit() {
		return tryLimit;
	}

	public void setTryLimit(int tryLimit) {
		this.tryLimit = tryLimit;
	}

	// (-) basic **ACCESORS & MUTATORS**
}
