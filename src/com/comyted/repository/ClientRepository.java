package com.comyted.repository;

import junit.framework.Assert;

import com.comyted.conectivity.GetClientesClient;
import com.comyted.conectivity.PostClientesClient;
import com.comyted.models.Client;
import com.comyted.models.ClientSummary;
import com.enterlib.exceptions.InvalidOperationException;

public class ClientRepository implements IClientRepository {

	GetClientesClient getClient;
	PostClientesClient postClient;
	
	public ClientRepository(GetClientesClient getClient, PostClientesClient postClient) {
		this.getClient = getClient;
		this.postClient = postClient;
	}

	public ClientRepository() {
		this(new GetClientesClient(),new PostClientesClient());
	}

	@Override
	public ClientSummary[] getClients() throws InvalidOperationException {
		Assert.assertNotNull(getClient);
		return getClient.ObtenerClientes();				
	}

	@Override
	public Client getClient(int clientId) throws InvalidOperationException {
		Assert.assertNotNull(getClient);
		return getClient.ObtenerCliente(clientId);
	}

	@Override
	public boolean updateClient(Client client) throws InvalidOperationException {
		Assert.assertNotNull(postClient);
		return postClient.ActualizarCliente(client);
	}

}
