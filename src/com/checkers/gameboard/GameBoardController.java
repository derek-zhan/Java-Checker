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

	public GameBoardController(ArrayList<Image> images) {

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

	public void movePeice(MouseEvent e) {
		int[] id = gameBoard.coordinateToID(e.getX(), e.getY());
		if (gameBoard.isIdTurn(id)) {
			gameBoard.setMovesOnFocus(gameBoard.getPossibleMoves(id));
			gameBoard.setFocusedLocation(id);
		}
		if (!gameBoard.getMovesOnFocus().isEmpty()) {

			for (int[] ID : gameBoard.getMovesOnFocus()) {

				if (gameBoard.coordinateToID(e.getX(), e.getY())[0] == ID[0] && gameBoard.coordinateToID(e.getX(), e.getY())[1] == ID[1]) {
					gameBoard.movePeice(gameBoard.getFocusedLocation(), ID);
					gameBoard.setPlayerBlackTurn(!gameBoard.isPlayerBlackTurn());
					gameBoard.removeMovesOnFocus();
					break;
				}
			}
		}
	}

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
			gameBoard = new GameBoardModel(parse.parseGameBoard(data), gameBoard.isPlayerBlackTurn());
		}
	}

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
		return strList;
	}
}
