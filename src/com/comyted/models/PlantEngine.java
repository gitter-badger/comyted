package com.comyted.models;

import java.io.Serializable;

public class PlantEngine extends BaseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	public String siglas;
	public String serialno;
	
	@Override
	public String toString() {	
		String value  = siglas!=null ? siglas : "no definido";
		value+= " - " + (serialno !=null? serialno: "no definido");		
		return value;
	}
}
