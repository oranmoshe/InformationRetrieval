package com.informationretrieval;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
 
public class indexFile 
{

	public ArrayList<indexObejct>     indexObjectsArray; 
	public Stopwords				  stopWords;
	public List<File> 		    	  listOfFiles;

	//constructor
	public indexFile() throws IOException 
	{
		stopWords 		= 	 new Stopwords();	
		String currentdir = System.getProperty("user.dir");
		File[] localFiles = (new File(currentdir + "/system_storage")).listFiles();
		listOfFiles		=	 Arrays.asList(localFiles);
		indexObjectsArray		    = 	 new  ArrayList<indexObejct>();
		load();
	}
	

	//removes file from all lists
	public void RemoveFile(File f ) throws FileNotFoundException
	{
		
	}
	
	//adds file to all lists
	public void AddFile(File f ) throws FileNotFoundException
	{
		
	}
	
	public boolean isDocHasWordWithStopWords(int docId, String word){
		for(int i=0; i<indexObjectsArray.size();i++){
			if(indexObjectsArray.get(i).name.equals(word)){
				indexObejct io = indexObjectsArray.get(i);
				for(int j=0;j<io.offsetArray.size();j++){
					int pid = io.offsetArray.get(j).docId;
					if(docId == pid)
						return true;
				}
			}
		}
		return false;
	}	
	
	public boolean isDocHasWord(int docId, String word){
		for(int i=0; i<indexObjectsArray.size();i++){
			if(indexObjectsArray.get(i).name.equals(word) && !stopWords.isStopWord(word)){
				indexObejct io = indexObjectsArray.get(i);
				for(int j=0;j<io.offsetArray.size();j++){
					int pid = io.offsetArray.get(j).docId;
					if(docId == pid)
						return true;
				}
			}
		}
		return false;
	}
	
	int getRanking(int docId, String word){
		int counter = 0;
		for(indexObejct io: indexObjectsArray){
			if(io.name.equals(word)){
				for(Offset o: io.offsetArray){
					if(o.docId == docId)
						counter = o.hits;
				}
			}
		}
		return counter;
	}
	
	public final  boolean isSentenceTakePlace(String sentence, PostingFile pf, int docId){
		
			String expression = "";							
    		Scanner scanner = null ;
    		scanner =  new Scanner(sentence).useDelimiter(" ");	 
    		boolean skipOutOfBrackets = false;
    		boolean isQts = false;
    		while(scanner.hasNext()){
    			
    			String temp = scanner.next();	        			
    			isQts = false;
    						
    			if(temp.equals("") || skipOutOfBrackets==true){
    				continue;	    
    			}
    			if(temp.indexOf("\"")==0){
					temp = temp.substring(1);
					while(temp.substring(temp.length()-1,temp.length())== "\"" && scanner.hasNext()){
						temp += scanner.next();
					}		
					temp = temp.substring(0,temp.length()-1);
					isQts = true;
				}   			    			
    			if(temp.indexOf("(")>=0 && !isQts){
    				skipOutOfBrackets=true;
    				try{
	    				int start = sentence.indexOf("(")+1;
	    				int end = sentence.indexOf(")");
	    				String subWord = sentence.substring(start,end);
	    				expression += isSentenceTakePlace(subWord, pf, docId);    				
    				}catch(Exception exc){
    				}
    			}
    			else if(temp.indexOf(")")>=0  && !isQts){
        			skipOutOfBrackets = false;
        			continue;
    			}
    			else if(temp.equals("AND"))
    				expression += " && "; 
    			else if(temp.equals("OR"))
    				expression += " || ";
    			else if(temp.equals("NOT"))
    				expression += " !";
    			else {
    				temp = temp.toLowerCase();	     	        					     	        			    				
    				expression += (isQts?isDocHasWordWithStopWords(docId,temp):isDocHasWord(docId,temp));
    			}
	
    		}
    		// evaluate 
		    SimpleBooleanEvaluator evaluator = new SimpleBooleanEvaluator();	
		    boolean result = evaluator.evaluate(expression); 
		    System.out.println("< " + expression + " = "+ result +" >");		    
			return result;		
	}
	
