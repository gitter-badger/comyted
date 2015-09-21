package com.comyted.models;

import java.io.Serializable;

public class Contact  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public String nombrecontacto;
	public String nombreempresa;
	public String puesto;
	public String dpto;
	public String direccion;
	public String codpos;
	public String ciudad;
	public String provincia;
	public String pais;
	public String telefono;
	public String fax;
	public String email;
	public String observaciones;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nombrecontacto!=null?nombrecontacto : super.toString();
	}	
		
}
