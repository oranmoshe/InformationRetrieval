package com.informationretrieval;

//creating a structure for the id and the number of times a word exist in a file
public	class Offset{
	
	public int docId;
	public int hits;
	
	public Offset(int _id, int _hit){
		
		docId=_id;
		hits = _hit;
		
	}
	public String toString(){
		return docId+" , "+ hits;
	}
	
}
