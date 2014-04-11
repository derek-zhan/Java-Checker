package com.checkers.gameboard;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/*
 * This call acts as a container for the checkers Applet.
 * This class consists of a main method which initiliazes the Applet
 * and runs it in a frame. This is only necessary for a stand alone application.
 * -->for HTML use this can be removed
 * */
public class AppletContainer {

	public static void main(String[] args) {
		GameBoardView gbv = new GameBoardView();
				
		gbv.init();
		
		JFrame frame = gbv.getFrame();
		frame.add(gbv,BorderLayout.CENTER);
		frame.setSize(480+120+5 +140, 480+50);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gbv.start();

	}

}
