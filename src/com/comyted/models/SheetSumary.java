package com.comyted.models;

import java.io.Serializable;

public class SheetSumary implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;
	
	public int id;
	public String titulo;
	public String tipohoja;
	public String planta;
	public int horastotales;
	public int minutostotales;
	public String estado;
		
	public SheetSumary() {
		super();
	}
	
	public SheetSumary(int id, String titulo, String tipohoja, String planta, int totalhoras, int totalminutos, String estado) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.tipohoja = tipohoja;
		this.planta = planta;
		this.horastotales = totalhoras;
		this.minutostotales = totalminutos;
		this.estado = estado;
	}
	
	/*public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getTipoHoja() {
		return tipohoja;
	}
	public void setTipoHoja(String tipohoja) {
		this.tipohoja = tipohoja;
	}
	public String getPlanta() {
		return planta;
	}
	public void setPlanta(String planta) {
		this.planta = planta;
	}
	public int getTotalhoras() {
		return horastotales;
	}
	public void setTotalhoras(int totalhoras) {
		this.horastotales = totalhoras;
	}
	public int getTotalMinutos() {
		return minutostotales;
	}
	public void setTotalMinutos(int totalminutos) {
		this.minutostotales = totalminutos;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}*/
}
