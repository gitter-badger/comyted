package com.comyted.repository;

import com.comyted.conectivity.GetHojasClient;
import com.comyted.models.IdNameValue;
import com.enterlib.exceptions.InvalidOperationException;

public class PeticionariosRepository implements IPeticionariosRepository {

	GetHojasClient client;
	
	public PeticionariosRepository(){
		this(new GetHojasClient());
	}
	
	public PeticionariosRepository(GetHojasClient client) {
		 this.client = client;
	}
	
	@Override
	public IdNameValue[] getPeticionariosByPlant(int cod_planta)
			throws InvalidOperationException {
		return client.ObtenerPeticionarios(cod_planta);
	}

	@Override
	public String getPeticionarioEmail(int cod_peticionario)
			throws InvalidOperationException {
		return client.ObtenerEmailPeticionario(cod_peticionario);
	}

}
