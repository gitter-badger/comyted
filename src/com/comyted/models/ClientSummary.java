/**
 * 
 */
package com.comyted.models;

import java.io.Serializable;

/**
 * @author ansel
 * This model class is used for displaying  a short information about a Client.
 */
public class ClientSummary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public String nombreempresa;
	public String direccionempresa;
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
