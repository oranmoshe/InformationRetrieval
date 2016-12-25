package com.informationretrieval;

import java.util.ArrayList;
import java.util.Comparator;


public class indexObejct 
{
	
	public	String 		   		name ;
	public	int 		   		hits;
	public  int 				id;
	public  ArrayList<Offset>   offsetArray ;


		indexObejct(){ offsetArray = new ArrayList<Offset>();id=0;}

		public String toString(){
			return "word: "+name+" hits: "+hits+" id: "+id +"\n";
		}
		public int get_post_hits(){
			return offsetArray.size();
		}
		


}