	// load the indexfile to objects 
		public void load() throws IOException{
			String currentdir = System.getProperty("user.dir");
			 File dir = new File(currentdir);
			 String filePath = dir.toString()+"/Data/index.txt";
			// Open the file
			 FileInputStream fstream = new FileInputStream(filePath);
			 BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			 String strLine;

			 //Read File Line By Line
			 while ((strLine = br.readLine()) != null)   {
				 List<String> tokens = Arrays.asList(strLine.split(","));				 
				 List<String> offsets = Arrays.asList(strLine.substring(strLine.indexOf("[")+1, strLine.indexOf("]")).split(","));
				 
					// create indexObject
					 indexObejct iobject = new indexObejct();
					 	iobject.id = (tryParseInt(tokens.get(1)));
						iobject.name = (tokens.get(0));
						iobject.hits = Integer.parseInt(tokens.get(1));					
						for(int j = 0;j<offsets.size()/2;j++){							
							Offset offset = new Offset(tryParseInt(offsets.get((j*2))),tryParseInt(offsets.get((j*2)+1))); 
							iobject.offsetArray.add(offset);
						}					
						indexObjectsArray.add(iobject);							
			 }
			
			 //Close the input stream
			 br.close();
		}
	
	// Scan list of file and indexing words
	public void scanAndIndexNewFiles(List<File> filse, int lastDocId) throws FileNotFoundException  
	{	  		
		  int docId = lastDocId;
		  String word = null;
		  File file = null ;
		  Scanner scanner = null ;
		  List<String> words = new ArrayList<String>();
		  List<String> wordsCopy = new ArrayList<String>();		 
		  
		  for (int i = 0; i < filse.size(); i++) { // for each file
		      if (filse.get(i).isFile() ) 
		      {
		    	docId++; 
		    	file = new File(filse.get(i).getPath());		    	 
		    	scanner =  new Scanner(file);
		    	
		    	// Break up the file into parts (upper to lower letters)
		    	while(scanner.hasNext() ) 
		    	{
		    		word = scanner.next().replaceAll("[^a-zA-Z0-9]","");
		    		if(!word.equals("")){
							words.add(word.toLowerCase());
							wordsCopy.add(word.toLowerCase()); 
					}		    			    		
		    	}
		    	
		    	// sorting  the words 
		    	Collections.sort(words);
		    	Collections.sort(wordsCopy);
		    	
		    	// merging  the words
		    	Set<String> hashset = new HashSet<String>(words);
		        words = new ArrayList<String>(hashset);
		        
		        // For each word in doc 
				for(int j=0;j<words.size();j++)
				{				
					// counting how many times a word occurs in the doc
					int counter=0;
					for(int k=0;k<wordsCopy.size();k++){
						if(wordsCopy.get(k).equals(words.get(j))){
							counter++;
						}
					}
					System.out.println("counter" +counter);
					
					// if word exist in system increment hits and add offset
					int wordIndex = isWordExistIndex(words.get(j));
					if(wordIndex != -1){
						Offset offset = new Offset(docId,counter);
						indexObjectsArray.get(wordIndex).offsetArray.add(offset);
						indexObjectsArray.get(wordIndex).hits++;
					}
					else{
						// else creating instance
						indexObejct iobject = new indexObejct();
						iobject.id=i;					
						iobject.hits = 1;
						iobject.name = words.get(j);	
						Offset offset = new Offset(docId,counter);
						iobject.offsetArray.add(offset);
						indexObjectsArray.add(iobject);
						Collections.sort(indexObjectsArray, new CustomComparator());
					}
				}
				
				//  update indexfile with new words
				WriteToIndexFile();
								
				// clear words
				words.clear();
				wordsCopy.clear();
		      } 
		 
		  }
		  scanner.close();
	}
	

	// Writing the index.txt 
	public void WriteToIndexFile() 
	{
		 String currentdir = System.getProperty("user.dir");
		 File dir = new File(currentdir);
		 try {
			BufferedWriter  br = new BufferedWriter (new FileWriter(dir.toString()+"/Data/index.txt"));
		 
			for(int i=0;i<indexObjectsArray.size();i++){
				String aaa = indexObjectsArray.get(i)
						.offsetArray.toString();
				br.write(indexObjectsArray.get(i).name+","+indexObjectsArray.get(i).offsetArray.size() +","+indexObjectsArray.get(i)
					.offsetArray.toString());
				br.newLine();
			}
			br.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	// Checks if word is exist
	public Boolean isWordExist(String s)
	{
		for(int i=0;i<indexObjectsArray.size();i++){
			if(indexObjectsArray.get(i).name.equals(s)){
				return false;
			}
		}
		
		return true;
	}

	// Checks if word is exist and return index of it.
	public int isWordExistIndex(String s)
	{
		for(int i=0;i<indexObjectsArray.size();i++){
			if(indexObjectsArray.get(i).name.equals(s)){
				return i;
			}
		}
		return -1;
	}
	
	int tryParseInt(String value) { 
		 try {  
		     return  Integer.parseInt(value.trim()); 
		  } catch(NumberFormatException nfe) {  
		      // Log exception.
		      return 0;
		  }  
		}

}