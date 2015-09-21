package com.comyted.models;

import java.io.Serializable;
import java.util.Date;

public class ClientOffert implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int id;
	public String nombreproyecto;
	public String nombrecontacto;
	public Date fechapropuesta;
	public String responsable;
	public String tipooferta;
	public String estado;
	
	@Override
	public String toString() {		
		return nombreproyecto!=null?nombreproyecto: super.toString();
	}
}
