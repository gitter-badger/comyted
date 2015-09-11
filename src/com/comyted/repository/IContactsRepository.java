package com.comyted.repository;

import com.comyted.models.ClientContactSummary;
import com.enterlib.exceptions.InvalidOperationException;

public interface IContactsRepository {

	ClientContactSummary[] getClientContacts(int clientId) throws InvalidOperationException;

}