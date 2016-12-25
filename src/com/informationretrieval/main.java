package com.informationretrieval;
import java.awt.BorderLayout;

import java.awt.image.*;
import javax.imageio.*;

import java.awt.Color;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class main extends JFrame
{
	private indexFile 		 in;
	private PostingFile 	 pf;
	
	private JPanel 			 contentPane ;
	private JTextField  	 textField;
	private JList 			 list;
	private JScrollPane 	  sp;
	private DefaultListModel model;
	
	private ArrayList<String> searchWordsArray;
	
	 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					main frame = new main();
					//frame.checkFiles();
					 
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 * @throws Exception 
	 */
	public main() throws Exception 
	{

		searchWordsArray =new ArrayList<String>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);
		setBounds(600, 100, 600, 600);
		setTitle("Search Engine");
		
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.white);
		BufferedImage img = null;

		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setBounds(83, 158, 308, 30);
		contentPane.add(textField);
		textField.setColumns(10);

	
		
		JButton btnImportDirectory = new JButton("Import Dictionary");
		btnImportDirectory.setBounds(0, 0, 110, 30);
		contentPane.add(btnImportDirectory);

		JButton btnExit = new JButton("Exit");
		btnExit.setBounds(490, 0, 110, 30);
		contentPane.add(btnExit);
		
		JLabel lblFilesInDatabase = new JLabel("Files: ");
		lblFilesInDatabase.setBounds(260,10, 107, 14);
		contentPane.add(lblFilesInDatabase);
		
		final JLabel NumberFileslabel = new JLabel("0");
		NumberFileslabel.setBounds(300,10, 46, 14);
		contentPane.add(NumberFileslabel);
		
		model = new DefaultListModel();
		list = new JList();
 
		list.setModel(model);
		list.setBounds(42, 200, 100, 100);
		sp = new JScrollPane( list);
		sp.setSize(507, 309);
		sp.setLocation(42, 230);
		contentPane.add(sp, BorderLayout.CENTER);
	
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(400, 158, 100, 30);
		contentPane.add(btnSearch);
		
		final JButton btnNameFiles = new JButton("List Of Files");
		btnNameFiles.setBounds(100, 0, 110, 30);
		contentPane.add(btnNameFiles);	
		
		JButton btnAnd = new JButton("AND");
		btnAnd.setBounds(170, 190, 75, 25);
		contentPane.add(btnAnd);
		
		JButton btnOr = new JButton("OR");
		btnOr.setBounds(260, 190, 75, 25);
		contentPane.add(btnOr);
		
		JButton btnNot = new JButton("NOT");
		btnNot.setBounds(350, 190, 75, 25);
		contentPane.add(btnNot);
		
		JButton btnReadMe = new JButton("ReadMe");
		btnReadMe.setBounds(390, 0, 110, 30);
		contentPane.add(btnReadMe);
	
		String currentdir = System.getProperty("user.dir");
		  ImageIcon icon = new ImageIcon(currentdir + "/logo.png");
		  JLabel label = new JLabel(icon);

		  try {
		         BufferedImage img2 = ImageIO.read(new File(currentdir + "/logo.png"));
		         ImageIcon icon2 = new ImageIcon(img2);
		         JLabel label2 = new JLabel(icon);
		         contentPane.add(label2);
		         label2.setBounds(140, 40, 300, 101);
		      } catch (IOException e) {
		         e.printStackTrace();
		      }
		  
		  
		  

		// System Initiations 
		//
		
		pf = new PostingFile();// posting files
		NumberFileslabel.setText(String.valueOf(pf.fileMap.size()));				
		in = new indexFile();// index files	
		
		
		//
		// Listeners
		//
		
		// Import files by choose specific folder 
		btnImportDirectory.addActionListener(
					new ActionListener(){
						public void actionPerformed(ActionEvent e)
						{
						    JFileChooser fileChooser = new JFileChooser();
						    fileChooser.setCurrentDirectory(new java.io.File("."));
						    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
						    fileChooser.setMultiSelectionEnabled(true);
						    int returnValue = fileChooser.showOpenDialog(null);
					        if (returnValue == JFileChooser.APPROVE_OPTION) 
					        {
					        	try {
					        			File[] files = fileChooser.getSelectedFiles();
					        			int lastIndex = pf.getLastIndex();
					        			
					        			pf.add(Arrays.asList(files));					        			
					        			NumberFileslabel.setText(String.valueOf(pf.fileMap.size()));
					        			
					        			List<File> importFiles = Arrays.asList(files);
					        			in.scanAndIndexNewFiles(importFiles, lastIndex);
					        			
									} catch(Exception exc){
										System.out.println("Import Was Canceled: " + exc);
									}
					        }
					        else
					        {
					        	System.out.println("Import Was Canceled");
					        }
						}
					}
				);
		
		
		// Exit
		btnExit.addActionListener(
					new ActionListener(){
						public void actionPerformed(ActionEvent e)
						{
							dispose();
						}
					}
				);

		
		 // Read Me Button
		btnReadMe.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{
						   
						JFrame jf = new JFrame("Read Me");
                		jf.setBounds(750, 200, 500, 500);
                		
                		JTextPane pane = new JTextPane();
                		pane.setEditable(false);
                		SimpleAttributeSet set = new SimpleAttributeSet();
                		StyleConstants.setBold(set, true);
                		
                	   
                		StyleConstants.setFontSize(set, 14);
                	    StyleConstants.setItalic(set, true);
                	    
                	    
                	    pane.setCharacterAttributes(set, true);
                	    Document doc = pane.getStyledDocument();
                	    
                	    String w = "Oran Moshe & Mark Pertzovsky";
                	    
                	    try {
							doc.insertString(doc.getLength(),w , set);
						} catch (BadLocationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                	    
                	    Container cp = jf.getContentPane();
                	    JScrollPane scrollPane = new JScrollPane(pane);
                	    cp.add(scrollPane, BorderLayout.CENTER);

                	    jf.setVisible(true);	
					}
				}
			);
		 
		

		// Search
		btnSearch.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
                
            	model.clear();
            	searchWordsArray.clear();
            	
            	String word = 	textField.getText().toString();	 
				System.out.println("\nsentence: " + word);	 
				
				ArrayList<Integer> docsResults = new ArrayList<Integer>();
				
				// for every doc search sentence
				for(int i=0;i<pf.fileMap.size();i++){
						if(in.isSentenceTakePlace(word,pf,i)){
							docsResults.add(i); // add result
						}
				}
				
				// fill searchWordsArray
				Scanner scanner  = new Scanner(word);				
				while(scanner.hasNext()){
					String temp = scanner.next();
					if(temp.indexOf("(")==0){
						temp = temp.substring(1,temp.length());
					}
					if(temp.indexOf(")")==temp.length()-1 && temp.length()>0){
						temp = temp.substring(0,temp.length()-1);
					}			
					if(temp.indexOf("\"")==0){
						temp = temp.substring(1);
						while(temp.substring(temp.length()-1,temp.length())== "\""){
							temp += scanner.next();
						}		
						temp = temp.substring(0,temp.length()-1);
					}
					if(!temp.equals("NOT") && !temp.equals("AND") && !temp.equals("OR") && temp.length() >0){						
						searchWordsArray.add(temp);
					}
				}
			
				// merging  the words
		    	Set<String> hashset = new HashSet<String>(searchWordsArray);
		    	searchWordsArray = new ArrayList<String>(hashset);
		    	
				// update ranking of word in files sammaries
				for(int docId : docsResults){
					int ranks = 0;
					for(int j=0; j<searchWordsArray.size();j++){
						ranks += in.getRanking(docId,searchWordsArray.get(j));
					}	
					pf.summaryArray.get(docId).hits = ranks;
				}
				
				// show result for every doc 
		    	boolean isMatches = false;
			   for(int i=0;i<docsResults.size();i++){
				   if(!pf.summaryArray.get(docsResults.get(i)).hidden){ // is document hidden ?
       				Summary summery = pf.summaryArray.get(docsResults.get(i));
       				model.addElement(summery);
       				isMatches = true;
   				}
				   if(!isMatches)
					   model.addElement("No Matches");
				
			   }
			   System.out.println("search for:" + searchWordsArray);
			   
            }
        });


		btnNameFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				model.clear();
				if(pf != null){ 
				for(int j=0;j<pf.fileMap.size();j++)
				{
					
					model.addElement(pf.fileMap.get(j) + " " + (pf.fileMapHide.get(j)?",hide":",show"));
					
				}
				}else{
					model.addElement("No Files In DataBase.");
				}
				
			}
		});

		btnAnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				model.clear();
				textField.setText(textField.getText() + " AND " );
				
				
			}
		});

		btnOr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				model.clear();
				textField.setText(textField.getText() + " OR " );
				
			}
		});

		btnNot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				model.clear();
				textField.setText(textField.getText() + " NOT " );
				
			}
		});

		list.addListSelectionListener(new ListSelectionListener() {

	            @Override
	            public void valueChanged(ListSelectionEvent arg0) {
	            	
	                if (!arg0.getValueIsAdjusting()) {
	                	if(list.isSelectionEmpty()==false){
	                		
	                		 try {
			                		// creates a new window
			                		JFrame jf = new JFrame("Result...");
			                		jf.setBounds(100, 100, 600, 600);
			                		
			                		JTextPane pane = new JTextPane();
			                		pane.setEditable(false);
			                		SimpleAttributeSet set = new SimpleAttributeSet();
			                		
			                		pane.setCharacterAttributes(set, true);
			                	   
			                		StyleConstants.setFontSize(set, 14);
			                	  
			                	    
			                	    StyleConstants.setBackground(set, Color.white);
			                	    Document doc = pane.getStyledDocument();
		
			                	    
			                	    Container cp = jf.getContentPane();
			                	    JScrollPane scrollPane = new JScrollPane(pane);
			                	    cp.add(scrollPane, BorderLayout.CENTER);
		
			                	    jf.setSize(600, 600);
			                	    	                	   
			                	    try {
				                	    Summary summery = (Summary) list.getSelectedValue();	                	
				                	 	File file = new File(pf.fileMap.get(summery.id));
				                	
				                		String word = null;
										Scanner input =  new Scanner(file);							
										while(input.hasNext() ) 
								    	{
											boolean colored = false;
										
											word=input.next();
											word+=" ";	
											// checks if word should yellowed.
											for(int i=0;i<searchWordsArray.size();i++)
											{
												if(word.replaceAll("[^a-zA-Z0-9]","").toLowerCase().trim().equals(searchWordsArray.get(i).toString()))
												{
													StyleConstants.setBackground(set, Color.GREEN);																			
													doc.insertString(doc.getLength(), word, set);
													colored = true;
													StyleConstants.setBackground(set, Color.WHITE);
												}
												else{
													doc.insertString(doc.getLength(), word, set);
												}
											}
											
								    	}
										input.close();
										jf.setVisible(true);
			               		 }catch(Exception exc){
		                			 // List of files shown
			               			 try {
										pf.toggleFileByIndex(list.getSelectedIndex());
										 btnNameFiles.doClick();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
		                		 }
                		
	                	    } catch(ClassCastException e){
	                	    	// its path of file
	                	    	try {
									pf.toggleFileByPath(list.getSelectedValue().toString());
									list.getSelectedIndex();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
									
								}
	                	    }  	                	
	                	
	                	}
	                }	                
	            }
	        });
	}
}
