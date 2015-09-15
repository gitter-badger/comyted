package com.comyted.repository;

import com.comyted.models.ClientOffert;
import com.enterlib.exceptions.InvalidOperationException;

public interface IOffertRepository {

	ClientOffert[] getClientOfferts(int clientId)
			throws InvalidOperationException;

}