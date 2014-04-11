package com.checkers.gameboard;

import java.applet.Applet;
import java.awt.Choice;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * GameBoardView is the view of the checkers application. The GameBoardView is
 * the view in the MVC software pattern implementation. This class contains the
 * thread and all the graphic content of the application.
 * 
 * @author SONY
 * @version 0.3
 * @since 06-04-2014
 * */
@SuppressWarnings("serial")
public class GameBoardView extends Applet implements Runnable, MouseListener, ActionListener {

	private int fps = 10;
	// calculates the time to sleep thread to achieve desired fps
	private int threadSleepTime = (int) 1000 / fps;
	private Image image;
	private Graphics second;
	private JFrame frame;
	private GameBoardController boardController;
	private ArrayList<Choice> dropDownList = new ArrayList<Choice>();
	private Choice p1, p2, first, difficulty;

	/**
	 * Default empty class constructor
	 * 
	 * */
	public GameBoardView() {
	}

	/**
	 * Initializing method. Should be called after constructor
	 * 
	 * @return void
	 * */
	public void init() {

		// create boardController object
		try {
			boardController = new GameBoardController(setUpImages());
		} catch (IOException e) {
			// image loading error
			e.printStackTrace();
		}

		// window parameters
		setSize(480 + 120 + 120, 480);
		setFocusable(true);
		addMouseListener(this);

		frame = new JFrame();
		frame.setResizable(false);
		frame.setLocation(250, 20);
		frame.setTitle("Checkers - 2AA4");
		frame.setMenuBar(setUpMenu());
		setUpChoiceMenu();

	}

	/**
	 * Returns the frame of the application. Used in AppletContainer to allow
	 * for a stand-alone application.
	 * 
	 * @return void
	 * */
	public JFrame getFrame() {
		return this.frame;
	}

	/**
	 * Creates thread in which the application runs when called
	 * 
	 * @return void
	 * */
	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}

	/**
	 * The applications run method which contains the game loop
	 * 
	 * @return void
	 * 
	 * */
	public void run() {
		// game loop
		while (true) {
			repaint();
			boardController.update();
			try {
				Thread.sleep(threadSleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Updates the applications graphics using a double buffer system. This
	 * eliminates frame drops and gitters
	 * 
	 * @param g
	 *            Graphics object to draw too
	 * 
	 * @return void
	 * 
	 * */
	public void update(Graphics g) {
		// double buffering system
		if (image == null) {
			image = createImage(this.getWidth(), this.getHeight());
			second = image.getGraphics();
		}

		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);

		g.drawImage(image, 0, 0, this);
	}

	/**
	 * Main paint method for application. Invokes the GameBoardController
	 * classes drawGame method to convert the game into a visual entity
	 * 
	 * @param g
	 *            Graphics object to draw too
	 * 
	 * @return void
	 * 
	 * */
	public void paint(Graphics g) {
		boardController.drawGame(g);
	}

	/**
	 * Over-ridden method MouseListener, it is called every time a mouse is
	 * clicked on screen
	 * 
	 * @param e
	 *            MouseEvent object
	 * 
	 * @void void
	 * 
	 * */
	@Override
	public void mouseClicked(MouseEvent e) {

		// New Game Button
		if (e.getX() > 605 && e.getX() < (605 + 125) && e.getY() > 230 && e.getY() < (230 + 20)) {
			boardController.newDefaultGame(p1.getSelectedIndex(), p2.getSelectedIndex(), first.getSelectedIndex(), difficulty.getSelectedIndex());
		}

		// draw!
		else if (e.getX() > 605 && e.getX() < (605 + 125) && e.getY() > (480 - 40) && e.getY() < (480 - 40 + 20)) {
			boardController.drawOnBoard();
			// boardController.setStartingUp(true);
			return;
		}

		// start clock
		else if (e.getX() > 605 && e.getX() < (605 + 125) && e.getY() > 255 && e.getY() < (255 + 20)) {
			boardController.startResumeAction();
		}

		boardController.movePeice(e);

	}

	// (+) **UNUSED METHODS**
	@Override
	public void actionPerformed(ActionEvent e) {
		boardController.menuClick(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	// (-) **UNUSED METHODS**

	/**
	 * The Applications image loader. Its loads the images and puts them into
	 * and ArrayList
	 * 
	 * @exception IOException
	 * 
	 * @return ArrayList<Image>
	 * 
	 * */
	private ArrayList<Image> setUpImages() throws IOException {
		ArrayList<Image> imageList = new ArrayList<Image>();
		imageList.add(ImageIO.read(getClass().getResource("/data/Checker_Red.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/Checker_Black.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/Board.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/highlighted.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/Checker_Red_King.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/Checker_Black_King.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/BLACK.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/RED.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/BLACK_H.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/RED_H.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/ScoreBoard.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/0.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/1.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/2.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/3.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/4.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/5.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/6.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/7.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/8.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/9.png")));
		imageList.add(ImageIO.read(getClass().getResource("/data/menu.png")));
		return imageList;
	}

	/**
	 * Sets up the menu bar. This is where the save and load menu is created.
	 * 
	 * @return MenuBar
	 * */
	private MenuBar setUpMenu() {
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("Settings");
		MenuItem menuItem;

		// add menu to menuBar
		menuBar.add(menu);

		// add menu item 1
		menuItem = new MenuItem("Save Game");
		menuItem.setShortcut(new MenuShortcut(KeyEvent.VK_S));
		menuItem.addActionListener(this);
		menu.add(menuItem);

		// add menu item 2
		menuItem = new MenuItem("Load Game");
		menuItem.setShortcut(new MenuShortcut(KeyEvent.VK_L));
		menuItem.addActionListener(this);
		menu.add(menuItem);

		return menuBar;
	}

	/**
	 * Sets up the Choice (drop-down) menus and adds them to the frame
	 * 
	 * @return void
	 * 
	 * */
	private void setUpChoiceMenu() {
		// drop down 1 player1
		p1 = new Choice();
		p1.setLocation(480 + 120 + 15, 45 + 15 + 5);
		p1.add("Human");
		p1.add("Computer");
		frame.add(p1);

		// drop down 2 player2
		p2 = new Choice();
		p2.setLocation(480 + 120 + 15, 45 + 15 + 5 + 40);
		p2.add("Human");
		p2.add("Computer");
		frame.add(p2);

		// drop down 3 who wants to go first
		first = new Choice();
		first.setLocation(480 + 120 + 15, 45 + 15 + 40 + 40 + 5);
		first.add("Player One");
		first.add("Player Two");
		first.add("Random");
		frame.add(first);

		difficulty = new Choice();
		difficulty.setLocation(480 + 120 + 15, 45 + 15 + 40 + 40 + 5 + 40);
		difficulty.add("Easy");
		difficulty.add("Medium");
		difficulty.add("Hard");
		difficulty.select(1);
		frame.add(difficulty);
	}

}
