package com.checkers.gameboard;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.cole.file.io.FileWriter;
import com.cole.file.io.Parser;
import com.cole.file.io.myFileReader;

/**
 * GameBoardController is the controller of the checkers application. The
 * GameBoardController is the controller in the MVC software implementation
 * pattern. It acts as the top level Hierarchy and connects the GameBoardView
 * class to the GameBoardModel class.
 * 
 * 
 * @author SONY
 * @version 0.3
 * @since 06-04-2014
 * */
public class GameBoardController {

	private GameBoardModel gameBoard = new GameBoardModel(false, false);
	private GameBoardView gameView = new GameBoardView();
	private Image board, red, black, highlighted, red_king, black_king, black_word, red_word, black_h_word, red_h_word, score_board, num_0, num_1, num_2,
			num_3, num_4, num_5, num_6, num_7, num_8, num_9, menuButton;

	private int clickCounter = 0;
	private boolean lastStand = false;

	private myTimer timer = new myTimer(5.0);
	private myTimer timerAI = new myTimer(0.0);

	/**
	 * Class constructor
	 * 
	 * @param images
	 *            ArrayList<Image> of images
	 * 
	 * */
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
		num_0 = images.get(11);
		num_1 = images.get(12);
		num_2 = images.get(13);
		num_3 = images.get(14);
		num_4 = images.get(15);
		num_5 = images.get(16);
		num_6 = images.get(17);
		num_7 = images.get(18);
		num_8 = images.get(19);
		num_9 = images.get(20);
		menuButton = images.get(21);
	}

	/**
	 * Moves piece depending on the location of the mouse click and previously
	 * selected pieces
	 * 
	 * @param e
	 *            MouseEvent object
	 * 
	 * @return void
	 * */
	public void movePeice(MouseEvent e) {

		int[] id = gameBoard.coordinateToID(e.getX(), e.getY());

		if (gameBoard.isIdTurn(id)) {
			gameBoard.setFocusedLocation(id);

			ArrayList<int[]> jumpingPos = gameBoard.getPositions(true, gameBoard.isPlayerBlackTurn());
			if (!jumpingPos.isEmpty()) {

				// check if current mouse click id has jumping move if it does
				// show a warning that they must jump
				if (gameBoard.getPossibleMoves(id, true).isEmpty()) {
					JOptionPane.showMessageDialog(gameView, "Must Jump Your Opponent", "Must Jump!", JOptionPane.INFORMATION_MESSAGE);
				}
				// set focus moves to only jumping moves
				gameBoard.setMovesOnFocus(gameBoard.getPossibleMoves(id, true));
			} else {
				// normal move - if there is no possible jumping move
				gameBoard.setMovesOnFocus(gameBoard.getPossibleMoves(id, false));
			}
		}
		if (!gameBoard.getMovesOnFocus().isEmpty()) {
			for (int[] ID : gameBoard.getMovesOnFocus()) {
				if (id[0] == ID[0] && id[1] == ID[1]) {
					timer.setStart();
					gameBoard.movePiece(gameBoard.getFocusedLocation(), ID);
					setProperDuration();
					break;
				}
			}
		}

		// check if game is over
		if (gameBoard.getBlackScore() == 12) {
			popUpWin("Black!");
		} else if (gameBoard.getRedScore() == 12) {
			popUpWin("Red!");
		}
	}

	/**
	 * Update method called in the main game thread, used for AI and other
	 * application checks
	 * 
	 * @return void
	 * 
	 * */
	public void update() {
		if (!gameBoard.isGameOver()) {
			if (gameBoard.getBlackScore() == 12) {
				popUpWin("Black!");
				gameBoard.setGameOver(true);
				timer.setRunning(false);
			} else if (gameBoard.getRedScore() == 12) {
				popUpWin("Red!");
				gameBoard.setGameOver(true);
				timer.setRunning(false);
			}
		}

		if (timer.isTimerFinished() && lastStand) {
			if (gameBoard.isPlayerBlackTurn()) {
				popUpWin("Red");
			} else {
				popUpWin("Black");
			}
			this.lastStand = false;
			timer.setRunning(false);
		} else if (timer.isTimerFinished()) {
			JOptionPane.showMessageDialog(gameView, "You have 1 min to make a move or you loose!", "Times Up!", JOptionPane.ERROR_MESSAGE);
			timer.setDuration(1.0);
			timer.setStart();
			this.lastStand = true;
		}

		if (gameBoard.isCurrenTurnComputer()) {
			timerAI.setDuration(0.01);
			timerAI.setStart();
			while (!timerAI.isTimerFinished())
				;
			// setProperDuration();
			gameBoard.checkerAI();
		}
	}

	/**
	 * Draws the side board
	 * 
	 * @param g
	 *            Graphics object to draw too
	 * 
	 * @return void
	 * */
	private void drawSideBoard(Graphics g) {
		g.drawImage(score_board, 480, 0, gameView);
		g.drawImage(red_word, 480, 96, gameView);
		g.drawImage(black_word, 480, 300, gameView);

		if (gameBoard.isPlayerBlackTurn()) {
			g.drawImage(black_h_word, 480, 300, gameView);
		} else {
			g.drawImage(red_h_word, 480, 96, gameView);
		}
	}

	/**
	 * Draws the option pane
	 * 
	 * @param g
	 *            Graphics object to draw too
	 * 
	 * @return void
	 * */
	private void drawSideOptionPane(Graphics g) {
		Font titleFont = new Font("TimesRoman", Font.BOLD, 15);
		Font subTitle = new Font("TimesRoman", Font.BOLD, 15);
		Font subMenu = new Font("Calibri", Font.ROMAN_BASELINE, 12);
		Font button = new Font("Calibri", Font.BOLD, 15);

		// title
		g.setFont(titleFont);
		g.drawString("Options Pane", 480 + 120, 20);
		g.setColor(new Color(213, 7, 7));
		g.drawRect(480 + 120, 25, 140, 5);
		g.fillRect(480 + 120, 25, 140, 5);
		g.setColor(Color.BLACK);

		// subtitle 1
		g.setFont(subTitle);
		g.drawString("Game Settings", 480 + 120 + 20, 45);
		g.setColor(new Color(213, 7, 7));
		g.drawLine(480 + 120, 50, 480 + 120 + 140, 50);
		g.setColor(Color.BLACK);

		// sub-menu title
		g.setFont(subMenu);
		g.drawString("Player One(Red-Top)", 480 + 120, 45 + 15);

		// drop down menu 1

		g.drawString("Player Two(Black-Bottom)", 480 + 120, 45 + 15 + 40);

		// drop down menu2

		g.drawString("Starting Player", 480 + 120, 45 + 15 + 40 + 40);

		// Difficulty drop down
		g.drawString("AI Difficulty", 480 + 120, 45 + 15 + 40 + 40 + 40);

		// subtitle 2
		g.setFont(subTitle);
		g.drawString("Game Type", 480 + 120 + 30, 140 + 80);
		g.setColor(new Color(213, 7, 7));
		g.drawLine(480 + 120, 140 + 85, 480 + 120 + 140, 140 + 85);
		g.setColor(Color.BLACK);

		// button default game
		g.setFont(button);
		g.drawRect(480 + 120 + 5, 225 + 5, 125, 20);
		g.drawString("New Game", 480 + 120 + 25, 225 + 20);

		// start/resume/pause button
		g.setFont(button);
		g.drawRect(480 + 120 + 5, 225 + 5 + 20 + 5, 125, 20);
		g.drawString("Start Clock", 480 + 120 + 25 + 5 + 5, 225 + 20 + 25);

		// subtitle 3
		g.setFont(subTitle);
		g.drawString("Extras", 480 + 120 + 30, 480 - 50);
		g.setColor(new Color(213, 7, 7));
		g.drawLine(480 + 120, 480 - 45, 480 + 120 + 140, 480 - 45);
		g.setColor(Color.BLACK);

		// draw button
		g.drawRect(480 + 120 + 5, 480 - 40, 125, 20);
		g.drawString("DRAW!", 480 + 120 + 30 + 13, 480 - 25);

	}

	/**
	 * converts the integer score to an images
	 * 
	 * @param score
	 *            integer of the score must be 12 or less
	 * 
	 * @param x
	 *            integer of the x location to draw the image
	 * 
	 * @param y
	 *            integer of the y location to draw the image
	 * 
	 * @param g
	 *            Graphics object to draw too
	 * 
	 * @return void
	 * */
	private void scoreToImage(int score, int x, int y, Graphics g) {
		switch (score) {
		case 0:
			g.drawImage(num_0, x, y, gameView);
			break;
		case 1:
			g.drawImage(num_1, x, y, gameView);
			break;
		case 2:
			g.drawImage(num_2, x, y, gameView);
			break;
		case 3:
			g.drawImage(num_3, x, y, gameView);
			break;
		case 4:
			g.drawImage(num_4, x, y, gameView);
			break;
		case 5:
			g.drawImage(num_5, x, y, gameView);
			break;
		case 6:
			g.drawImage(num_6, x, y, gameView);
			break;
		case 7:
			g.drawImage(num_7, x, y, gameView);
			break;
		case 8:
			g.drawImage(num_8, x, y, gameView);
			break;
		case 9:
			g.drawImage(num_9, x, y, gameView);
			break;
		case 10:
			g.drawImage(num_1, x - 10, y, gameView);
			g.drawImage(num_0, x + 15, y, gameView);
			break;
		case 11:
			g.drawImage(num_1, x - 10, y, gameView);
			g.drawImage(num_1, x + 15, y, gameView);
			break;
		case 12:
			g.drawImage(num_1, x - 10, y, gameView);
			g.drawImage(num_2, x + 15, y, gameView);
			break;
		}
	}

	/**
	 * Main drawing method. Calls all the smaller drawing sub-units
	 * 
	 * @param g
	 *            Graphics object to draw too
	 * 
	 * @return void
	 * 
	 * */
	public void drawGame(Graphics g) {
		g.drawImage(board, 0, 0, gameView);

		drawSideBoard(g);
		drawSideOptionPane(g);
		scoreToImage(gameBoard.getBlackScore(), 515, 335, g);
		scoreToImage(gameBoard.getRedScore(), 515, 130, g);

		if (!gameBoard.getMovesOnFocus().isEmpty()) {
			for (int[] id : gameBoard.getMovesOnFocus()) {
				g.drawImage(highlighted, gameBoard.IDtoCoordinate(id)[0], gameBoard.IDtoCoordinate(id)[1], gameView);
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
					g.drawImage(red, loc[0], loc[1], gameView);
					break;
				case GameBoardModel.CHECKER_BLACK:
					loc = gameBoard.IDtoCoordinate(id);
					g.drawImage(black, loc[0], loc[1], gameView);
					break;
				case GameBoardModel.CHECKER_RED_KING:
					loc = gameBoard.IDtoCoordinate(id);
					g.drawImage(red_king, loc[0], loc[1], gameView);
					break;
				case GameBoardModel.CHECKER_BLACK_KING:
					loc = gameBoard.IDtoCoordinate(id);
					g.drawImage(black_king, loc[0], loc[1], gameView);
					break;
				}

			}
		}

		g.drawString(String.format("Clock %d s", timer.getTimeDifference()), 480 + 15, 480 / 2);
	}

	/**
	 * Action listener for the menu bar
	 * 
	 * @param e
	 *            ActionEvent object
	 * 
	 * @return void
	 * */
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
			int confirm = JOptionPane
					.showConfirmDialog(gameView, "Are You Sure You Want\n To Start a New Game?", "Confirm New Game", JOptionPane.YES_NO_OPTION);
			if (confirm == 0) {
				gameBoard = new GameBoardModel(false, false);
			}
			break;
		}
	}

	/**
	 * Saves the current game to a file
	 * 
	 * @return void
	 * 
	 * */
	private void saveGame() throws FileNotFoundException {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text File", "txt");
		fileChooser.setFileFilter(filter);

		String fname = null;
		if (fileChooser.showSaveDialog(gameView) == JFileChooser.APPROVE_OPTION) {
			fname = fileChooser.getSelectedFile().toString();
			if (!fname.endsWith(".txt")) {
				fname += ".txt";
			}

		}
		if (fname != null) {
			File file = new File(fname);
			FileWriter fileWriter = new FileWriter(convertToStringArrayList(), file);
			fileWriter.writeFile();
		}
	}

	/**
	 * Loads the game from a text file and parses the data. Creates a new
	 * GameBoardModel object with the new loaded data
	 * 
	 * @return void
	 * 
	 * */
	private void loadGame() {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text File", "txt");
		fileChooser.setFileFilter(filter);
		ArrayList<String> data = new ArrayList<String>();
		File file = null;
		if (fileChooser.showOpenDialog(gameView) == JFileChooser.APPROVE_OPTION) {
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
			int tempBoard[][] = parse.parseGameBoard(data);
			gameBoard = new GameBoardModel(tempBoard, parse.isBlackTurn(), parse.getBlackScore(), parse.getRedScore(), parse.isRedComp(), parse.isBlackComp());
		}
	}

	/**
	 * Converts the current game state to an ArrayList of Strings to be written
	 * to a file
	 * 
	 * @return ArrayList<String>
	 * 
	 * */
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

		strList.add(Boolean.toString(gameBoard.isPlayerBlackTurn()));

		strList.add(Boolean.toString(gameBoard.isBlackComp()));
		strList.add(Boolean.toString(gameBoard.isRedComp()));

		strList.add(((Integer) gameBoard.getBlackScore()).toString());
		strList.add(((Integer) gameBoard.getRedScore()).toString());
		return strList;
	}

	/**
	 * This is extra feature which allows you to draw on the board
	 * 
	 * @return void
	 * */
	public void drawOnBoard() {
		gameBoard = new GameBoardModel(false, false);
		gameBoard.setmBoard(gameBoard.getBlankBoard());
		gameBoard.setBlackScore(0);
		gameBoard.setRedScore(0);
		JOptionPane.showMessageDialog(gameView, "Left Click for Red \n Right Click for Black", "DRAW!", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Creates a new checkers game.
	 * 
	 * @param p1
	 *            String of player 1
	 * 
	 * @param p2
	 * */
	public void newDefaultGame(int p1, int p2, int startPlayer, int difficulty) {
		int confirm = JOptionPane.showConfirmDialog(gameView, "Are you sure you would \n like to start a New Game?", "New Game Confirmation",
				JOptionPane.YES_NO_OPTION);
		if (confirm == 0) {
			timer.setDuration(5.0);
			timer.setStart();
			timer.setRunning(false);
			// yes
			gameBoard = new GameBoardModel(p2 == 1, p1 == 1);

			switch (difficulty) {
			case 0:
				gameBoard.setTryLimit(5);
				break;
			case 1:
				gameBoard.setTryLimit(10);
				break;
			case 2:
				gameBoard.setTryLimit(15);
				break;
			default:
				gameBoard.setTryLimit(5);
				break;
			}

			System.out.println("Difficuly (try limit): " + gameBoard.getTryLimit());

			if (startPlayer == 0) {
				gameBoard.setPlayerBlackTurn(false);
			} else if (startPlayer == 1) {
				gameBoard.setPlayerBlackTurn(true);
			} else {
				Random rand = new Random();
				gameBoard.setPlayerBlackTurn(rand.nextBoolean());
			}

		} else {
			// no -> do nothing
			return;
		}
	}

	/**
	 * Extra bit to draw pieces on board!
	 * 
	 * @param e
	 *            MouseEvent object
	 * 
	 * @return void
	 * */
	public void putPeiceOnBoard(MouseEvent e) {
		int id[] = gameBoard.coordinateToID(e.getX(), e.getY());

		if (e.getButton() == 1) {
			gameBoard.setBoardValue(id, GameBoardModel.CHECKER_RED);
		} else if (e.getButton() == 3) {
			gameBoard.setBoardValue(id, GameBoardModel.CHECKER_BLACK);
		} else {
			gameBoard.setBoardValue(id, GameBoardModel.CHECKER_RED);
		}
	}

	/**
	 * Starts timer if it is not running
	 * 
	 * @return void
	 * */
	public void startResumeAction() {
		if (!timer.isRunning()) {
			timer.setStart();
		}
	}
	

	/**
	 * Displays a pop window that displays a win message
	 * 
	 * @param whoWon
	 *            String of who one
	 * 
	 * @return void
	 * */
	private void popUpWin(String whoWon) {
		JOptionPane.showMessageDialog(gameView, "Congrats " + whoWon + " WINS! Go to the Options Pane to start a New Game!", "WIN!",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Sets the proper timer duration based on if a jumping move can be made or
	 * not
	 * 
	 * @return void
	 * 
	 * */
	private void setProperDuration() {
		if (!gameBoard.getPositions(true, gameBoard.isPlayerBlackTurn()).isEmpty())
			timer.setDuration(1.0);
		else
			timer.setDuration(5.0);
	}

}
