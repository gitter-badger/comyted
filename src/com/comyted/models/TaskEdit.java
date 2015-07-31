package com.comyted.models;

import java.io.Serializable;
import java.util.Date;

import com.enterlib.DateUtils;

public class TaskEdit implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int codtarea = -9999;
	public int idhoja;
	public String titulo;
	public int idtecnico;
	/**fecha de inicio de la tarea*/
	public Date fecha;
	
	/**hora:min de inicio de la tarea*/
	public Date tdFecha;
	
	/**hora:min de inicio de la tarea*/
	public Date thFecha;
	public Date ddFecha;
	public Date dhFecha;
	public double horasviaje;
	public double kilometros;
	public int estado;
	
	public void init()
	{	
		fecha = DateUtils.getCurrentDate();
		tdFecha =DateUtils.getCurrentDate();
		thFecha =DateUtils.getCurrentDate();
		ddFecha =DateUtils.getCurrentDate();
		dhFecha =DateUtils.getCurrentDate();
		estado = 1;		
	}
	
	public boolean isNew(){
		return codtarea <= 0;
	}
	
	@Override
	public String toString() {		
		return titulo;
	}

	public void setTimes() {
		thFecha = DateUtils.getDateTime(fecha, thFecha);
		tdFecha = DateUtils.getDateTime(fecha, tdFecha);
		
		ddFecha = DateUtils.getDateTime(fecha, ddFecha);
		dhFecha = DateUtils.getDateTime(fecha, dhFecha);
		
	}
}
