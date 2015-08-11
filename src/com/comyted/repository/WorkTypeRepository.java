package com.comyted.repository;

import com.comyted.conectivity.GetHojasClient;
import com.comyted.models.IdNameValue;
import com.enterlib.exceptions.InvalidOperationException;

public class WorkTypeRepository implements IWorkTypeRepository {

	GetHojasClient client;
	
	public WorkTypeRepository(GetHojasClient client) {
		this.client = client;
	}

	@Override
	public IdNameValue[] getWorkTypes() throws InvalidOperationException {
		return client.ObtenerTipoTrabajos();
	}

}
