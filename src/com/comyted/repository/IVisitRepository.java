package com.comyted.repository;

import com.comyted.models.ContactVisit;
import com.enterlib.exceptions.InvalidOperationException;

public interface IVisitRepository {	
	ContactVisit[] getClientVisits(int clientId, int userId)throws InvalidOperationException;
}
