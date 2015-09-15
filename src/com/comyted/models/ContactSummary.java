package com.comyted.models;

public class ContactSummary {
	public int id;
	public String nombreempresa;
	public String nombrecontacto;
	public String puesto;
	public String telefono;
	public String fax;
	public String email;
	
	@Override
	public String toString() {
		return nombreempresa !=null ? nombreempresa : super.toString();
	}
}
