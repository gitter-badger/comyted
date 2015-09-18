package com.comyted.repository;

import com.comyted.conectivity.GetVisitasClient;
import com.comyted.models.ClientVisit;
import com.enterlib.exceptions.InvalidOperationException;

public class VisitRepository implements IVisitRepository {

	GetVisitasClient get;
	
	public VisitRepository(){
		this(new GetVisitasClient());
	}
	
	public VisitRepository(GetVisitasClient get) {
		this.get = get;
	}

	@Override
	public ClientVisit[] getClientVisits(int clientId, int userId)
			throws InvalidOperationException {
		return get.ObtenerVisitasUsuarioCliente(clientId, userId);
	}

}
