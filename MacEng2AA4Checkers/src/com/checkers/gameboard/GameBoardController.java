package com.checkers.gameboard;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.cole.file.io.FileWriter;
import com.cole.file.io.Parser;
import com.cole.file.io.myFileReader;

public class GameBoardController {

	private GameBoardModel gameBoard = new GameBoardModel();
	private GameBoardView view = new GameBoardView();
	private Image board, red, black, highlighted, red_king, black_king, black_word, red_word, black_h_word, red_h_word, score_board;

	private boolean isStartingUp = false;
	private int clickCounter = 0;

	public GameBoardController(ArrayList<Image> images) {
		// setting up all the images as arraylist
		red = images.get(0);
		black = images.get(1);
		board = images.get(2);
		highlighted = images.get(3);
		red_king = images.get(4);
		black_king = images.get(5);
		black_word = images.get(6);
		red_word = images.get(7);
		black_h_word = images.get(8);
		red_h_word = images.get(9);
		score_board = images.get(10);

	}

	// move piece logic depends on mouse event
	public void movePiece(MouseEvent e) {
		int[] id = gameBoard.coordinateToID(e.getX(), e.getY());
		if (gameBoard.isIdTurn(id)) {
			gameBoard.setMovesOnFocus(gameBoard.getPossibleMoves(id));
			gameBoard.setFocusedLocation(id);
		}
		if (!gameBoard.getMovesOnFocus().isEmpty()) {

			for (int[] ID : gameBoard.getMovesOnFocus()) {

				if (gameBoard.coordinateToID(e.getX(), e.getY())[0] == ID[0] && gameBoard.coordinateToID(e.getX(), e.getY())[1] == ID[1]) {
					gameBoard.movePiece(gameBoard.getFocusedLocation(), ID);
					gameBoard.setPlayerBlackTurn(!gameBoard.isPlayerBlackTurn());
					gameBoard.removeMovesOnFocus();
					break;
				}
			}
		}
	}
	
	// function for drawing the board
	public void drawBoard(Graphics g) {
		g.drawImage(board, 0, 0, view);
		g.drawImage(score_board, 480, 0, view);
		g.drawImage(red_word, 480, 96, view);
		g.drawImage(black_word, 480, 300, view);

		if (gameBoard.isPlayerBlackTurn()) {
			g.drawImage(black_h_word, 480, 300, view);
		} else {
			g.drawImage(red_h_word, 480, 96, view);
		}

		if (!gameBoard.getMovesOnFocus().isEmpty()) {
			for (int[] id : gameBoard.getMovesOnFocus()) {
				g.drawImage(highlighted, gameBoard.IDtoCoordinate(id)[0], gameBoard.IDtoCoordinate(id)[1], view);
			}
		}

		int[] id = new int[2];
		int[] loc;
		for (int i = 0; i < gameBoard.getmBoard().length; i++) {
			for (int j = 0; j < gameBoard.getmBoard()[0].length; j++) {
				id[0] = i;
				id[1] = j;
				switch (gameBoard.getIdValue(id)) {
				case GameBoardModel.CHECKER_RED:
					loc = gameBoard.IDtoCoordinate(id);
					g.drawImage(red, loc[0], loc[1], view);
					break;
				case GameBoardModel.CHECKER_BLACK:
					loc = gameBoard.IDtoCoordinate(id);
					g.drawImage(black, loc[0], loc[1], view);
					break;
				case GameBoardModel.CHECKER_RED_KING:
					loc = gameBoard.IDtoCoordinate(id);
					g.drawImage(red_king, loc[0], loc[1], view);
					break;
				case GameBoardModel.CHECKER_BLACK_KING:
					loc = gameBoard.IDtoCoordinate(id);
					g.drawImage(black_king, loc[0], loc[1], view);
					break;
				}

			}
		}
	}

	// setting up the menu on the right top
	public void menuClick(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Save Game":
			try {
				saveGame();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			break;
		case "Load Game":
			loadGame();
			break;
		case "New Game":
			int confirm = JOptionPane.showConfirmDialog(view, "Are You Sure You Want\n To Start a New Game?", "Confirm New Game", JOptionPane.YES_NO_OPTION);
			if (confirm == 0) {
				gameBoard = new GameBoardModel();
			}
			break;
		}
	}

