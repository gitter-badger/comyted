package com.comyted.modules.clients;

import junit.framework.Assert;

import com.comyted.models.Client;
import com.comyted.models.ClientSummary;
import com.comyted.repository.IClientRepository;
import com.enterlib.exceptions.InvalidOperationException;

public class ClientManager {
	
	IClientRepository repository;
	public ClientManager(IClientRepository repository){
		Assert.assertNotNull(repository);
		
		this.repository= repository;
	}
	public boolean save(Client client) throws InvalidOperationException {
		Assert.assertNotNull(client);
		
		 return client.id > 0 ?
				 repository.updateClient(client):false;
				 
	}
	public ClientSummary[] getClients() throws InvalidOperationException {
		return repository.getClients();
	}
	public Client getClient(int clientId) throws InvalidOperationException {
		return repository.getClient(clientId);
	}
}
