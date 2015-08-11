package com.comyted.models;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
	
	/**
	 *  Recibe todos los datos de una orden.
	 */
	private static final long serialVersionUID = 1;
	
	public static final String Planta = "Planta";
	public static final String Taller = "Taller";
	public static final String Viaje = "Viaje";
	
	public int id;
	public String titulo;
	public String planta;
	public String peticionario;
	public String expediente;
	public String tipotrabajo;
	public String motor;
	public String numserie;
	public String direccion;
	public int horastotales;
	public int minutostotales;
	public int horasreales;
	public int minutosreales;
	public int horasdescanso;
	public int minutosdescanso;                      
	public String estado;	
	public Date fechainicio;
		
	public Order() {
		super();
	}
	
	public Order(int id, String titulo, String planta, String expediente, String tipotrabajo, String motor, String numserie, 
			String direccion, int horastotales, int minutostotales, int horasreales, int minutosreales, int horasdescanso, int minutosdescanso, String estado) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.planta = planta;
		this.expediente = expediente;
		this.tipotrabajo = tipotrabajo;
		this.motor = motor;
		this.numserie = numserie;
		this.direccion = direccion;
		this.horastotales = horastotales;
		this.minutostotales = minutostotales;
		this.horasreales = horasreales;
		this.minutosreales = minutosreales;
		this.horasdescanso = horasdescanso;
		this.minutosdescanso = minutosdescanso;
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
	public String getPlanta() {
		return planta;
	}
	public void setPlanta(String planta) {
		this.planta = planta;
	}
	public String getExpediente() {
		return expediente;
	}
	public void setExpediente(String expediente) {
		this.expediente = expediente;
	}
	public String getTipoTrabajo() {
		return tipotrabajo;
	}
	public void setTipoTrabajo(String tipotrabajo) {
		this.tipotrabajo = tipotrabajo;
	}
	public String getMotor() {
		return motor;
	}
	public void setMotor(String motor) {
		this.motor = motor;
	}
	public String getNumserie() {
		return numserie;
	}
	public void setNumserie(String numserie) {
		this.numserie = numserie;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public int getHorasTotales() {
		return horastotales;
	}
	public void setHorasTotales(int horastotales) {
		this.horastotales = horastotales;
	}
	public int getMinutosTotales() {
		return minutostotales;
	}
	public void setMinutosTotales(int minutostotales) {
		this.minutostotales = minutostotales;
	}
	public int getHorasReales() {
		return horasreales;
	}
	public void setHorasReales (int horasreales) {
		this.horasreales = horasreales;
	}
	public int getMinutosReales() {
		return minutosreales;
	}
	public void setMinutosReales(int minutosreales) {
		this.minutosreales = minutosreales;
	}
	public int getHorasDescanso() {
		return horasdescanso;
	}
	public void setHorasDescanso (int horasdescanso) {
		this.horasdescanso = horasdescanso;
	}
	public int getMinutosDescanso() {
		return minutosdescanso;
	}
	public void setMinutosDescanso(int minutosdescanso) {
		this.minutosdescanso = minutosdescanso;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPeticionario() {
		return peticionario;
	}

	public void setPeticionario(String peticionario) {
		this.peticionario = peticionario;
	}*/
}
