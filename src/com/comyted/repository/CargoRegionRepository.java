package com.comyted.repository;

import com.comyted.conectivity.GetHojasClient;
import com.comyted.models.IdNameValue;
import com.enterlib.exceptions.InvalidOperationException;

public class CargoRegionRepository implements ICargoRegionRepository {

	public CargoRegionRepository(GetHojasClient client) {
		this.client = client;
	}

	GetHojasClient client;
	
	@Override
	public IdNameValue[] getCargoRegions() throws InvalidOperationException {
		return client.ObtenerCargoRegion();
	}

}
