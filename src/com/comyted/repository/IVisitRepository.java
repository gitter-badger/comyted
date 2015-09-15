package com.comyted.repository;

import com.comyted.models.ClientVisit;
import com.enterlib.exceptions.InvalidOperationException;

public interface IVisitRepository {	
	ClientVisit[] getClientVisits(int clientId, int userId)throws InvalidOperationException;
}
