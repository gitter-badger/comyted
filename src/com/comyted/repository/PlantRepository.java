package com.comyted.repository;

import com.comyted.conectivity.GetHojasClient;
import com.comyted.models.IdNameValue;
import com.comyted.models.PlantEngine;
import com.enterlib.exceptions.InvalidOperationException;

public class PlantRepository implements IPlantRepository {
	
	GetHojasClient client;
	
	@Override
	public IdNameValue[] getPlantIdName() throws InvalidOperationException {
		return client.ObtenerListaPlantas();
	}

	@Override
	public PlantEngine[] getEnginesByPlant(int cod_planta)
			throws InvalidOperationException {
		return client.ObtenerMotorPlanta(cod_planta);
	}

	public PlantRepository(GetHojasClient client) {
		this.client = client;
	}

}
