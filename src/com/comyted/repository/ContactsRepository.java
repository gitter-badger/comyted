package com.comyted.repository;

import com.comyted.conectivity.GetContactos;
import com.comyted.models.ClientContactSummary;
import com.enterlib.exceptions.InvalidOperationException;

public class ContactsRepository implements IContactsRepository {
	
	GetContactos getContacts;
	
	public ContactsRepository(){
		this(new GetContactos());
	}
	
	public ContactsRepository(GetContactos getContacts) {
		this.getContacts = getContacts;
	}
	

	@Override
	public ClientContactSummary[] getClientContacts(int clientId)
			throws InvalidOperationException {
		return getContacts.ObtenerContactosCliente(clientId);
	}
}
