package com.comyted.repository;

import com.comyted.conectivity.GetHojasClient;
import com.comyted.models.IdNameValue;
import com.enterlib.exceptions.InvalidOperationException;

public class SheetTypesRepository implements ISheetTypesRepository {

	GetHojasClient client;
	
	public SheetTypesRepository(GetHojasClient client) {
		this.client = client;
	}
	
	@Override
	public IdNameValue[] getSheetTypes() throws InvalidOperationException {
		return client.ObtenerTipoHojas();
	}

	

}
