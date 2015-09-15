package com.comyted.models;

import java.io.Serializable;
import java.util.Date;

public class ClientOffert implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int codoferta;
	public String descripcion;
	public String nombrecontacto;
	public Date fechapropuesta;
	public String responsable;
	public String tipooferta;
	public String estadooferta;
	
	@Override
	public String toString() {		
		return nombrecontacto!=null?nombrecontacto: super.toString();
	}
}
