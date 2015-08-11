package com.comyted.models;

import java.io.Serializable;

public class OrderSumary implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;
	
	public int id;
	public String titulo;
	public String planta;
	public int horastotales;
	public int minutostotales;
	public String estado;
	public String responsable;
		
	public OrderSumary() {
	
	}
	
	public OrderSumary(int id, String titulo, String planta, int totalhoras, int totalminutos, String estado) {		
		this.id = id;
		this.titulo = titulo;
		this.planta = planta;
		this.horastotales = totalhoras;
		this.minutostotales = totalminutos;
		this.estado = estado;
	}	
	
	@Override
	public String toString() {		
		return responsable!=null?responsable:"";				 
	}
}
