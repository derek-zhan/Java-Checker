package com.checkers.gameboard;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * This class acts as a container for the checkers Application, the class
 * consists of one main method which initializes the Applet and sets the
 * Applet's frame to the current window. This Class is necessary for the
 * checkers application to run on a stand-alone basis
 * 
 * @author SONY
 * @version 0.3
 * @since 06-04-2014
 * */
public class AppletContainer {

	public static void main(String[] args) {
		GameBoardView gbv = new GameBoardView();

		gbv.init();
		gbv.start();

		JFrame frame = gbv.getFrame();
		frame.add(gbv, BorderLayout.CENTER);
		frame.setSize(480 + 120 + 5 + 140, 480 + 50);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
