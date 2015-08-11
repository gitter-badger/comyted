package com.comyted.models;

public class Syncronizable {

	public static final int ADD = 1;
	public static final int UPDATE = 2;
	
	public int Id;
	
	/**Must be ADD or UPDATE*/
	public int Action;

	public Syncronizable() {
		super();
	}

}