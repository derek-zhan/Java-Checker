package com.cole.file.io;

import java.util.ArrayList;

public class Parser {
	private boolean isBlackTurn = false;
	private int blackScore = 0;
	private int redScore = 0;

	public Parser() {

	}

	public int[][] parseGameBoard(ArrayList<String> dataToParse) {
		int[][] board = new int[8][8];
		this.isBlackTurn = strToBoolean(dataToParse.get(dataToParse.size()-3));
		this.blackScore = Integer.parseInt(dataToParse.get(dataToParse.size()-2));
		this.redScore  = Integer.parseInt(dataToParse.get(dataToParse.size()-1));
		
		System.out.println(dataToParse.get(dataToParse.size()-1));
		for (int i = 0; i < dataToParse.size()-3; i++) {
			for (int j = 0; j < dataToParse.get(0).length(); j++) {
				String str = Character.toString(dataToParse.get(i).charAt(j));
				int integer = Integer.parseInt(str); 
				board[i][j] = integer;
			}
		}

		return board;
	}
	
	private boolean strToBoolean(String str){
		if (str == "t")
			return true;
		else
			return false;
	}

	public boolean isBlackTurn() {
		return this.isBlackTurn;
	}
	
	public int getBlackScore() {
		return this.blackScore;
	}
	
	public int getRedScore() {
		return this.redScore;
	}
}
