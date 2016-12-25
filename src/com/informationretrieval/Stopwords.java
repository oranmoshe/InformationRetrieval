package com.informationretrieval;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


import java.util.HashSet;

public class Stopwords {

	protected HashSet<String> stopWords = null;

	protected static Stopwords instance;

	static {
		if (instance == null) {
			try {
				instance = new Stopwords();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	 
	public Stopwords() throws IOException {
		stopWords = new HashSet<String>();
		
		 String currentdir = System.getProperty("user.dir");
		 File dir = new File(currentdir);
		 String filePath = dir.toString()+"/Data/stopwords.txt";
		 
		// Open the file
		 FileInputStream fstream = new FileInputStream(filePath);
		 BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		 String strLine;

		 //Read File Line By Line
		 while ((strLine = br.readLine()) != null)   {
			 add(strLine.trim());
		 }

	}
  
	public void add(String word) {
		if (word.trim().length() > 0)
			stopWords.add(word.trim().toLowerCase());
	}

	public boolean isStopWord(String word) {
		return stopWords.contains(word.toLowerCase());
	}
 
}
