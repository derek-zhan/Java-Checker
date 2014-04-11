package com.cole.file.io;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * myFileReader is a file reading class that should be capable of reading any ASCII
 * file format and storing the data to an ArrayList of strings.
 * 
 * @author SONY
 * @version 0.3
 * @since 06-04-2014
 * 
 * */
public class myFileReader {
	private File templateFile;

	public myFileReader(File templateFile) {
		this.templateFile = templateFile;
	}

	public ArrayList<String> getFileContents() throws IOException {
		ArrayList<String> codeTemplate = new ArrayList<String>();		
		FileReader fr = new FileReader(templateFile); 
	    BufferedReader txtReader = new BufferedReader(fr);

		String line = txtReader.readLine();
		

		while (line != null) {
			codeTemplate.add(line);
			line = txtReader.readLine();
		}

		txtReader.close();
		fr.close();
		return codeTemplate;
	}

	public File getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(File templateFile) {
		this.templateFile = templateFile;
	}
}