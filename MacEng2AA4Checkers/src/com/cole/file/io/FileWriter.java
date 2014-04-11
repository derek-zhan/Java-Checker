package com.cole.file.io;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * FileWriter is a file writing class that takes an ArrayList of strings and
 * writes the contents of the list to a specified file
 * 
 * @author SONY
 * @version 0.3
 * @since 06-04-2014
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
