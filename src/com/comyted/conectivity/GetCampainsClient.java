package com.comyted.conectivity;

import com.comyted.models.ContactCampaing;
import com.enterlib.exceptions.InvalidOperationException;

public class GetCampainsClient extends AppServiceClient {

	public GetCampainsClient() {
		super("GetCampanas.svc");
	}

	public ContactCampaing[] ObtenerCampanas() throws InvalidOperationException {
		return get(ContactCampaing[].class, "ObtenerCampanas");
	}
	
	public ContactCampaing[] ObtenerCampanasContacto(int cod_contacto) throws InvalidOperationException {
		return get(ContactCampaing[].class, "ObtenerCampanasContacto",
				new RequestParameter("cod_contacto", cod_contacto));
	}
}
