package com.cole.file.io;

import java.util.ArrayList;

public class Parser {
	private boolean isBlackTurn = false;
	private int blackScore = 0;
	private int redScore = 0;
	private boolean blackComp = false;
	private boolean redComp = false;
	private final int NUM_PARSING_ELEMENTS = 5;

	public Parser() {

	}

	public int[][] parseGameBoard(ArrayList<String> dataToParse) {
		int[][] board = new int[8][8];
		this.blackComp =  strToBoolean(dataToParse.get(dataToParse.size()-5));
		this.redComp =  strToBoolean(dataToParse.get(dataToParse.size()-4));
		this.isBlackTurn = strToBoolean(dataToParse.get(dataToParse.size()-3));
		this.blackScore = Integer.parseInt(dataToParse.get(dataToParse.size()-2));
		this.redScore  = Integer.parseInt(dataToParse.get(dataToParse.size()-1));
		
		//System.out.println(dataToParse.get(dataToParse.size()-1));
		for (int i = 0; i < dataToParse.size()-NUM_PARSING_ELEMENTS; i++) {
			for (int j = 0; j < dataToParse.get(0).length(); j++) {
				String str = Character.toString(dataToParse.get(i).charAt(j));
				int integer = Integer.parseInt(str); 
				board[i][j] = integer;
			}
		}

		return board;
	}
	
	private boolean strToBoolean(String str){
		if (str.equals("true"))
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
	
	public boolean isRedComp() {
		return redComp;
	}

	public boolean isBlackComp() {
		return blackComp;
	}



}
