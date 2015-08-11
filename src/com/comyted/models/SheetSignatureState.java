package com.comyted.models;

import java.util.Date;

public class SheetSignatureState {
	public int codhoja;
	public int firmacliente;
	public int firmatecnico;
	public Date fechafirmacliente;
	public Date fechafirmatecnico;
	
	public boolean firmadaPorCliente(){
		return firmacliente > 0;
	}
	
	public boolean firmadaPorTecnico(){
		return firmatecnico > 0;
	}
}
