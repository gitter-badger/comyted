package com.comyted.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import com.enterlib.DateUtils;
import com.enterlib.annotations.Required;


public class SheetEdit implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int id = -9999;
	@Required
	public int codusuario;
	@Required
	public int codorden;
	@Required
	public int codtecnico;
	@Required
	public int codplanta;	
	@Required
	public int codpeticionario;
	@Required
	public int codcargoregion;
	@Required
	public int tipotrabajo;
	@Required
	public int tipohoja;
	@Required
	public Date fechafinmontaje;
	@Required
	public Date fechahoja;
	public int codmotorplanta;	
	public String horasmotor;
	/** 1=abierta, 2=cerrada*/
	public int codestado;
	@Required
	public String numseriemotor;
	
	public String expediente;
	@Required
	public String titulo;
	public String direccion;
	public String codpos;
	public String pais;
	public String provincia;
	public String ciudad;
	public String telefono;
	public String refpedido;
	
	public SheetEdit()
	{		
		pais = "España";
		codestado = 1;
		fechahoja = DateUtils.getCurrentDate();
		fechafinmontaje = DateUtils.getCurrentDate();
		tipohoja = 1;
		tipotrabajo = 1;
		codcargoregion = 1;
		horasmotor = "0";
	}
	
	@Override
	public String toString() {
		return String.format(Locale.ENGLISH, "id:%d,title:$s", id, titulo);
	}

}
