package com.checkers.gameboard;

import java.applet.Applet;
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

@SuppressWarnings("serial")
public class GameBoardView extends Applet implements Runnable, MouseListener, ActionListener {

	private int fps = 60;
	//calculates the time to sleep thread to achieve desired fps
	private int threadSleepTime = (int) 1000 / fps;
	private boolean running = false;
	private Image image;
	private Graphics second;
	private JFrame frame;
	private GameBoardController boardController;

	public void init() {
		//window parameters
		setSize(480 + 120, 480);
		setFocusable(true);
		addMouseListener(this);
		
		running = true;

		frame = new JFrame();
		frame.setResizable(false);
		frame.setLocation(250, 20);
		frame.setTitle("Checkers - 2AA4");
		frame.setMenuBar(setUpMenu());
		
		try {
			boardController = new GameBoardController(setUpImages());
		} catch (IOException e) {
			//image loading error
			e.printStackTrace();
		}
		
		boardController.onStartUp();

	}
	
	//gets frame
	public JFrame getFrame(){
		return this.frame;
	}


	public void start() {
		Thread thread = new Thread(this);
		thread.start();

	}

	public void stop() {
		running = false;
		System.exit(0);
	}


	public void run() {
		// game loop
		while (running) {
			repaint();
			try {
				Thread.sleep(threadSleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}


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

	//main paint method -> calls controller draw method
	public void paint(Graphics g) {
		boardController.drawBoard(g);
	}

	//(+) mouse methods
	@Override
	public void mouseClicked(MouseEvent e) {
		
		if (boardController.isStartingUp()){
			boardController.putPeiceOnBoard(e);
		} else {
			boardController.movePeice(e);
		}
		
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
	
	//(-) end of mouse methods

	
	//image loader
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
		return imageList;
	}

	//initializes menu bar (save,load,new...)
	private MenuBar setUpMenu() {
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("Settings");
		MenuItem menuItem;

		//add menu to menubar
		menuBar.add(menu);

		//add menu item 1
		menuItem = new MenuItem("Save Game");
		menuItem.setShortcut(new MenuShortcut(KeyEvent.VK_S));
		menuItem.addActionListener(this);
		menu.add(menuItem);

		//add menu item 2
		menuItem = new MenuItem("Load Game");
		menuItem.setShortcut(new MenuShortcut(KeyEvent.VK_L));
		menuItem.addActionListener(this);
		menu.add(menuItem);

		//add menu item 3
		menuItem = new MenuItem("New Game");
		menuItem.setShortcut(new MenuShortcut(KeyEvent.VK_N));
		menuItem.addActionListener(this);
		menu.add(menuItem);
		return menuBar;
	}
	
}
