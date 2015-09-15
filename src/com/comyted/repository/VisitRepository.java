package com.comyted.repository;

import com.comyted.conectivity.GetVisitas;
import com.comyted.models.ClientVisit;
import com.enterlib.exceptions.InvalidOperationException;

public class VisitRepository implements IVisitRepository {

	GetVisitas get;
	
	public VisitRepository(){
		this(new GetVisitas());
	}
	
	public VisitRepository(GetVisitas get) {
		this.get = get;
	}

	@Override
	public ClientVisit[] getClientVisits(int clientId, int userId)
			throws InvalidOperationException {
		return get.ObtenerVisitasUsuarioCliente(clientId, userId);
	}

}
