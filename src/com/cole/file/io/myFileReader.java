package com.cole.file.io;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * This class reads any file. It reads the file using input stream and will
 * work no matter the file location. This was necessary for the compiled jar executable
 * to open the files correctly
 * Inputs:
 * -->File inFile -> for conformity it's a file object could be string
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