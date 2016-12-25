package com.informationretrieval;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

//scanning a folder and responsible for printing the list of files to posting.txt
public class PostingFile 
{
	public Map<Integer,String> fileMap = new Hashtable<Integer,String>();
	public Map<Integer,Boolean> fileMapHide = new Hashtable<Integer,Boolean>();
	public ArrayList<Summary> summaryArray;
	public List<File> listofFiles;
	
	PostingFile() throws Exception
	{
		readPostFile();
		startSummary();
	}
	
	public void init(){
	}
	
	public int getCountOfFiles(){
		return listofFiles.size();
	}
	
	public int getLastIndex() throws IOException{
		 String currentdir = System.getProperty("user.dir");
		 File dir = new File(currentdir);
		 String filePath = dir.toString()+"/Data/posting.txt";		 
		 FileInputStream fstream = new FileInputStream(filePath);
		 BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		 //Read File Line By Line
		 String strLine;		 
		 int lastIndex = -1;
		 while ((strLine = br.readLine()) != null)   {
			 try{
					Scanner input = null ;
					input =  new Scanner(strLine);
					if(!input.hasNext())
						continue;
					Scanner scanner = new Scanner(input.next()).useDelimiter(",");
					lastIndex = tryParseInt(scanner.next().trim());
				}catch(Exception exc){
					
				}
		 }
		return lastIndex;	
	}
	
	
	public void readPostFile() throws Exception{
		
		 String currentdir = System.getProperty("user.dir");
		 File dir = new File(currentdir);
		 String filePath = dir.toString()+"/Data/posting.txt";		 
		 FileInputStream fstream = new FileInputStream(filePath);
		 BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		 //Read File Line By Line
		 String strLine;		 
		 listofFiles = new ArrayList<File>();
		 while ((strLine = br.readLine()) != null)   {
			 try{
					Scanner input = null ;
					input =  new Scanner(strLine);
					if(!input.hasNext())
						continue;
					Scanner scanner = new Scanner(input.next()).useDelimiter(",");
					int id = tryParseInt(scanner.next().trim());
					String path = scanner.next().trim();
					listofFiles.add(new File(path));
					String isHidden =  scanner.next().trim();
					fileMap.put(id, path);
					fileMapHide.put(id,(isHidden.equals("hide")?true:false));
					System.out.print(fileMapHide.get(id));
				}catch(Exception exc){
					
				}
		 }	
	}
	
	public void toggleFileByIndex(int index) throws IOException{		
		if(fileMapHide.get(index)){
			fileMapHide.put(index, false);
		}else{
			fileMapHide.put(index, true);
		}
		writePostFile();
	} 
	
	public void toggleFileByPath(String _path) throws IOException{
		int index =  getFileIdByPath(_path);		
		if(fileMapHide.get(index)){
			fileMapHide.put(index, false);
		}else{
			fileMapHide.put(index, true);
		}
		writePostFile();
	} 
	
	private int getFileIdByPath(String _path){		
		for(int i=0;i<fileMap.size();i++){
			if(fileMap.get(i).equals(_path)){
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
	
	// Add to post file
	public void add(List<File> files) throws Exception{
		
		// copy files 
		copyFilesToSystem(files);
		
		// update
		String currentdir = System.getProperty("user.dir");
		for(File file: files){
			int index = fileMap.size();
			fileMap.put(index, currentdir + "/system_storage/" +file.getName());
			fileMapHide.put(index,false);
			listofFiles.add(new File(currentdir + "/system_storage/" +file.getName()));
		}
		
		// write to file
		writePostFile();

	}
	
	
	private void writePostFile() throws IOException{
		
		// write to postfile
		 String currentdir = System.getProperty("user.dir");
		 File dir = new File(currentdir);
		 try {
			BufferedWriter  br = new BufferedWriter (new FileWriter(dir.toString()+"/data/posting.txt"));
			for(int i=0;i<fileMap.size();i++){
				br.write(i+","+fileMap.get(i)+","+(fileMapHide.get(i)?"hide":"show"));
				br.newLine();
			}
			 
			br.close();
		} catch (IOException e) {
			 
			e.printStackTrace();
		}
		 startSummary();
	}

	
	// Copy files to system storage folder
	public void copyFilesToSystem(List<File> files) throws FileNotFoundException  
	{
		try {
			 String currentdir = System.getProperty("user.dir");
			 File dir = new File(currentdir);
			 String path = dir.toString() + "/system_storage/";
			 for(File file: files){
				 Files.copy(file.toPath(),
				        (new File(path + file.getName())).toPath(),
				        StandardCopyOption.REPLACE_EXISTING);
			 }	
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	

	
	// initialize sammeries
	public void startSummary () throws IOException
	{	
		 summaryArray = new ArrayList<Summary>();
		 File file;
		 Scanner input = null ;
		 for (int i = 0; i < fileMap.size(); i++) 
		 {
			 file = new File(fileMap.get(i));
			  input =  new Scanner(file);
				if(!input.hasNext() || file.isHidden())
					continue;
			  Summary s = new Summary();
			  // read first line
			  try{
				  @SuppressWarnings("resource")
				Scanner scanner = new Scanner(input.next()).useDelimiter(",");  
				  s.id = i;
				  s.title= scanner.next();		
				  s.hidden = fileMapHide.get(i);				  
				  s.date = scanner.next() + '.' + scanner.next() + '.' + scanner.next();
			  }catch(Exception exc){}
			  // read all file
			  try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				  s.summary ="";
				  br.readLine();
				    for(int j = 0;j<3;j++)					 
					  {
				    	s.summary += br.readLine();
					  }	
				}
			 			
			  summaryArray.add(s);
		  }
	}
	
}
	
	
	
	


