package com.cole.file.io;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/*
 * This class writes a file
 * 
 * Inputs:
 * --> ArrayList<String> of strings as lines to be written from start of list to end (top to bottom)
 * --> File ->full file location, directory and name + extension
 * 
 * */

public class FileWriter {
	private ArrayList<String> fileContent;
	private File file;
	
	public FileWriter(){}
	
	public FileWriter(ArrayList<String> fileContent, File fullFileName){
		this.setFileContent(fileContent);
		this.setFile(fullFileName);
	}
	

	public ArrayList<String> getFileContent() {
		return fileContent;
	}

	public void setFileContent(ArrayList<String> content) {
		this.fileContent = content;
	}
	
	
	public void writeFile() throws FileNotFoundException{
		PrintWriter printWriter = new PrintWriter(getFile());
		for (int i = 0; i < fileContent.size(); i++) {
			printWriter.write(fileContent.get(i));
			System.out.println(i);
			if (i == fileContent.size() -1){
				continue;
			}
			printWriter.write("\n");
		}
		printWriter.close();
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
