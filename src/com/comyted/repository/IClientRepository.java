package com.comyted.repository;

import com.comyted.models.Client;
import com.comyted.models.ClientSummary;
import com.enterlib.exceptions.InvalidOperationException;

public interface IClientRepository {
	
	
	/**Returns the list of clients
	 * @return An array containing the clients's short information 
	 * @throws InvalidOperationException
	 */
	ClientSummary[] getClients() throws InvalidOperationException;
	
	Client getClient(int clientId) throws InvalidOperationException;
	
	boolean updateClient(Client client) throws InvalidOperationException;
}