	private void saveGame() throws FileNotFoundException {
		JFileChooser fileChooser = new JFileChooser();
		File file = null;
		if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
		}
		if (file != null) {
			FileWriter fileWriter = new FileWriter(convertToStringArrayList(), file);
			fileWriter.writeFile();
		}

	}

	private void loadGame() {
		JFileChooser fileChooser = new JFileChooser();
		ArrayList<String> data = new ArrayList<String>();
		File file = null;
		if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
		}

		if (file != null) {
			myFileReader fileReader = new myFileReader(file);
			try {
				data = fileReader.getFileContents();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// TODO: allow writing of whose turn it is!
			Parser parse = new Parser();
			int tempBoard [][] = parse.parseGameBoard(data);
			boolean tempTurn = parse.isBlackTurn();
			gameBoard = new GameBoardModel(tempBoard, tempTurn);
		}
	}

	// convert whole board to a string array list
	private ArrayList<String> convertToStringArrayList() {
		ArrayList<String> strList = new ArrayList<String>();
		StringBuilder strBuilder = new StringBuilder();
		for (int i = 0; i < gameBoard.getmBoard().length; i++) {
			for (int j = 0; j < gameBoard.getmBoard()[0].length; j++) {
				strBuilder.append(gameBoard.getIdValue(gameBoard.genId(i, j)));
			}
			strList.add(strBuilder.toString());
			strBuilder.setLength(0);
		}
		
		if(gameBoard.isPlayerBlackTurn()){
			strList.add("t");
		}else{
			strList.add("f");
		}
		return strList;
	}

	/*-used to ask the user if they want to select the 
	 * board or choose a standard setup
	 * */
	public void onStartUp() {
		int confirm = JOptionPane.showConfirmDialog(view,"Would you like to start a standard game (yes) or place peices (No) ?");
		if (confirm == 0) {
			System.out.println("yes");
		} else if (confirm == 1) {
			System.out.println("no");
			JOptionPane.showMessageDialog(view, "Place the Black Players", "Black Players", JOptionPane.PLAIN_MESSAGE);
			setStartingUp(true);
			gameBoard.setmBoard(gameBoard.getBlankBoard());
		} else {
		} // do nothing
		
	}

	//keeps track of whether the game is starting
	public boolean isStartingUp() {
		return isStartingUp;
	}
	
	public void setStartingUp(boolean isStartingUp) {
		this.isStartingUp = isStartingUp;
	}

	//adds peice to the board when user selected board is choosen
	public void putPeiceOnBoard(MouseEvent e) {
		int id[] = gameBoard.coordinateToID(e.getX(), e.getY());
		if (!gameBoard.isDarkTile(id)){
			JOptionPane.showMessageDialog(view, "Can Not Place Player on Light Tile!", "Uh-oh", JOptionPane.OK_OPTION);
			return;
		}
		
		if (clickCounter < 12) {
			if (gameBoard.checkTileAvailiblity(id)) {
				gameBoard.setBoardValue(id, GameBoardModel.CHECKER_BLACK);
				if (clickCounter == 11){
					JOptionPane.showMessageDialog(view, "Place the Red Players", "Red Players", JOptionPane.PLAIN_MESSAGE);
				}
				updateClickCount(id);
			}
		} else if (clickCounter >= 12) {
			if (gameBoard.checkTileAvailiblity(id)) {
				gameBoard.setBoardValue(id, GameBoardModel.CHECKER_RED);
				if (clickCounter == 23) {
					JOptionPane.showMessageDialog(view, "Get Ready To Play!", "All done!", JOptionPane.PLAIN_MESSAGE);
					setStartingUp(false);
				}
				updateClickCount(id);
			}
		}
	}

	//updates the click count to keep track of peices on the board
	private void updateClickCount(int[] ID) {
		if (gameBoard.getIdValue(ID) != 0) {
			clickCounter += 1;
		} // do nothing
	}
	
	
}
