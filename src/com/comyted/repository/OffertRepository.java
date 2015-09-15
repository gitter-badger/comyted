package com.comyted.repository;

import com.comyted.conectivity.GetOfertasClient;
import com.comyted.models.ClientOffert;
import com.enterlib.exceptions.InvalidOperationException;

public class OffertRepository implements IOffertRepository {
	GetOfertasClient get;
	
	/* (non-Javadoc)
	 * @see com.comyted.repository.IOffertRepository#getClientOfferts(int)
	 */
	@Override
	public ClientOffert[] getClientOfferts(int clientId) throws InvalidOperationException{
		return get.ObtenerOfertasCliente(clientId);
	}
}
