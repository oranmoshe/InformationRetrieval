package com.informationretrieval;

public class Summary {
	
	public int 	  id;
	public Boolean hidden;
	public String title;
	public String date;
	public String summary;
	public int hits;
	
	Summary(){hidden=false;}
	public String toString(){
		return "<HTML>Title: "+title+"<BR>Date: "+date+" hits:" + hits +"<BR>"+summary+"<BR>#DocId: "+id+"<BR></HTML>";
	}
	
}
