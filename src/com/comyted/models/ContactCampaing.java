package com.comyted.models;

import java.io.Serializable;
import java.util.Date;

public class ContactCampaing implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public String nombrecampana;
	public Date fechaini;
	public Date fechafin;
	public String tipocampana; 
	
	@Override
	public String toString() {
		return nombrecampana!=null? nombrecampana : super.toString();
	}
}
