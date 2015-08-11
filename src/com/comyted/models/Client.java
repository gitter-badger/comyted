/**
 * 
 */
package com.comyted.models;

import java.io.Serializable;

/**
 * This class holds full user information
 *     
 * @author ansel
 */
public class Client implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int id;
	public String nombreempresa;
	public String codigo;
	public String cif;
	public String direccion;
	public String ciudad;
	public String provincia;
	public String codpos;
	public String pais;
	public String telefono;
	public String fax;
	public String email;
	
	@Override
	public String toString() {			
		return nombreempresa!=null?
						nombreempresa : 
						super.toString();
	}
	
}
