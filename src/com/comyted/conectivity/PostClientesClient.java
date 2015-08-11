package com.comyted.conectivity;

import com.comyted.models.Client;
import com.enterlib.exceptions.InvalidOperationException;

public class PostClientesClient extends AppServiceClient {

	public PostClientesClient() {
		super("PostClientes.svc");		
	}

	public boolean ActualizarCliente(Client client) 
			throws InvalidOperationException {		
		 return post(boolean.class, "ActualizarCliente", client);
	}
}
