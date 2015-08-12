package com.comyted.testing.repository;

import junit.framework.Assert;
import android.content.Context;

import com.comyted.models.Client;
import com.comyted.models.ClientSummary;
import com.comyted.repository.IClientRepository;
import com.comyted.testing.JSONFileStore;
import com.enterlib.exceptions.InvalidOperationException;

/**Stub repository. 
 * Repositorio para pruebas cuando la aplicacion esta offline
 * @author ansel
 *
 */
public class LocalJSONClientRepository extends JSONFileStore 
		implements IClientRepository {

	static final String STORE = "clientes.json";
	
	Client[]clients;
	ClientSummary[]clientsSummary;
	
	public LocalJSONClientRepository(Context context) {
		super(context);
					
	}

	private void loadData() {
		clients = load(Client[].class, STORE);
		clientsSummary = new ClientSummary[clients.length];
		for (int i = 0; i < clients.length; i++) {
			ClientSummary cs = new ClientSummary();
			Client c = clients[i];
			
			cs.id = c.id;
			cs.nombreempresa = c.nombreempresa;			
			cs.direccionempresa = c.direccion;
			cs.telefono = c.telefono;
			cs.fax = c.fax;
			cs.email = c.email;
			
			clientsSummary[i] = cs;
		}
	}
	
	@Override
	public ClientSummary[] getClients() throws InvalidOperationException {
		loadData();
		return clientsSummary;
	}

	@Override
	public Client getClient(int clientId) throws InvalidOperationException {
		Assert.assertTrue(clientId > 0);		
		
		loadData();
		
		if(clientId > clients.length)
			return null;
			
		return clients[clientId - 1];
	}

	@Override
	public boolean updateClient(Client c) throws InvalidOperationException {
		Assert.assertTrue(c.id > 0);
		
		if(clients == null){
			loadData();
		}
		
		if(c.id > clients.length)
			return false;
		
		clients[c.id - 1] = c;
		ClientSummary cs = clientsSummary[c.id - 1];
		
		cs.id = c.id;
		cs.nombreempresa = c.nombreempresa;			
		cs.direccionempresa = c.direccion;
		cs.telefono = c.telefono;
		cs.fax = c.fax;
		cs.email = c.email;
		
		save(clients, STORE);
		return true;
	}

}
