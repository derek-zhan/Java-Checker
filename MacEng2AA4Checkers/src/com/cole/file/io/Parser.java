package com.cole.file.io;

import java.util.ArrayList;

public class Parser {

	public Parser() {

	}
	
	// convert the context read from file to an interger 2D array
	public int[][] parseGameBoard(ArrayList<String> dataToParse) {
		int[][] board = new int[8][8];
		for (int i = 0; i < dataToParse.size(); i++) {
			for (int j = 0; j < dataToParse.get(0).length(); j++) {
				String str = Character.toString(dataToParse.get(i).charAt(j));
				int integer = Integer.parseInt(str);
				board[i][j] = integer;
			}
		}

		return board;
	}
}
