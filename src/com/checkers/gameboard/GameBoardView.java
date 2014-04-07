package com.checkers.gameboard;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameBoardView extends Applet implements Runnable, MouseListener, ActionListener {

	private int fps = 10;
	// calculates the time to sleep thread to achieve desired fps
	private int threadSleepTime = (int) 1000 / fps;
	private boolean running = false;
	private Image image;
	private Graphics second;
	private JFrame frame;
	private GameBoardController boardController;
	private Point startLoc = new Point(250, 20);
	private ArrayList<Choice> dropDownList = new ArrayList<Choice>();
	private Choice p1, p2, first;


	// initializer
	public void init() {
		// window parameters
		setSize(480 + 120 + 120, 480);
		setFocusable(true);
		addMouseListener(this);

		running = true;

		frame = new JFrame();
		frame.setResizable(false);
		frame.setLocation(startLoc);
		frame.setTitle("Checkers - 2AA4");
		frame.setMenuBar(setUpMenu());
		
		//move to method
		Choice c;
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
		// c.add("Computer");
		frame.add(first);
		
		try {
			boardController = new GameBoardController(setUpImages());
		} catch (IOException e) {
			// image loading error
			e.printStackTrace();
		}
	}

	// gets frame
	public JFrame getFrame() {
		return this.frame;
	}

	// starts the thread in which the application runs on
	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}

	// stopping method
	// should remove as it is not being using in current set up
	public void stop() {
		running = false;
		System.exit(0);
	}

	// run method -> holds the game loop
	public void run() {
		// game loop
		while (running) {
			repaint();
			boardController.update();
			try {
				Thread.sleep(threadSleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	// graphics updater ->contains double buffering system
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

	// main paint method -> calls controller draw method
	public void paint(Graphics g) {
		boardController.drawGame(g);
	}

	// (+) mouse methods
	@Override
	public void mouseClicked(MouseEvent e) {

		//New Game Button
		if (e.getX() > 605 && e.getX() < (605 + 125) && e.getY() > 230 && e.getY() < (230+20)) {
			System.out.println("New Game");
			System.out.println("p1: " + p1.getSelectedItem());
			System.out.println("p2: " + p2.getSelectedItem());
			boardController.newDefaultGame(p1.getSelectedItem(), p2.getSelectedItem(), first.getSelectedIndex());
		}

		
		//draw!
		else if (e.getX() > 605 && e.getX() < (605 + 125) && e.getY() > (480-40) && e.getY() < (480-40+20)) {
			System.out.println("Blank Board");
			boardController.newBlankBoard();
			boardController.setStartingUp(true);
			return;
		}
		
		else if (e.getX() > 605 && e.getX() < (605 + 125) && e.getY() > 255 && e.getY() < (255+20)) {
			System.out.println("Start/Pause");
			boardController.startResumeAction();
		}
		

		if (boardController.isStartingUp())
			boardController.putPeiceOnBoard(e);
		else
			boardController.movePeice(e);

	}

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

	// (-) end of mouse methods

	// image loader
	// move this to controller, it is now useless here since
	// it is now using ImageIO.read()
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

	// initializes menu bar (save,load,new...)
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

		// add menu item 3
//		menuItem = new MenuItem("New Game");
//		menuItem.setShortcut(new MenuShortcut(KeyEvent.VK_N));
//		menuItem.addActionListener(this);
//		menu.add(menuItem);
		return menuBar;
	}

	// returns the starting window position on screen
	// currently not used
	// (+)
	public Point getStartLoc() {
		return startLoc;
	}

	public void setStartLoc(Point startLoc) {
		this.startLoc = startLoc;
	}
	// (-)

}
