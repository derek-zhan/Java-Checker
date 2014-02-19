package com.checkers.gameboard;

import java.applet.Applet;
import java.awt.Frame;
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
import java.net.URL;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class GameBoardView extends Applet implements Runnable, MouseListener,ActionListener {

	private int fps = 30;
	private int threadSleepTime = (int) 1000 / fps;
	private Image image;
	public URL baseContext;
	private Graphics second;
	private GameBoardController boardController;
	private Frame frame;

	@Override
	public void init() {
		setSize(480+120, 480);
		setFocusable(true);
		addMouseListener(this);

		frame = (Frame) this.getParent().getParent();
		frame.setResizable(false);
		frame.setLocation(250, 20);
		frame.setTitle("Checkers - 2AA4");
		
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("Settings");
	    MenuItem menuItem;
		
		menuBar.add(menu);
		
		menuItem = new MenuItem("Save Game");
		menuItem.setShortcut(new MenuShortcut(KeyEvent.VK_S));
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new MenuItem("Load Game");
		menuItem.setShortcut(new MenuShortcut(KeyEvent.VK_L));
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menuItem = new MenuItem("New Game");
		menuItem.setShortcut(new MenuShortcut(KeyEvent.VK_N));
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		frame.setMenuBar(menuBar);
		
		
		baseContext = getDocumentBase();
		//image loading must be done in view since getImage extends Applet		
		ArrayList<Image> imageList = new ArrayList<Image>();
		imageList.add(getImage(baseContext, "data/Checker_Red.png"));
		imageList.add(getImage(baseContext, "data/Checker_Black.png"));
		imageList.add(getImage(baseContext, "data/Board.png"));
		imageList.add(getImage(baseContext, "data/highlighted.png"));
		imageList.add(getImage(baseContext, "data/Checker_Red_King.png"));
		imageList.add(getImage(baseContext, "data/Checker_Black_King.png"));
		imageList.add(getImage(baseContext, "data/BLACK.png"));
		imageList.add(getImage(baseContext, "data/RED.png"));
		imageList.add(getImage(baseContext, "data/BLACK_H.png"));
		imageList.add(getImage(baseContext, "data/RED_H.png"));
		imageList.add(getImage(baseContext, "data/ScoreBoard.png"));
		
		boardController = new GameBoardController(imageList);

	}

	@Override
	public void start() {
		Thread thread = new Thread(this);
		thread.start();
		
		
	}

	@Override
	public void stop() {

	}

	@Override
	public void destroy() {
		
	}

	@Override
	public void run() {
		// game loop
		while (true) {
			repaint();
			try {
				Thread.sleep(threadSleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void update(Graphics g) {
		//double buffering system
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

	@Override
	public void paint(Graphics g) {
		boardController.drawBoard(g);
	}
		
	@Override
	public void mouseClicked(MouseEvent e) {
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

}
