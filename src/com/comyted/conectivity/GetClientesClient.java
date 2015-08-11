package com.comyted.conectivity;

import com.comyted.models.Client;
import com.comyted.models.ClientSummary;
import com.enterlib.exceptions.InvalidOperationException;

public class GetClientesClient extends AppServiceClient {

	public GetClientesClient() {
		super("GetClientes.svc");
		
	}
	
	public ClientSummary[] ObtenerClientes()
			throws InvalidOperationException{	
		
		 return get(ClientSummary[].class, "ObtenerClientes");
	 }

	public Client ObtenerCliente(int cod_cliente)
			throws InvalidOperationException{
		 return get(Client.class, "ObtenerCliente", 
				 new RequestParameter("cod_cliente", cod_cliente));
	}
}
