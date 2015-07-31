package com.comyted.repository;

import com.comyted.models.IdNameValue;
import com.enterlib.exceptions.InvalidOperationException;

public interface IPeticionariosRepository{
	
	IdNameValue[] getPeticionariosByPlant(int cod_planta) 
			throws InvalidOperationException;
	
	String getPeticionarioEmail(int cod_peticionario)
			throws InvalidOperationException;

}