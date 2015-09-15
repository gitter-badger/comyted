package com.comyted.conectivity;

import com.comyted.models.ClientOffert;
import com.enterlib.exceptions.InvalidOperationException;

public class GetOfertasClient extends AppServiceClient {

	public GetOfertasClient() {
		super("GetOfertas.svc");		
	}
	
	public ClientOffert[] ObtenerOfertasCliente(int cod_cliente) throws InvalidOperationException{
		 return get(ClientOffert[].class, "ObtenerOfertasCliente",
				 new RequestParameter("cod_cliente", cod_cliente));
	}

}
