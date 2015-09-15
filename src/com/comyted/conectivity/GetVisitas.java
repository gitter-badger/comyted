package com.comyted.conectivity;

import com.comyted.models.ClientVisit;
import com.enterlib.exceptions.InvalidOperationException;

public class GetVisitas extends AppServiceClient {

	public GetVisitas() {
		super("GetVisitas.svc");		
	}

	public ClientVisit[] ObtenerVisitasUsuarioCliente(int cod_cliente, int cod_usuario) throws InvalidOperationException{
		return get(ClientVisit[].class, "ObtenerVisitasUsuarioCliente", 
				 new RequestParameter("cod_cliente", cod_cliente),
				 new RequestParameter("cod_usuario", cod_usuario));
	}
}
