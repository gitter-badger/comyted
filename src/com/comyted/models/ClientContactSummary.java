package com.comyted.models;

import java.io.Serializable;

public class ClientContactSummary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 
    public int id;    
    
    public String nombrecontacto;
    
    public String puesto;
    
    public String departamento;
    
    public String telefono;
    
    public String fax;
    
    public String email;   
    
  
    /**
	 * @param nombrecontacto
	 * @param puesto
	 * @param departamento
	 * @param telefono
	 * @param fax
	 * @param email
	 */
	public ClientContactSummary(String nombrecontacto, String puesto,
			String departamento, String telefono, String fax, String email) {
		this.nombrecontacto = nombrecontacto;
		this.puesto = puesto;
		this.departamento = departamento;
		this.telefono = telefono;
		this.fax = fax;
		this.email = email;
	}
	
	public ClientContactSummary(){
		
	}


	@Override
    public String toString() {    	
    	return nombrecontacto!=null?nombrecontacto: super.toString();
    }
}
