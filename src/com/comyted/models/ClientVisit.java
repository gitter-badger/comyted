package com.comyted.models;

import java.io.Serializable;
import java.util.Date;

public class ClientVisit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	  
    public int id;
    
    public Date fechavisita;
    
//    public Date fechalimite;
//    
//    public Date fechaalta;

    public String estado;
    
    public String contacto;
    
    public String telefono;
    
    public String fax;
    
    public String email;
    
    @Override
    public String toString() {    
    	return contacto!=null? contacto:super.toString();
    }
	
}
