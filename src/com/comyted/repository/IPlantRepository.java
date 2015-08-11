package com.comyted.repository;

import com.comyted.models.IdNameValue;
import com.comyted.models.PlantEngine;
import com.enterlib.exceptions.InvalidOperationException;

public interface IPlantRepository {
	
	IdNameValue[] getPlantIdName()
			throws InvalidOperationException;
	
	PlantEngine[] getEnginesByPlant(int cod_planta)
			throws InvalidOperationException;
}
