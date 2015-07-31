package com.comyted.models;


import java.util.Date;

import com.comyted.modules.sheets.SheetsManager;

public class Task {
	public int id;
	public int idhoja;
	public String titulo;
	public int idtecnico;	
	public Date fecha;	
	public int horastotales;
	public int minutostotales;	
	public int horaspausa;
	public int minutospausa;	
	public int horasviaje;	
	public int kilometros; 
	public String estado;
	
	public TaskEdit GetNewTask() {
		TaskEdit nt = new  TaskEdit();
		nt.codtarea = id;
		nt.idhoja = idhoja;
		nt.titulo = titulo;
		nt.idtecnico = idtecnico;
		nt.fecha = fecha;
		nt.horasviaje = horasviaje;
		nt.kilometros = kilometros;
		nt.estado = SheetsManager.IsFinish(estado)?2:1;
		return nt;
	}

}
