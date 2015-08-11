package com.comyted.models;

import java.io.Serializable;
import java.util.Date;

import com.comyted.R;
import com.comyted.MainApp;

public class SheetDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3919687225152960402L;
	
	public int id;
	public String expediente;
	public String titulo;
	public String planta;
	public String peticionario;
	public String tipotrabajo;
	public String cargoregion;
	public String tipohoja;
	public String motor;
	public String numserie;
	public String direccion;
	public int horastotales;
	public int minutostotales;
	public int horaspausa;
	public int minutospausa;
	public String estado;	
	public Date fechafinmontaje;
	public Date fechahoja;
	public String tecnico;
	
	public SheetEdit toSheetEdit(){
		SheetEdit s = new SheetEdit();
		s.id = id;
		s.expediente = expediente;
		s.titulo = titulo;
		s.numseriemotor = numserie;
		s.direccion = direccion;
		return s;
	}
	
	public boolean isClosed(){
		String cerrada = MainApp.getInstance().getString(R.string.cerrada).toLowerCase();
		return estado.toLowerCase().equals(cerrada) || estado.toLowerCase().equals("cerrado");
	}
}
