package com.comyted.models;

import java.io.Serializable;
import java.util.Date;

public class ContactVisit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 	public int id;	    
     	public Date fechavisita;	    
     	public Date fechaproxima;	    
	    public Date fechaalta;
	    public String responsable;
	    public String estado;	    	    	    	 
	    
	    @Override
	    public String toString() {    
	    	return responsable!=null? responsable:super.toString();
	    }
}
